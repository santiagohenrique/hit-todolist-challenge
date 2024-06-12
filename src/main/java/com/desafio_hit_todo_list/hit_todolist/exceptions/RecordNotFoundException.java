package com.desafio_hit_todo_list.hit_todolist.exceptions;

public class RecordNotFoundException extends RuntimeException{

    private static final long SerialVersionUID = 1L;

    public RecordNotFoundException(String msg){
        super(msg);
    }
    
}
