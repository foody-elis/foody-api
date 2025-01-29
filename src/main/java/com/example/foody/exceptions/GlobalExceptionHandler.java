package com.example.foody.exceptions;

import com.example.foody.exceptions.auth.InvalidCredentialsException;
import com.example.foody.exceptions.booking.*;
import com.example.foody.exceptions.email.EmailSendingException;
import com.example.foody.exceptions.entity.*;
import com.example.foody.exceptions.firebase.FirebaseCustomTokenCreationException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileDeleteException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileUploadException;
import com.example.foody.exceptions.order.ForbiddenOrderAccessException;
import com.example.foody.exceptions.order.InvalidOrderStateException;
import com.example.foody.exceptions.order.OrderNotAllowedException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.restaurant.RestaurateurAlreadyHasRestaurantException;
import com.example.foody.exceptions.review.ForbiddenReviewAccessException;
import com.example.foody.exceptions.review.ReviewNotAllowedException;
import com.example.foody.exceptions.sitting_time.InvalidWeekDayException;
import com.example.foody.exceptions.sitting_time.SittingTimeOverlappingException;
import com.example.foody.exceptions.user.InvalidPasswordException;
import com.example.foody.exceptions.user.UserNotActiveException;
import com.example.foody.utils.enums.CustomHttpStatus;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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

