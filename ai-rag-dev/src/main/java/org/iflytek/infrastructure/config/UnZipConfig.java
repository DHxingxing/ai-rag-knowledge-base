package org.iflytek.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hxdu5
 * @since 2025/6/26 16:07
 */

@Data
@Component
@ConfigurationProperties(prefix = "file.unzip")
public class UnZipConfig {

    private String temp;

    private String local;

}
