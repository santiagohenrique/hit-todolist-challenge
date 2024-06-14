package com.desafio_hit_todo_list.hit_todolist.task.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;

public class TaskTest {
    

    @Test
    @DisplayName("Should have correct structure")
    void taskEntityCreation(){

        Task task = Task.builder()
                        .id(1L)
                        .title("New task title")
                        .description("New task description")
                        .status(TaskStatus.PENDING)
                        .priority(3L)
                        .build();

        Assertions.assertThat(task.getId()).isNotNull();
        Assertions.assertThat(task.getTitle()).isNotEmpty();
        Assertions.assertThat(task.getDescription()).isNotEmpty();
        Assertions.assertThat(task.getStatus()).isNotNull().isInstanceOf(TaskStatus.class);
        Assertions.assertThat(task.getPriority()).isGreaterThan(0);

    }



}
