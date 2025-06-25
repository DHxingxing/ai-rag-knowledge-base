package org.iflytek.domain.entity;

import org.iflytek.domain.enums.message.MessageStatusEnum;

import lombok.Data;
import org.iflytek.domain.exception.BusinessException;

import static org.iflytek.domain.errorCode.MessageErrorCodeEnum.*;

@Data
public class ChatMessage {

    private final String model;
    private final String content;
    private MessageStatusEnum status;

    public ChatMessage(String model, String content) {
        this.model = model;
        this.content = content;
        this.status = MessageStatusEnum.CREATED;
    }

    public void validate() {
        if (content == null || content.isEmpty()) {
            throw new BusinessException(MESSAGE_IS_NOT_EMPTY);
        }

    }
}