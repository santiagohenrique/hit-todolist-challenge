package com.desafio_hit_todo_list.hit_todolist.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationExceptionMessage {
    

    private HttpStatus status;
    private String message;


}
