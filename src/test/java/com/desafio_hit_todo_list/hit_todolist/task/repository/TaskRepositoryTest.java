package com.desafio_hit_todo_list.hit_todolist.task.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskDTO;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should return a task successfully from database when id exists.")
    void findTaskByIdCase1(){
        Long existingId = 1L;
        TaskDTO data = new TaskDTO("Tarefa nova", "tarefa nova descrição", TaskStatus.PENDING, 3L);
        this.createTask(data);
        Optional<Task> result = taskRepository.findById(existingId);
        Assertions.assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not return a task when it does not exist in the database.")
    void findTaskByIdCase2(){
        Long nonExistingId = 99L;
        Optional<Task> result = taskRepository.findById(nonExistingId);
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    private Task createTask(TaskDTO data){
        Task newTask = new Task();
        newTask.setTitle(data.title());
        newTask.setDescription(data.description());
        newTask.setStatus(data.status());
        newTask.setPriority(data.priority());
        this.entityManager.persist(newTask);
        return newTask;
    }
    
}
