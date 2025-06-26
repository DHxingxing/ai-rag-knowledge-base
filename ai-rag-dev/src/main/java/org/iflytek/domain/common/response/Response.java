package org.iflytek.domain.common.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hxdu5
 * @date 2025/6/25 23:25
 */
@Data

@Builder
public class Response<T> implements Serializable {

    private String code;

    private String msg;

    private T data;  // 只包含业务数据

    public Response(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response(T data) {
        this("200","success",data);
    }


    public static <T> Response<T> success(T data) {
        return new Response<>("200", "success", data);
    }

    public static <T> Response<T> success(String msg) {
        return new Response<>("200", msg, null);
    }

    public static <T> Response<T> error(String code, String msg) {
        return new Response<>(code, msg, null);
    }

    public static <T> Response<T> error(String msg) {
        return new Response<>("500", msg, null);
    }
}