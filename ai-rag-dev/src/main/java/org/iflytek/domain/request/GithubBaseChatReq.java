package org.iflytek.domain.request;

import lombok.Data;

/**
 * @author hxdu5
 * @since 2025/6/29 16:30
 */
@Data
public class GithubBaseChatReq {
    private UserBaseReq userBaseReq;
    private ChatInfoReq chatInfoReq;
    private String ragTag;
}
