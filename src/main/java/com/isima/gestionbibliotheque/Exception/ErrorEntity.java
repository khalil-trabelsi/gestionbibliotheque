package com.isima.gestionbibliotheque.Exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
