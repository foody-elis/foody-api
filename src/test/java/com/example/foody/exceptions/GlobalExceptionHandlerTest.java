package com.example.foody.exceptions;

import com.example.foody.utils.enums.CustomHttpStatus;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private ServletWebRequest servletWebRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        when(servletWebRequest.getRequest()).thenReturn(mock(jakarta.servlet.http.HttpServletRequest.class));
    }

    @Test
    void handleValidationExceptionWhenErrorsPresentReturnsBadRequest() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        FieldError fieldError1 = new FieldError("objectName", "fieldName", "defaultMessage1");
        FieldError fieldError2 = new FieldError("objectName", "fieldName", "defaultMessage2");
        ObjectError objectError = new ObjectError("objectName", "objectErrorMessage");

        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2, objectError));

        // Act
        ResponseEntity<ErrorDTO> response = exceptionHandler.handleValidationException(exception, servletWebRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // Check if the message is a map
        assertTrue(response.getBody().getMessage() instanceof Map);

        Map<String, Object> errorMap = (Map<String, Object>) response.getBody().getMessage();
        assertTrue(errorMap.containsKey("fieldName"));
        assertTrue(errorMap.containsKey("objectName"));

        Object fieldNameErrors = errorMap.get("fieldName");
        assertTrue(fieldNameErrors instanceof List);
        List<String> errorMessages = (List<String>) fieldNameErrors;
        assertTrue(errorMessages.contains("defaultMessage1"));
        assertTrue(errorMessages.contains("defaultMessage2"));

        assertEquals("objectErrorMessage", errorMap.get("objectName"));
    }

    @Test
    void handleConstraintViolationExceptionReturnsBadRequest() {
        // Arrange
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(exception.getConstraintViolations()).thenReturn(Collections.singleton(violation));
        when(violation.getMessage()).thenReturn("Constraint violation message");

        // Act
        ResponseEntity<ErrorDTO> response = exceptionHandler.handleConstraintViolationException(exception, servletWebRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Constraint violation message", response.getBody().getMessage());
    }

    @Test
    void handleInvalidFormatExceptionReturnsBadRequest() {
        // Arrange
        InvalidFormatException exception = mock(InvalidFormatException.class);
        when(exception.getValue()).thenReturn("invalidValue");
        when(exception.getPath()).thenReturn(Collections.singletonList(new InvalidFormatException.Reference(mock(Object.class), "fieldName")));

        // Act
        ResponseEntity<ErrorDTO> response = exceptionHandler.handleInvalidFormatException(exception, servletWebRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().toString().contains("invalidValue"));
    }

    @Test
    void handleBadRequestExceptionReturnsBadRequest() {
        // Arrange
        RuntimeException exception = new RuntimeException("Bad request message");

        // Act
        ResponseEntity<ErrorDTO> response = exceptionHandler.handleBadRequestException(exception, servletWebRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bad request message", response.getBody().getMessage());
    }

    @Test
    void handleUnauthorizedExceptionReturnsUnauthorized() {
        // Arrange
        RuntimeException exception = new RuntimeException("Unauthorized message");

        // Act
        ResponseEntity<ErrorDTO> response = exceptionHandler.handleUnauthorizedException(exception, servletWebRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unauthorized message", response.getBody().getMessage());
    }

    @Test
    void handleForbiddenExceptionReturnsForbidden() {
        // Arrange
        RuntimeException exception = new RuntimeException("Forbidden message");

        // Act
        ResponseEntity<ErrorDTO> response = exceptionHandler.handleForbiddenException(exception, servletWebRequest);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Forbidden message", response.getBody().getMessage());
    }

    @Test
    void handleNotFoundExceptionReturnsNotFound() {
        // Arrange
        RuntimeException exception = new RuntimeException("Not found message");

        // Act
        ResponseEntity<ErrorDTO> response = exceptionHandler.handleNotFoundException(exception, servletWebRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Not found message", response.getBody().getMessage());
    }

    @Test
    void handleConflictExceptionReturnsConflict() {
        // Arrange
        RuntimeException exception = new RuntimeException("Conflict message");

        // Act
        ResponseEntity<ErrorDTO> response = exceptionHandler.handleConflictException(exception, servletWebRequest);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Conflict message", response.getBody().getMessage());
    }

    @Test
    void handleInvalidTokenExceptionReturnsInvalidToken() {
        // Arrange
        RuntimeException exception = new RuntimeException("Invalid token message");

        // Act
        ResponseEntity<ErrorDTO> response = exceptionHandler.handleInvalidTokenException(exception, servletWebRequest);

        // Assert
        assertEquals(CustomHttpStatus.INVALID_TOKEN.getValue(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Invalid token message", response.getBody().getMessage());
    }

    @Test
    void handleBadGatewayExceptionReturnsBadGateway() {
        // Arrange
        RuntimeException exception = new RuntimeException("Bad gateway message");

        // Act
        ResponseEntity<ErrorDTO> response = exceptionHandler.handleBadGatewayException(exception, servletWebRequest);

        // Assert
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bad gateway message", response.getBody().getMessage());
    }
}