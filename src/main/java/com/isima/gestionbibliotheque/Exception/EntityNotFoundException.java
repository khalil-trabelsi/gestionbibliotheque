package com.isima.gestionbibliotheque.Exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private ErrorCode errorCode;
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
