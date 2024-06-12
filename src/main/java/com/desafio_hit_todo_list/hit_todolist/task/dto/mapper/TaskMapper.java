package com.desafio_hit_todo_list.hit_todolist.task.dto.mapper;

import org.springframework.stereotype.Component;

import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskDTO;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;

@Component
public class TaskMapper {
    
    public TaskDTO toDTO(Task task){
        return new TaskDTO(task.getTitle(), task.getDescription(), task.getStatus(), task.getPriority());
    }

    public Task toEntity(TaskDTO taskDTO){
        Task task = Task.builder()
                        .title(taskDTO.title())
                        .description(taskDTO.description())
                        .status(taskDTO.status())
                        .priority(taskDTO.priority())
                        .build();
        return task;
    }






}
