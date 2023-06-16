package com.capitole.price.exception;

import com.capitole.price.model.dto.ErrorDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExceptionManagerTest {
    private final ExceptionManager exceptionManager = new ExceptionManager();

    @Test
    void handleMissingServletRequestParameterException_ReturnsBadRequest() {
        // Given
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("param", "String");

        // When
        ResponseEntity<?> responseEntity = exceptionManager.handleMissingServletRequestParameterException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorDto errorDto = (ErrorDto) responseEntity.getBody();
        assertEquals("E100", errorDto.getCode());
        assertEquals(exception.getMessage(), errorDto.getMessage());
    }

    @Test
    void handleBusinessException_ReturnsCustomResponse() {
        // Given
        BusinessException exception = new BusinessException("E200", HttpStatus.NOT_FOUND, "Custom message");

        // When
        ResponseEntity<ErrorDto> responseEntity = exceptionManager.handleBusinessException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ErrorDto errorDto = responseEntity.getBody();
        assertEquals("E200", errorDto.getCode());
        assertEquals("Custom message", errorDto.getMessage());
    }

    @Test
    void handleDateTimeParseException_ReturnsBadRequest() {
        // Given
        DateTimeParseException exception = new DateTimeParseException("Invalid date", "2022-13-45", 0);

        // When
        ResponseEntity<ErrorDto> responseEntity = exceptionManager.handleDateTimeParseException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorDto errorDto = responseEntity.getBody();
        assertEquals("E300", errorDto.getCode());
        assertEquals(exception.getMessage(), errorDto.getMessage());
    }

    @Test
    void handleNumberFormatException_ReturnsBadRequest() {
        // Given
        NumberFormatException exception = new NumberFormatException("Invalid number");

        // When
        ResponseEntity<ErrorDto> responseEntity = exceptionManager.handleNumberFormatException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorDto errorDto = responseEntity.getBody();
        assertEquals("E400", errorDto.getCode());
        assertEquals(exception.getMessage(), errorDto.getMessage());
    }
}
