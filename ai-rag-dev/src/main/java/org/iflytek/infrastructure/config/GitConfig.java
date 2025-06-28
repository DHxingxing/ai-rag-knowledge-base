package org.iflytek.infrastructure.config;

import lombok.Builder;
import lombok.Data;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.iflytek.domain.request.UserBaseReq;

import java.io.File;

/**
 * @author hxdu5
 * @since 2025/6/28 21:51
 */

@Builder
@Data
public class GitConfig {

    private String url;

    private File localPath;

    private String username;

    private String token;

    private UsernamePasswordCredentialsProvider credentialsProvider;

    public static GitConfig from(UserBaseReq userBaseReq) {
        return GitConfig.builder()
                .url(userBaseReq.getUrl())
                .localPath(new File(userBaseReq.getLocalPath()))
                .credentialsProvider(new UsernamePasswordCredentialsProvider(userBaseReq.getUserName(),userBaseReq.getToken()))
                .build();
    }

}
