package com.desafio_hit_todo_list.hit_todolist.task.dto;

import com.desafio_hit_todo_list.hit_todolist.task.entity.TaskStatus;

public record TaskDTO(

    String title, 
    String description, 
    TaskStatus status, 
    Long priority) {

}
