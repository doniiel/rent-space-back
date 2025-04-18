package com.renstapce.review.exception;

import com.rentspace.core.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        StringBuilder errorMessage = new StringBuilder("Validation failed for arguments: ");

        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        for (ObjectError error : errorList) {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append(fieldName).append(" (").append(message).append("), ");
        }

        if (!errorMessage.isEmpty()) {
            errorMessage.setLength(errorMessage.length() - 2);
        }

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                BAD_REQUEST.value(),
                errorMessage.toString(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    public ResponseEntity<ErrorResponseDto> handleReviewNotFoundException(ReviewNotFoundException ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    public ResponseEntity<ErrorResponseDto> handleGlobalException(WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                BAD_REQUEST.value(),
                "An unexpected error occurred.",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }
}
