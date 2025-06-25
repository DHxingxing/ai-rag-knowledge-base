package org.iflytek.domain.service.impl;

import jakarta.annotation.Resource;
import org.iflytek.domain.service.IAiService;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
public class IAiServiceImplWeb implements IAiService {

    @Resource
    private OllamaChatClient ollamaChatClient;

    @Override
    public ChatResponse generate(String model, String message) {
        return ollamaChatClient.call(new Prompt(message, OllamaOptions.create().withModel(model)));
    }

    @Override
    public Flux<ChatResponse> generateStream(String model, String message) {
        return ollamaChatClient.stream(new Prompt(message, OllamaOptions.create().withModel(model)));
    }
}
