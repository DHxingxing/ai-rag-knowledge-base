package org.iflytek.domain.request;

import jakarta.annotation.Resource;
import lombok.Builder;
import lombok.Data;
import org.iflytek.infrastructure.config.RagPromptConfig;

/**
 * @author hxdu5
 * @since 2025/6/28 22:55
 */
@Data
@Builder
public class ChatInfoReq {

    @Resource
    RagPromptConfig ragPromptConfig;

    private String model;

    private String message;

    public static ChatInfoReq generateNewInfoRag(ChatInfoReq chatInfoReq,String documentCollectors, RagPromptConfig ragPromptConfig) {
        String systemPrompt = ragPromptConfig.getSystem().replace("{documents}", documentCollectors);
        String userPrompt = ragPromptConfig.getUser().replace("{question}", chatInfoReq.getMessage());
        String fullPrompt = systemPrompt + "\n\n" + userPrompt;
        return ChatInfoReq.builder()
                .model(chatInfoReq.getModel())
                .message(fullPrompt)
                .build();
    }
}
