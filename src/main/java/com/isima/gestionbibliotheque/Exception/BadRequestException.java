package com.isima.gestionbibliotheque.Exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException {
    private ErrorCode errorCode;
    private List<String> errors;

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
    public  BadRequestException(String message, ErrorCode errorCode, List<String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }

}
