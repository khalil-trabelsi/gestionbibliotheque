    package com.isima.gestionbibliotheque.Exception;

    import io.jsonwebtoken.ExpiredJwtException;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.AccessDeniedException;
    import org.springframework.security.authorization.AuthorizationDeniedException;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RestControllerAdvice;
    import org.springframework.web.servlet.NoHandlerFoundException;

    import java.time.LocalDateTime;
    import java.util.HashMap;
    import java.util.Map;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<ErrorEntity> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
            ErrorEntity error = ErrorEntity.builder()
                    .timeStamp(LocalDateTime.now())
                    .message(ex.getMessage())
                    .httpStatus(HttpStatus.FORBIDDEN.value())
                    .build();

            return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(error);
        }

        @ExceptionHandler(ExpiredJwtException.class)
        public ResponseEntity<ErrorEntity> handleExpiredJwtException(ExpiredJwtException ex) {
            ErrorEntity error = ErrorEntity.builder()
                    .timeStamp(LocalDateTime.now())
                    .message(ex.getMessage())
                    .httpStatus(HttpStatus.FORBIDDEN.value())
                    .build();

            return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(error);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorEntity> entityNotFoundHandler(EntityNotFoundException exception) {
            ErrorEntity error = ErrorEntity.builder()
                    .timeStamp(LocalDateTime.now())
                    .message(exception.getMessage())
                    .httpStatus(HttpStatus.NOT_FOUND.value())
                    .errorCode(exception.getErrorCode())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorEntity> badRequestHandler(BadRequestException exception) {
            ErrorEntity error = ErrorEntity.builder()
                    .timeStamp(LocalDateTime.now())
                    .message(exception.getMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errors(exception.getErrors())
                    .errorCode(exception.getErrorCode())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorEntity> accessDeniedHandler(AccessDeniedException exception) {
            ErrorEntity error = ErrorEntity.builder()
                    .timeStamp(LocalDateTime.now())
                    .message("Access Denied "+exception.getMessage())
                    .httpStatus(HttpStatus.FORBIDDEN.value())
                    .build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(error);
        }

        @ExceptionHandler(OperationNotPermittedException.class)
        public ResponseEntity<ErrorEntity> operationNotPermittedHandler(OperationNotPermittedException exception) {
            ErrorEntity error = ErrorEntity.builder()
                    .timeStamp(LocalDateTime.now())
                    .message("Invalid operation: "+exception.getMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
        }



        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorEntity> runtimeExceptionHandler(RuntimeException exception) {
            ErrorEntity error = ErrorEntity.builder()
                    .timeStamp(LocalDateTime.now())
                    .message(exception.getMessage())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(error);
        }

    }
