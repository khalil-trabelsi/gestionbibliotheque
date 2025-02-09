package com.isima.gestionbibliotheque.Exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class BadRequestException extends RuntimeException {
    private ErrorCode errorCode;
    private Map<String, String> errors;

    public  BadRequestException(String message) {
        super(message);
    }

    public  BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public  BadRequestException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public  BadRequestException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public  BadRequestException(String message, ErrorCode errorCode, Map<String, String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }

}
