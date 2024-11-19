package com.example.foody.exceptions;

import com.example.foody.exceptions.auth.InvalidCredentialsException;
import com.example.foody.exceptions.booking.*;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.order.ForbiddenOrderAccessException;
import com.example.foody.exceptions.order.InvalidOrderStateException;
import com.example.foody.exceptions.order.OrderNotAllowedException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.restaurant.RestaurateurAlreadyHasRestaurantException;
import com.example.foody.exceptions.sitting_time.InvalidWeekDayException;
import com.example.foody.exceptions.sitting_time.SittingTimeOverlappingException;
import com.example.foody.exceptions.user.UserNotActiveException;
import com.example.foody.utils.enums.CustomHttpStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        Map<String, String> errorMap = new HashMap<>();

        // getFieldsErrors() returns all fields that have validation errors
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errorMap.put(error.getField(), error.getDefaultMessage())
        );

        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.BAD_REQUEST, errorMap, ((ServletWebRequest)webRequest).getRequest().getRequestURI());

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            InvalidWeekDayException.class,
            SittingTimeOverlappingException.class,
            BookingNotAllowedException.class,
            InvalidBookingWeekDayException.class,
            InvalidBookingRestaurantException.class,
            InvalidBookingStateException.class,
            OrderNotAllowedException.class,
            InvalidOrderStateException.class
    })
    public ResponseEntity<ErrorDTO> handleBadRequestException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.BAD_REQUEST, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDTO> handleUnauthorizedException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.UNAUTHORIZED, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            AuthenticationException.class,
            UserNotActiveException.class,
            ForbiddenBookingAccessException.class,
            ForbiddenRestaurantAccessException.class,
            ForbiddenOrderAccessException.class
    })
    public ResponseEntity<ErrorDTO> handleForbiddenException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.FORBIDDEN, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.NOT_FOUND, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            EntityDuplicateException.class,
            RestaurateurAlreadyHasRestaurantException.class,
    })
    public ResponseEntity<ErrorDTO> handleConflictException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.CONFLICT, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            MalformedJwtException.class,
            ExpiredJwtException.class,
            UnsupportedJwtException.class,
            SignatureException.class
    })
    public ResponseEntity<ErrorDTO> handleInvalidTokenException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(CustomHttpStatus.INVALID_TOKEN, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return ResponseEntity.status(CustomHttpStatus.INVALID_TOKEN.getValue()).body(errorDTO);
    }

    @ExceptionHandler(EntityCreationException.class)
    public ResponseEntity<ErrorDTO> handleBadGatewayException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.BAD_GATEWAY, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_GATEWAY);
    }

    private ErrorDTO buildErrorDTO(HttpStatus httpStatus, Object message, String path) {
        return new ErrorDTO(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message, path);
    }

    private ErrorDTO buildErrorDTO(CustomHttpStatus httpStatus, Object message, String path) {
        return new ErrorDTO(LocalDateTime.now(), httpStatus.getValue(), httpStatus.getReason(), message, path);
    }
}
