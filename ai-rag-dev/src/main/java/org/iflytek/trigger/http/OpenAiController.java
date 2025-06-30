package org.iflytek.trigger.http;

import jakarta.annotation.Resource;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.iflytek.application.service.ChatService;
import org.iflytek.domain.common.response.Response;
import org.iflytek.domain.request.ChatInfoReq;
import org.iflytek.domain.request.GithubBaseChatReq;
import org.iflytek.domain.service.IAiService;
import org.iflytek.domain.service.RagService;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 * @author hxdu5
 * @since 2025/6/29 22:23
 */

@RestController
@RequestMapping("/api/v1/openai")
public class OpenAiController {

    @Resource
    private OpenAiChatClient openAiChatClient;
    @Resource
    private IAiService aiService;
    @Resource
    RagService ragService;
    @Resource
    ChatService chatService;


    @GetMapping("generate")
    public ChatResponse generate(@RequestParam ChatInfoReq chatInfoReq) {
        return openAiChatClient.call(new Prompt(
                chatInfoReq.getMessage(),
                OpenAiChatOptions.builder()
                        .withModel(chatInfoReq.getModel())
                        .build()
        ));
    }

    @PostMapping("uploadFiles")
    public Response<String> uploadFiles(@RequestParam("ragTag") String ragTag, @RequestParam("files") MultipartFile files) {
        return ragService.uploadFile(ragTag, files);
    }

    @GetMapping("rag")
    public Flux<ChatResponse> chatWithRag(@RequestParam String ragTag, @RequestParam ChatInfoReq chatInfoReq) {
        return ragService.chatWithRag(ragTag,chatInfoReq);
    }

    @PostMapping("rag_GithubBase")
    public Flux<ServerSentEvent<String>> chatWithGithubBaseRag(@RequestBody GithubBaseChatReq githubBaseChatReq) throws GitAPIException, IOException {
        return chatService.chatWithGithubBaseRag(githubBaseChatReq);
    }
}
