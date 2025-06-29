package org.iflytek.infrastructure.rag;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.iflytek.domain.common.response.Response;
import org.iflytek.domain.request.ChatInfoReq;
import org.iflytek.domain.service.FileProgressService;
import org.iflytek.domain.service.IAiService;
import org.iflytek.domain.service.RagService;
import org.iflytek.domain.utils.ZipUtil;
import org.iflytek.infrastructure.config.RagPromptConfig;
import org.iflytek.infrastructure.config.UnZipConfig;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RagServiceImpl implements RagService {

    @Resource
    PgVectorStore pgVectorStore;
    @Resource
    RagPromptConfig ragPromptConfig;
    @Resource
    UnZipConfig unZipConfig;
    @Resource
    IAiService iaiService;
    @Resource
    FileProgressService fileProgressService;

    @Override
    public void uploadFile(String filePath, String ragTag) throws IOException {
        List<Document> documents = fileProgressService.setMetaData(filePath, ragTag);
        pgVectorStore.accept(documents);
    }


    @Override
    public Response<List<String>> ragModelList() {
        return null;
    }

    @Override
    public Response<String> uploadFile(String ragTag, MultipartFile files) {

        try{
            List<String> paths = ZipUtil.unZipFiles(files, unZipConfig.getLocal());
            for(String path : paths){
                if (path.contains("__MACOSX") || !path.toLowerCase().endsWith(".md")) {
                    continue;
                }
                List<Document> documents = fileProgressService.setMetaData(path, ragTag);
                pgVectorStore.accept(documents);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Response.error("上传失败" + e.getMessage());
        }
            return Response.success("上传成功");
    }

    @Override
    public Flux<ChatResponse> chatWithRag(String ragTag, ChatInfoReq chatInfoReq) {
        SearchRequest request = SearchRequest
                .query(chatInfoReq.getMessage())
                .withTopK(5)
                .withFilterExpression("knowledge == '" + ragTag + "'");
        List<Document> documents = pgVectorStore.similaritySearch(request);
        String documentCollectors = documents.stream().map(Document::getContent).collect(Collectors.joining());
        ChatInfoReq chatInfoNewReqRag = ChatInfoReq.generateNewInfoRag(chatInfoReq, documentCollectors, ragPromptConfig);
        return iaiService.generateStream(chatInfoNewReqRag);
    }

}
