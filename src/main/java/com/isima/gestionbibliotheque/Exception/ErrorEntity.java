package com.isima.gestionbibliotheque.Exception;

import lombok.Builder;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorEntity(
        LocalDateTime timeStamp,
        String message,
        List<String> errors,
        @Transient
        String errorAuthor,
        int httpStatus,
        ErrorCode errorCode
) {
}
