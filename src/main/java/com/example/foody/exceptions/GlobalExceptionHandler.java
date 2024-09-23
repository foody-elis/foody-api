package com.example.foody.exceptions;

import com.example.foody.utils.CustomHttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

public class GlobalExceptionHandler {
    @ExceptionHandler({
            AccessDeniedException.class,
            AuthenticationException.class
    })
    public ResponseEntity<ErrorDTO> handleForbiddenException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.FORBIDDEN, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    private ErrorDTO buildErrorDTO(HttpStatus httpStatus, Object message, String path) {
        return new ErrorDTO(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message, path);
    }

    private ErrorDTO buildErrorDTO(CustomHttpStatus httpStatus, Object message, String path) {
        return new ErrorDTO(LocalDateTime.now(), httpStatus.getValue(), httpStatus.getReason(), message, path);
    }
}
