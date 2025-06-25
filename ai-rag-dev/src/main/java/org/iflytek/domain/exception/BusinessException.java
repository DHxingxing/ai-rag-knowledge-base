package org.iflytek.domain.exception;

import org.iflytek.domain.common.base.BaseCodeException;
import org.iflytek.domain.common.error.IError;

public class BusinessException extends BaseCodeException {

    public BusinessException(IError iError) {
        super(iError);
    }

    public BusinessException(IError iError, String message) {
        super(iError, message);
    }
}
