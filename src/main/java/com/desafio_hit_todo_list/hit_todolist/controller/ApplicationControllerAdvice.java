package com.desafio_hit_todo_list.hit_todolist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.desafio_hit_todo_list.hit_todolist.exceptions.ApplicationExceptionMessage;
import com.desafio_hit_todo_list.hit_todolist.exceptions.RecordNotFoundException;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ApplicationExceptionMessage> handleNotFoundException(RecordNotFoundException e){
        ApplicationExceptionMessage exceptionResponse = new ApplicationExceptionMessage(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }
    
}
