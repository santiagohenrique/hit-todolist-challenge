package com.desafio_hit_todo_list.hit_todolist.task.dto;

import java.util.List;

import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;

public record TaskPageDTO(List<Task> tasks, long totalElements, int totalPages) {
    
}
