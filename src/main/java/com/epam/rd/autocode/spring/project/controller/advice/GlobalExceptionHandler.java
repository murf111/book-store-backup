package com.epam.rd.autocode.spring.project.controller.advice;

import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "NOT_FOUND", "message", e.getMessage()));
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Object> handleAlreadyExist(AlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(Map.of("error", "CONFLICT", "message", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = new HashMap<>();

        e.getBindingResult()
         .getFieldErrors()
         .forEach(fieldError -> fieldErrors.put(fieldError.getField(),
                                                fieldError.getDefaultMessage()));
        return ResponseEntity.badRequest()
                             .body(Map.of("error", "VALIDATION_FAILED",
                                          "details", fieldErrors));
    }
}
