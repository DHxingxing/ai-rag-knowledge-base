package org.iflytek.application.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.iflytek.application.service.ChatService;
import org.iflytek.domain.request.ChatInfoReq;
import org.iflytek.domain.request.GithubBaseChatReq;
import org.iflytek.domain.service.GitService;
import org.iflytek.domain.service.IAiService;
import org.iflytek.infrastructure.config.RagPromptConfig;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hxdu5
 * @since 2025/6/29 14:55
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Resource
    PgVectorStore pgVectorStore;
    @Resource
    IAiService iaiService;
    @Resource
    RagPromptConfig ragPromptConfig;
    @Resource
    GitService gitService;



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

    @Override
    public Flux<ServerSentEvent<String>> chatWithGithubBaseRag(GithubBaseChatReq githubBaseChatReq) throws GitAPIException, IOException {

        FileUtils.deleteDirectory(new File(githubBaseChatReq.getUserBaseReq().getLocalPath()));
        gitService.analyzeGithubBase(githubBaseChatReq.getUserBaseReq());

        SearchRequest request = SearchRequest
                .query(githubBaseChatReq.getChatInfoReq().getMessage())
                .withTopK(5)
                .withFilterExpression("knowledge == '" + githubBaseChatReq.getRagTag() + "'");

        List<Document> documents = pgVectorStore.similaritySearch(request);
        String documentCollectors = documents.stream().map(Document::getContent).collect(Collectors.joining());
        ChatInfoReq chatInfoNewReqRag = ChatInfoReq.generateNewInfoRag(githubBaseChatReq.getChatInfoReq(), documentCollectors, ragPromptConfig);
        return iaiService.generateStream(chatInfoNewReqRag)
                .map(chatResponse -> {
                    String contents = chatResponse.getResult().getOutput().getContent();
                    return ServerSentEvent.builder(contents).build();
                });
    }

}
