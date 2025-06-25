package org.iflytek.trigger.http;

import jakarta.annotation.Resource;
import org.iflytek.domain.service.IAiService;
import org.iflytek.domain.service.RagService;
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
    public String chatWithRag(){
        return ragService.chatWithRag("/Users/haisen/PostGraduate Folder/实习/个人项目/DeepSeekRAGEnforceKnowledgeBase/ai-rag-dev/src/main/java/org/iflytek/domain/service/data/data1.md","花猪的手机号是？");
    }
}
