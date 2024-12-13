package com.example.foody.exceptions;

import com.example.foody.exceptions.auth.InvalidCredentialsException;
import com.example.foody.exceptions.booking.*;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileUploadException;
import com.example.foody.exceptions.order.ForbiddenOrderAccessException;
import com.example.foody.exceptions.order.InvalidOrderStateException;
import com.example.foody.exceptions.order.OrderNotAllowedException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.restaurant.RestaurateurAlreadyHasRestaurantException;
import com.example.foody.exceptions.sitting_time.InvalidWeekDayException;
import com.example.foody.exceptions.sitting_time.SittingTimeOverlappingException;
import com.example.foody.exceptions.user.UserNotActiveException;
import com.example.foody.utils.enums.CustomHttpStatus;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        Map<String, Object> errorMap = collectErrors(exception);
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.BAD_REQUEST, errorMap, ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    // InvalidFormatException is thrown by Jackson when it cannot deserialize a value to a specific type (@JsonFormat pattern is not respected)
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorDTO> handleInvalidFormatException(InvalidFormatException exception, WebRequest webRequest) {
        String message = String.format("Invalid value '%s' for %s field.", exception.getValue(), exception.getPath().getFirst().getFieldName());
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.BAD_REQUEST, message, ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            InvalidWeekDayException.class,
            SittingTimeOverlappingException.class,
            BookingNotAllowedException.class,
            InvalidBookingWeekDayException.class,
            InvalidBookingRestaurantException.class,
            InvalidBookingStateException.class,
            InvalidBookingSittingTimeException.class,
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
            DuplicateActiveFutureBookingException.class
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

    @ExceptionHandler({
            EntityCreationException.class,
            GoogleDriveFileUploadException.class
    })
    public ResponseEntity<ErrorDTO> handleBadGatewayException(RuntimeException exception, WebRequest webRequest) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.BAD_GATEWAY, exception.getMessage(), ((ServletWebRequest)webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_GATEWAY);
    }

    /*
    Collects validation errors from a MethodArgumentNotValidException and returns a map with the object name as key and the error message/s as value.
    It manages:
    - single error messages, {objectName: errorMessage}
    - multiple error messages, {objectName: [errorMessage1, errorMessage2, ...]}
     */
    private Map<String, Object> collectErrors(MethodArgumentNotValidException exception) {
        // getAllErrors() returns all errors, both global and field errors
        return exception.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        this::getErrorKey,
                        this::getErrorMessage,
                        this::mergeErrors // Merge function to handle conflicts
                ));
    }

    // Returns the key for the error map, it is the field name if the error is a FieldError, otherwise it is the object name (if the error is a ObjectError)
    private String getErrorKey(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField();
        } else {
            return error.getObjectName();
        }
    }

    // Returns the error message from the ObjectError, it manage getDefaultMessage() returning null
    private String getErrorMessage(ObjectError error) {
        String defaultMessage = error.getDefaultMessage();
        return defaultMessage != null ? defaultMessage : "unknown error";
    }

    /*
    Handles conflicts when there are several errors for the same object when collecting validation errors.
    Returns the new value associated with the specified key.
     */
    private Object mergeErrors(Object existing, Object replacement) {
        if (existing instanceof String) {
            List<String> errorList = new ArrayList<>();
            errorList.add((String) existing);
            errorList.add((String) replacement);
            return errorList;
        } else if (existing instanceof List) {
            List<String> errorList = (List<String>) existing;
            errorList.add((String) replacement);
            return existing;
        }
        return replacement;
    }

    private ErrorDTO buildErrorDTO(HttpStatus httpStatus, Object message, String path) {
        return new ErrorDTO(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message, path);
    }

    private ErrorDTO buildErrorDTO(CustomHttpStatus httpStatus, Object message, String path) {
        return new ErrorDTO(LocalDateTime.now(), httpStatus.getValue(), httpStatus.getReason(), message, path);
    }
}