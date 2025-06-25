
package org.iflytek.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rag.prompt")
public class RagPromptConfig {

    /**
     * 系统提示词
     */
    private String system;

    /**
     * 用户提示词
     */
    private String user;
}