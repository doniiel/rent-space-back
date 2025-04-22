package com.rentspace.bookingservice.exception;

import com.rentspace.core.dto.ErrorResponseDto;
import com.rentspace.core.exception.ListingNotAvailableException;
import feign.FeignException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class GlobalHandlerException extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                BAD_REQUEST.value(),
                errors.toString(),
                LocalDateTime.now()
        );

        log.error("Validation failed for request {}: {}", request.getDescription(false), errors);
        return new ResponseEntity<>(errorResponseDto, BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponseDto> handleFeignException(FeignException ex, WebRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;

        if (ex.status() == 404) {
            httpStatus = NOT_FOUND;
        }

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                httpStatus.value(),
                "External service error: " + ex.getMessage(),
                LocalDateTime.now()
        );

        log.error("FeignException for request {}: {}", request.getDescription(false), ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponseDto, httpStatus);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(BookingNotFoundException ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        log.error("BookingNotFoundException for request {}: {}", request.getDescription(false), ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(BookingAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleAlreadyExists(BookingAlreadyExistsException ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        log.error("BookingAlreadyExistsException for request {}: {}", request.getDescription(false), ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, CONFLICT);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponseDto> handleExpired(TokenExpiredException ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                UNAUTHORIZED.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        log.error("TokenExpiredException for request {}: {}", request.getDescription(false), ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(IllegalArgumentException ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                BAD_REQUEST.value(),
                "Invalid status value. Allowed values: PENDING, SUCCESS, FAILED",
                LocalDateTime.now()
        );

        log.error("IllegalArgumentException for request {}: {}", request.getDescription(false), ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(ListingNotAvailableException.class)
    public ResponseEntity<ErrorResponseDto> handleListingNotAvailable(ListingNotAvailableException ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        log.error("ListingNotAvailableException for request {}: {}", request.getDescription(false), ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                FORBIDDEN.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        log.error("AccessDeniedException for request {}: {}", request.getDescription(false), ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponseDto, FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                request.getDescription(false).replace("uri=", ""),
                BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        log.error("Unexpected exception for request {}: {}", request.getDescription(false), ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }
}
