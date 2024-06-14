package com.desafio_hit_todo_list.hit_todolist.task.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskPageDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.mapper.TaskMapper;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.repository.TaskRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TaskServiceTestIT {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper mapper;

    Long existingId;

    @BeforeEach
    void setup(){
        existingId = 1L;
    }

    @Test
    @DisplayName("Should return a page of tasks with 5 elements")
    void findAllTasksCase1(){
        int page = 0;
        int size = 10;

        TaskPageDTO taskPageDTO = taskService.findAllTasks(page, size);

        Assertions.assertThat(taskPageDTO.totalElements()).isEqualTo(5);
        Assertions.assertThat(taskPageDTO.totalPages()).isEqualTo(1);

        List<Task> tasks = taskPageDTO.tasks();
        Assertions.assertThat(tasks).hasSize(5);
        Assertions.assertThat(tasks).extracting(Task::getTitle)
            .containsExactlyInAnyOrder("Tarefa 1", "Tarefa 2", "Tarefa 3", "Tarefa 4", "Tarefa 5");
    }

    @Test
    @DisplayName("Should return a task when it exists.")
    void findTaskByIdCase1(){

        TaskDTO result = taskService.findTaskById(existingId);
        Assertions.assertThat(result.title()).isEqualTo("Tarefa 1");
        Assertions.assertThat(result.description()).isEqualTo("Descrição da Tarefa 1");

        
    }




    
}
