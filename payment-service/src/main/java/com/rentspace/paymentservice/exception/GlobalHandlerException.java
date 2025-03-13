package com.rentspace.paymentservice.exception;

import com.rentspace.core.dto.ErrorResponseDto;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalHandlerException extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        StringBuilder errormessage = new StringBuilder("Validation failed for arguments: ");

        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        for (ObjectError error: errorList) {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errormessage.append(fieldName).append(" (").append(message).append("), ");
        }

        if (!errormessage.isEmpty()) {
            errormessage.setLength(errormessage.length() - 2);
        }

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                BAD_REQUEST.value(),
                errormessage.toString(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePaymentNotFoundException(PaymentNotFoundException ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }
}
