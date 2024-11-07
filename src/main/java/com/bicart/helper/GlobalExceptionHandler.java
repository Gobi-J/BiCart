package com.bicart.helper;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * <p>
 *   Global exception handler class that handles exceptions thrown by the application.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgument(RuntimeException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .status("ERROR")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .status("ERROR")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {DuplicateKeyException.class})
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .code(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .status("ERROR")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getAllErrors().getFirst().getDefaultMessage())
                .status("ERROR")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UnAuthorizedException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUserServiceException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .status("ERROR")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    public ResponseEntity<ErrorResponse> handleForbiddenException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .code(HttpStatus.FORBIDDEN.value())
                .message(e.getMessage())
                .status("ERROR")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ErrorResponse> handleException(CustomException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .code(HttpStatus.CONFLICT.value())
                .message("Error occurred with the server\n" + e.getMessage())
                .status("ERROR")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}