/**
 * Global exception handler for the application.
 * <p>
 * This class handles various exceptions and maps them to appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link MethodArgumentNotValidException} and returns a {@link ResponseEntity} with a BAD_REQUEST status.
     *
     * @param exception  the {@link MethodArgumentNotValidException} thrown
     * @param webRequest the current web request
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with details of the validation error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(
            MethodArgumentNotValidException exception,
            WebRequest webRequest
    ) {
        Map<String, Object> errorMap = collectErrors(exception);
        ErrorDTO errorDTO = buildErrorDTO(
                HttpStatus.BAD_REQUEST,
                errorMap,
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );
        return ResponseEntity.badRequest().body(errorDTO);
    }

    /**
     * Handles {@link ConstraintViolationException} and returns a {@link ResponseEntity} with a BAD_REQUEST status.
     *
     * @param exception  the {@link ConstraintViolationException} thrown
     * @param webRequest the current web request
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with details of the constraint violation
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolationException(
            ConstraintViolationException exception,
            WebRequest webRequest
    ) {
        String message = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Constraint violation");
        ErrorDTO errorDTO = buildErrorDTO(
                HttpStatus.BAD_REQUEST,
                message,
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );
        return ResponseEntity.badRequest().body(errorDTO);
    }

    /**
     * Handles {@link InvalidFormatException} and returns a {@link ResponseEntity} with a BAD_REQUEST status.
     * <p>
     * {@link InvalidFormatException} is thrown by Jackson when it cannot deserialize a value to a specific type (e.g. @JsonFormat is violated).
     *
     * @param exception  the {@link InvalidFormatException} thrown
     * @param webRequest the current web request
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with details of the invalid format
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorDTO> handleInvalidFormatException(
            InvalidFormatException exception,
            WebRequest webRequest
    ) {
        String message = String.format(
                "Invalid value '%s' for %s field.",
                exception.getValue(),
                exception.getPath().getFirst().getFieldName()
        );
        ErrorDTO errorDTO = buildErrorDTO(
                HttpStatus.BAD_REQUEST,
                message,
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );
        return ResponseEntity.badRequest().body(errorDTO);
    }

    /**
     * Handles various exceptions related to bad requests and returns a {@link ResponseEntity} with a BAD_REQUEST status.
     *
     * @param exception  the {@link RuntimeException} thrown
     * @param webRequest the current web request
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with details of the bad request error
     */
    @ExceptionHandler({
            InvalidWeekDayException.class,
            SittingTimeOverlappingException.class,
            BookingNotAllowedException.class,
            InvalidBookingWeekDayException.class,
            InvalidBookingRestaurantException.class,
            InvalidBookingStateException.class,
            InvalidBookingSittingTimeException.class,
            OrderNotAllowedException.class,
            InvalidOrderStateException.class,
            ReviewNotAllowedException.class
    })
    public ResponseEntity<ErrorDTO> handleBadRequestException(
            RuntimeException exception,
            WebRequest webRequest
    ) {
        ErrorDTO errorDTO = buildErrorDTO(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );
        return ResponseEntity.badRequest().body(errorDTO);
    }

    /**
     * Handles unauthorized exceptions and returns a {@link ResponseEntity} with an UNAUTHORIZED status.
     *
     * @param exception  the {@link RuntimeException} thrown
     * @param webRequest the current web request
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with details of the unauthorized error
     */
    @ExceptionHandler({
            InvalidCredentialsException.class,
            InvalidPasswordException.class
    })
    public ResponseEntity<ErrorDTO> handleUnauthorizedException(
            RuntimeException exception,
            WebRequest webRequest
    ) {
        ErrorDTO errorDTO = buildErrorDTO(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }

    /**
     * Handles forbidden exceptions and returns a {@link ResponseEntity} with a FORBIDDEN status.
     *
     * @param exception  the {@link RuntimeException} thrown
     * @param webRequest the current web request
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with details of the forbidden error
     */
    @ExceptionHandler({
            AccessDeniedException.class,
            AuthenticationException.class,
            UserNotActiveException.class,
            ForbiddenBookingAccessException.class,
            ForbiddenRestaurantAccessException.class,
            ForbiddenOrderAccessException.class,
            ForbiddenReviewAccessException.class
    })
    public ResponseEntity<ErrorDTO> handleForbiddenException(
            RuntimeException exception,
            WebRequest webRequest
    ) {
        ErrorDTO errorDTO = buildErrorDTO(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
    }

    /**
     * Handles not found exceptions and returns a {@link ResponseEntity} with a NOT_FOUND status.
     *
     * @param exception  the {@link RuntimeException} thrown
     * @param webRequest the current web request
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with details of the not found error
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(
            RuntimeException exception,
            WebRequest webRequest
    ) {
        ErrorDTO errorDTO = buildErrorDTO(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    /**
     * Handles conflict exceptions and returns a {@link ResponseEntity} with a CONFLICT status.
     *
     * @param exception  the {@link RuntimeException} thrown
     * @param webRequest the current web request
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with details of the conflict error
     */
    @ExceptionHandler({
            EntityDuplicateException.class,
            RestaurateurAlreadyHasRestaurantException.class,
            DuplicateActiveFutureBookingException.class
    })
    public ResponseEntity<ErrorDTO> handleConflictException(
            RuntimeException exception,
            WebRequest webRequest
    ) {
        ErrorDTO errorDTO = buildErrorDTO(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
    }

    /**
     * Handles internal server error exceptions and returns a {@link ResponseEntity} with an INTERNAL_SERVER_ERROR status.
     *
     * @param exception  the {@link RuntimeException} thrown
     * @param webRequest the current web request
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with details of the internal server error
     */
    @ExceptionHandler({
            MalformedJwtException.class,
            ExpiredJwtException.class,
            UnsupportedJwtException.class,
            SignatureException.class
    })
    public ResponseEntity<ErrorDTO> handleInvalidTokenException(
            RuntimeException exception,
            WebRequest webRequest
    ) {
        ErrorDTO errorDTO = buildErrorDTO(
                CustomHttpStatus.INVALID_TOKEN,
                exception.getMessage(),
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );
        return ResponseEntity.status(CustomHttpStatus.INVALID_TOKEN.getValue()).body(errorDTO);
    }

    /**
     * Handles bad gateway exceptions and returns a {@link ResponseEntity} with a BAD_GATEWAY status.
     *
     * @param exception  the {@link RuntimeException} thrown
     * @param webRequest the current web request
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with details of the bad gateway error
     */
    @ExceptionHandler({
            EntityCreationException.class,
            EntityEditException.class,
            EntityDeletionException.class,
            GoogleDriveFileUploadException.class,
            GoogleDriveFileDeleteException.class,
            EmailSendingException.class,
            FirebaseCustomTokenCreationException.class
    })
    public ResponseEntity<ErrorDTO> handleBadGatewayException(
            RuntimeException exception,
            WebRequest webRequest
    ) {
        ErrorDTO errorDTO = buildErrorDTO(
                HttpStatus.BAD_GATEWAY,
                exception.getMessage(),
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorDTO);
    }

    /**
     * Collects validation errors from a {@link MethodArgumentNotValidException} and returns a map with the object name as key and the error message(s) as value.
     * <p>
     * It manages:
     * <li>single error messages, {objectName: errorMessage}</li>
     * <li>multiple error messages, {objectName: [errorMessage1, errorMessage2, ...]}</li>
     *
     * @param exception the {@link MethodArgumentNotValidException} containing validation errors
     * @return a {@link Map} where the keys are the field names or object names and the values are the corresponding error messages
     */
    private Map<String, Object> collectErrors(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        this::getErrorKey,
                        this::getErrorMessage,
                        this::mergeErrors
                ));
    }

    /**
     * Retrieves the key for the given {@link ObjectError}.
     * <p>
     * If the error is a {@link FieldError}, the key will be the field name.
     * <p>
     * Otherwise, the key will be the object name.
     *
     * @param error the {@link ObjectError} to retrieve the key from
     * @return the key as a {@link String}, which is either the field name or the object name
     */
    private String getErrorKey(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField();
        } else {
            return error.getObjectName();
        }
    }

    /**
     * Retrieves the error message for the given {@link ObjectError}.
     * <p>
     * If the error has a default message, it will be returned.
     * <p>
     * Otherwise, "unknown error" will be returned.
     *
     * @param error the {@link ObjectError} to retrieve the error message from
     * @return the error message as a {@link String}
     */
    private String getErrorMessage(ObjectError error) {
        String defaultMessage = error.getDefaultMessage();
        return defaultMessage != null ? defaultMessage : "unknown error";
    }

    /**
     * Merges two error messages into a single {@link List} of error messages.
     * <p>
     * If the existing error is a {@link String}, it will be converted to a {@link List} containing the existing error and the replacement error.
     * <p>
     * If the existing error is a {@link List}, the replacement error will be added to the existing list.
     *
     * @param existing    the existing error message
     * @param replacement the replacement error message
     * @return the merged error message
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

    /**
     * Builds an {@link ErrorDTO} object with the given HTTP status, message, and request path.
     *
     * @param httpStatus the {@link HttpStatus} to set in the {@link ErrorDTO}
     * @param message    the error message or details to include in the {@link ErrorDTO}
     * @param path       the request URI path where the error occurred
     * @return an {@link ErrorDTO} containing the provided details
     */
    private ErrorDTO buildErrorDTO(HttpStatus httpStatus, Object message, String path) {
        return new ErrorDTO(
                LocalDateTime.now(), httpStatus.value(),
                httpStatus.getReasonPhrase(), message, path
        );
    }

    /**
     * Builds an {@link ErrorDTO} object with the given custom HTTP status, message, and request path.
     *
     * @param httpStatus the {@link CustomHttpStatus} to set in the {@link ErrorDTO}
     * @param message    the error message or details to include in the {@link ErrorDTO}
     * @param path       the request URI path where the error occurred
     * @return an {@link ErrorDTO} containing the provided details
     */
    private ErrorDTO buildErrorDTO(CustomHttpStatus httpStatus, Object message, String path) {
        return new ErrorDTO(
                LocalDateTime.now(), httpStatus.getValue(),
                httpStatus.getReason(), message, path
        );
    }
}