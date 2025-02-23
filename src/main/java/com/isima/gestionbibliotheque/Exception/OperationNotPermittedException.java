package com.isima.gestionbibliotheque.Exception;

public class OperationNotPermittedException extends RuntimeException {
    private ErrorCode errorCode;
    public OperationNotPermittedException(String message) {
        super(message);
    }

    public OperationNotPermittedException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public OperationNotPermittedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationNotPermittedException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
