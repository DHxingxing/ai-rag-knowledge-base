package org.iflytek.infrastructure.ai;

import jakarta.annotation.Resource;
import org.iflytek.domain.request.ChatInfoReq;
import org.iflytek.domain.service.IAiService;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
public class IAiServiceImp implements IAiService {

    @Resource
    private OllamaChatClient ollamaChatClient;

    @Override
    public ChatResponse generate(ChatInfoReq chatInfoReq) {
        return ollamaChatClient.call(new Prompt(chatInfoReq.getMessage(), OllamaOptions.create().withModel(chatInfoReq.getModel())));
    }

    @Override
    public Flux<ChatResponse> generateStream(ChatInfoReq chatInfoReq) {
        return ollamaChatClient.stream(new Prompt(chatInfoReq.getMessage(), OllamaOptions.create().withModel(chatInfoReq.getModel())));
    }
}
