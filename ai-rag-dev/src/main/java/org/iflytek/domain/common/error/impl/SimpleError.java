package org.iflytek.domain.common.error.impl;

import lombok.Data;
import org.iflytek.domain.common.error.IError;

@Data
public class SimpleError implements IError {
    private String code;
    private String error;

    public SimpleError(String code, String error) {
        this.code = code;
        this.error = error;
    }

    public String getCode() {
        return this.code;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
