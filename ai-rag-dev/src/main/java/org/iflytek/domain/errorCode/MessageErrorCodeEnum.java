package org.iflytek.domain.errorCode;

import org.iflytek.domain.common.error.IError;

public enum MessageErrorCodeEnum implements IError {

    MESSAGE_IS_NOT_EMPTY("001", "消息不能为空");//这行代码其实就是在创建一个 MessageErrorCodeEnum 类型的对象，并传入构造方法参数 "001" 和 "消息不能为空"。

    private final String code;

    private String message;

    MessageErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getError() {
        return message;
    }

    @Override
    public void setError(String message) {
        this.message = message;
    }
}
