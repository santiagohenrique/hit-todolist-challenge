package com.desafio_hit_todo_list.hit_todolist.task.dto;

import org.hibernate.validator.constraints.Length;

import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskDTO(

    @NotBlank(message = "Title is mandatory")
    @Length(min = 3, message = "The title must have at least 3 characters.")
    String title, 

    @NotBlank(message = "Title is mandatory")
    @Length(min = 5, message = "The description must have at least 5 characters.")
    String description, 

    @NotNull(message = "Status is mandatory")
    TaskStatus status, 

    @Min(value = 1, message = "Priority should not be less than 1")
    @Max(value = 5, message = "Priority should not be more than 5")
    Long priority
    ) {

}
