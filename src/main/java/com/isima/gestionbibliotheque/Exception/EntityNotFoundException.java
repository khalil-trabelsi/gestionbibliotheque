package com.isima.gestionbibliotheque.Exception;

import lombok.Getter;

import java.util.List;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private ErrorCode errorCode;
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    public EntityNotFoundException(String message, ErrorCode errorCode, List<String> errors) {
        super(message);
        this.errorCode = errorCode;
    }

}
