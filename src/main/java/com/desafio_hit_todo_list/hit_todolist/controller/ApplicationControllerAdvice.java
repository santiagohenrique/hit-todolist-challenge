package com.desafio_hit_todo_list.hit_todolist.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.desafio_hit_todo_list.hit_todolist.exceptions.ApplicationExceptionMessage;
import com.desafio_hit_todo_list.hit_todolist.exceptions.RecordNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    private List<String> errors;

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ApplicationExceptionMessage> handleNotFoundException(RecordNotFoundException e){
        ApplicationExceptionMessage exceptionResponse = new ApplicationExceptionMessage(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationExceptionMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        errors = e.getBindingResult().getFieldErrors()
                                .stream()
                                .map(error -> "[" + error.getField() + "] " + error.getDefaultMessage())
                                .collect(Collectors.toList());
        ApplicationExceptionMessage exceptionResponse = new ApplicationExceptionMessage(HttpStatus.BAD_REQUEST, "Validation failed", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse); 
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApplicationExceptionMessage> handleConstraintViolationException(ConstraintViolationException e) {
        errors = e.getConstraintViolations()
                .stream()
                .map(violation -> {
                    String fieldName = violation.getPropertyPath().toString();
                    fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
                    return "[" + fieldName + "] " + violation.getMessage();
                })
                .collect(Collectors.toList());
        ApplicationExceptionMessage exceptionResponse = new ApplicationExceptionMessage(HttpStatus.BAD_REQUEST, "Constraint violation", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApplicationExceptionMessage> handleValidationException(HttpMessageNotReadableException exception) {
        String errorDetails = "";

        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ifx = (InvalidFormatException) exception.getCause();
            if (ifx.getTargetType()!=null && ifx.getTargetType().isEnum()) {
                errorDetails = String.format("Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                        ifx.getValue(), ifx.getPath().get(ifx.getPath().size()-1).getFieldName(), Arrays.toString(ifx.getTargetType().getEnumConstants()));
            }
        }
        ApplicationExceptionMessage errorMessage = new ApplicationExceptionMessage(HttpStatus.BAD_REQUEST, errorDetails);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApplicationExceptionMessage> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String errorDetails = String.format("Failed to convert value of type '%s' to required type '%s' for parameter '%s'.",
                e.getValue().getClass().getSimpleName(), e.getRequiredType().getSimpleName(), e.getName());
        if (e.getRequiredType().isEnum()) {
            String enumValues = Arrays.stream(e.getRequiredType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            errorDetails += String.format(" Possible values are: %s.", enumValues);
        }
        ApplicationExceptionMessage errorMessage = new ApplicationExceptionMessage(HttpStatus.BAD_REQUEST, errorDetails);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

}
