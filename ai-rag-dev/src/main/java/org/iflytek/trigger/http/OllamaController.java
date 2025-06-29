package org.iflytek.trigger.http;

import jakarta.annotation.Resource;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.iflytek.application.service.ChatService;
import org.iflytek.domain.request.ChatInfoReq;
import org.iflytek.domain.request.GithubBaseChatReq;
import org.iflytek.domain.request.UserBaseReq;
import org.iflytek.domain.service.IAiService;
import org.iflytek.domain.service.RagService;
import org.iflytek.domain.common.response.Response;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ollama")
public class OllamaController{

    @Resource
    private IAiService aiService;
    @Resource
    RagService ragService;
    @Resource
    ChatService chatService;
    
    @GetMapping("generate")
    public ChatResponse generate(@RequestParam ChatInfoReq chatInfoReq) {
        return aiService.generate(chatInfoReq);
    }

    @GetMapping("generate_stream")
    public Flux<ChatResponse> generateStream(@RequestParam ChatInfoReq chatInfoReq) {
        return aiService.generateStream(chatInfoReq);
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
    public Flux<ChatResponse> chatWithGithubBaseRag(@RequestBody GithubBaseChatReq githubBaseChatReq) throws GitAPIException {
        return chatService.chatWithGithubBaseRag(githubBaseChatReq);
    }
}
