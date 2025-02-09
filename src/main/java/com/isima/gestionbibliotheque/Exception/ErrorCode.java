package com.isima.gestionbibliotheque.Exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_BOOK_NOT_FOUND(1000),
    USER_BOOK_NOT_VALID(1001),
    USER_NOT_FOUND(2000),
    COLLECTION_NOT_FOUND(3000);
    // To do: complete the rest of the Error Codes

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }


}
