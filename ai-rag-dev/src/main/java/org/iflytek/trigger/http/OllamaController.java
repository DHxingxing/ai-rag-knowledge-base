package org.iflytek.trigger.http;

import jakarta.annotation.Resource;
import org.iflytek.domain.service.IAiService;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/api/v1/ollama")
public class OllamaController{

    @Resource
    private IAiService aiService;
    
    @GetMapping("generate")
    public ChatResponse generate(@RequestParam String model,@RequestParam String message) {
        return aiService.generate(model, message);
    }

    @GetMapping("generate_stream")
    public Flux<ChatResponse> generateStream(@RequestParam String model, @RequestParam String message) {
        return aiService.generateStream(model, message);
    }
}
