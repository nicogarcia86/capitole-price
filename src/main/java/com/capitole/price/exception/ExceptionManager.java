package com.capitole.price.exception;

import com.capitole.price.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {

        ErrorDto errorDto = ErrorDto.builder()
                .code("E100")
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleBusinessException(BusinessException exception){

        ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto,exception.getStatus());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorDto> handleDateTimeParseException(DateTimeParseException exception){

        ErrorDto errorDto = ErrorDto.builder()
                .code("E300")
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorDto> handleNumberFormatException(NumberFormatException exception){

        ErrorDto errorDto = ErrorDto.builder()
                .code("E400")
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
