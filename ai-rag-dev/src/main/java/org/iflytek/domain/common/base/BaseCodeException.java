package org.iflytek.domain.common.base;

import org.iflytek.domain.common.error.IError;
import org.iflytek.domain.common.error.impl.SimpleError;

public abstract class BaseCodeException extends RuntimeException {
    private static final long serialVersionUID = 4417708061574640252L;
    private String errorCode;
    private String errorMessage;

    public BaseCodeException(IError iError) {
        super(iError.getError());
        this.errorCode = iError.getCode();
        this.errorMessage = iError.getError();
    }

    public BaseCodeException(IError iError, String message) {
        super(message);
        this.errorCode = iError.getCode();
        this.errorMessage = message;
    }

    public IError getErrorCode() {
        return new SimpleError(this.errorCode, this.errorMessage);
    }
}
