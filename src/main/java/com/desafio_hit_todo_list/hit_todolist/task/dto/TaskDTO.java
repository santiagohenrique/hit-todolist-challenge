package com.desafio_hit_todo_list.hit_todolist.task.dto;

import org.hibernate.validator.constraints.Length;

import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para Task")
public record TaskDTO(

    @Schema(description = "Título da tarefa", example = "Implementar nova funcionalidade")
    @NotBlank(message = "Title is mandatory")
    @Length(min = 3, message = "The title must have at least 3 characters.")
    String title, 

    @Schema(description = "Descrição da tarefa", example = "Implementar a nova funcionalidade conforme especificações")
    @NotBlank(message = "Title is mandatory")
    @Length(min = 5, message = "The description must have at least 5 characters.")
    String description, 

    @Schema(description = "Status da tarefa", example = "PENDING")
    @NotNull(message = "Status is mandatory")
    TaskStatus status, 

    @Schema(description = "Prioridade da tarefa", example = "3")
    @Min(value = 1, message = "Priority should not be less than 1")
    @Max(value = 5, message = "Priority should not be more than 5")
    Long priority
    ) {

}
