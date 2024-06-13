package com.desafio_hit_todo_list.hit_todolist.exceptions;

import java.util.List;

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
    private List<String> errors;

    public ApplicationExceptionMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.errors = null;
    }


}
