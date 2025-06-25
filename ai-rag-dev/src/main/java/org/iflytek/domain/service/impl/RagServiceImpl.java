package org.iflytek.domain.service.impl;

import jakarta.annotation.Resource;
import org.iflytek.domain.service.RagService;
import org.iflytek.infrastructure.config.RagPromptConfig;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.List;
import java.util.stream.Collectors;

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
    public String chatWithRag(String filePath, String message) {
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

        var response = chatClient.call(new Prompt(fullPrompt));

        return response.getResult().getOutput().getContent();
    }
}
