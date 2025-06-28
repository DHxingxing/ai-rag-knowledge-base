package org.iflytek.domain.request;

import lombok.Data;

/**
 * @author hxdu5
 * @since 2025/6/28 21:59
 */
@Data
public class UserBaseReq {

    private String url;

    private String localPath;

    private String userName;

    private String token;

}
