package org.iflytek.domain.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.iflytek.domain.service.RagService;
import org.iflytek.domain.common.response.Response;
import org.iflytek.infrastructure.config.RagPromptConfig;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class RagServiceImpl implements RagService {

    @Resource
    TokenTextSplitter tokenTextSplitter;
    @Resource
    PgVectorStore pgVectorStore;
    @Resource
    RagPromptConfig ragPromptConfig;
    @Resource
    ChatClient chatClient;

    private void uploadFile(String filePath) {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(filePath);
        List<Document> document = tikaDocumentReader.get();
        List<Document> documentSplitList = tokenTextSplitter.apply(document);
        document.forEach(doc -> doc.getMetadata().put("knowledge", "知识库名称"));
        documentSplitList.forEach(doc -> doc.getMetadata().put("knowledge", "知识库名称"));
        pgVectorStore.accept(documentSplitList);
    }


    @Override
    public Response<List<String>> ragModelList() {
        return null;
    }

    @Override
    public Response<String> uploadFile(String ragTag, List<MultipartFile> files) {

        String fileNames = files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.joining(","));
        log.info("上传知识库开始 {}", ragTag);
        try {
            for (MultipartFile file : files) {
                // 1. 读取文件内容为 Document 列表
                TikaDocumentReader documentReader = new TikaDocumentReader(file.getResource());
                List<Document> documents = documentReader.get();

                // 2. 文本分割
                List<Document> documentList = tokenTextSplitter.apply(documents);

                // 3. 添加元数据（可选：比如知识库标签）
                documentList.forEach(doc -> doc.getMetadata().put("knowledge", ragTag));

                // 4. 存入向量库
                pgVectorStore.accept(documentList);
            }

            return Response.success(fileNames);
        } catch (Exception e) {
            log.error("上传知识库失败", e);
            return Response.error("500", "上传失败: " + e.getMessage());
        }
    }

    @Override
    public Response<String> chatWithRag(String filePath, String model, String message) {
        uploadFile(filePath);

        SearchRequest request = SearchRequest
                .query(message)
                .withTopK(5)
                .withFilterExpression("knowledge == '知识库名称'");

        List<Document> documents = pgVectorStore.similaritySearch(request);
        String documentCollectors = documents.stream().map(Document::getContent).collect(Collectors.joining());
        String systemPrompt = ragPromptConfig.getSystem().replace("{documents}", documentCollectors);
        String userPrompt = ragPromptConfig.getUser().replace("{question}", message);

        String fullPrompt = systemPrompt + "\n\n" + userPrompt;

        var response = chatClient.call(new Prompt(fullPrompt, OllamaOptions.create().withModel(model)));

        return new Response(response.getResult().getOutput().getContent());
    }
}
