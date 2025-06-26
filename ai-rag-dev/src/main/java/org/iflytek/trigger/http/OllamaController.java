package org.iflytek.trigger.http;

import jakarta.annotation.Resource;
import org.iflytek.domain.service.IAiService;
import org.iflytek.domain.service.RagService;
import org.iflytek.domain.common.response.Response;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/api/v1/ollama")
public class OllamaController{

    @Resource
    private IAiService aiService;
    @Resource
    RagService ragService;
    
    @GetMapping("generate")
    public ChatResponse generate(@RequestParam String model,@RequestParam String message) {
        return aiService.generate(model, message);
    }

    @GetMapping("generate_stream")
    public Flux<ChatResponse> generateStream(@RequestParam String model, @RequestParam String message) {
        return aiService.generateStream(model, message);
    }

    @GetMapping("rag")
    public Response<String> chatWithRag(@RequestParam String model, @RequestParam String message) {
        return ragService.chatWithRag("data/data1.md",model, message);
    }
}
