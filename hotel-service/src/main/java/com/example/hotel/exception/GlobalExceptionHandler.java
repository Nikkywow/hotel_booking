package com.example.hotel.exception;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorResponse> onNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(Instant.now(), 404, e.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    ResponseEntity<ErrorResponse> onConflict(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(Instant.now(), 409, e.getMessage()));
    }
}
