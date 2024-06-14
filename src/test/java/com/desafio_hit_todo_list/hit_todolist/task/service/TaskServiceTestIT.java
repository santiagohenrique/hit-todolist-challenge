package com.desafio_hit_todo_list.hit_todolist.task.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.desafio_hit_todo_list.hit_todolist.exceptions.RecordNotFoundException;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskPageDTO;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;
import com.desafio_hit_todo_list.hit_todolist.task.repository.TaskRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TaskServiceTestIT {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;


    Long existingId;
    Long nonExistingId;

    @BeforeEach
    void setup(){
        existingId = 1L;
        nonExistingId = 999L;
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
    @DisplayName("Should return empty when page does not exist")
    void findAllTasksCase2(){
        int page = 500;
        int size = 10;

        TaskPageDTO taskPageDTO = taskService.findAllTasks(page, size);

        Assertions.assertThat(taskPageDTO).isNotNull();
        Assertions.assertThat(taskPageDTO.tasks()).isEmpty();
    }

    @Test
    @DisplayName("Should return a task when it exists.")
    void findTaskByIdCase1(){

        TaskDTO result = taskService.findTaskById(existingId);
        Assertions.assertThat(result.title()).isEqualTo("Tarefa 1");
        Assertions.assertThat(result.description()).isEqualTo("Descrição da Tarefa 1");

        
    }

    @Test
    @DisplayName("Should not return a task when id does not exist and throw a RecordNotFoundException.")
    void findTaskByIdCase2(){
        Assertions.assertThatThrownBy(() -> {
            taskService.findTaskById(nonExistingId);
        }).isInstanceOf(RecordNotFoundException.class)
        .hasMessageContaining("Task not found with id: " + nonExistingId);
    }

    @Test
    @DisplayName("Should return a task successfully when created.")
    void insertTaskCase1(){
        TaskDTO taskDTO = new TaskDTO("New task", "New task description", TaskStatus.PENDING, 3L);
        Task result = taskService.insertTask(taskDTO);
        
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTitle()).isEqualTo("New task");
        Assertions.assertThat(result.getDescription()).isEqualTo("New task description");
        Assertions.assertThat(taskRepository.count()).isEqualTo(6);
    }

    @Test
    @DisplayName("Should not return a task when an input is not valid.")
    void insertTaskCase2(){
        TaskDTO taskDTO = new TaskDTO("Ne", "New task description", TaskStatus.PENDING, 3L);
        Assertions.assertThatThrownBy(() -> {
            taskService.insertTask(taskDTO);
        }).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("The title must have at least 3 characters");
    }

    @Test
    @DisplayName("Should update and return a TaskDTO when id exists.")
    void updateTaskCase1(){
        TaskDTO taskToBeUpdatedDTO = new TaskDTO("Updated Task", "Updated description", TaskStatus.COMPLETED, 1L);

        TaskDTO result = taskService.updateTask(existingId, taskToBeUpdatedDTO);
        
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo("Updated Task");
        Assertions.assertThat(result.description()).isEqualTo("Updated description");
        Assertions.assertThat(result.status()).isEqualTo(TaskStatus.COMPLETED);

        Optional<Task> updatedTaskOptional = taskRepository.findById(existingId);
        Assertions.assertThat(updatedTaskOptional).isPresent();

        Task updatedTask = updatedTaskOptional.get();
        Assertions.assertThat(updatedTask.getTitle()).isEqualTo("Updated Task");
        Assertions.assertThat(updatedTask.getDescription()).isEqualTo("Updated description");
        Assertions.assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.COMPLETED);

    }

    @Test
    @DisplayName("Should not return a TaskDTO when id does not exist and should throw an RecordNotFoundException message.")
    void updateTaskCase2(){
        Assertions.assertThatThrownBy(() -> {
            taskService.updateTask(nonExistingId, null);
        }).isInstanceOf(RecordNotFoundException.class)
        .hasMessageContaining("Task not found with id: " + nonExistingId);
    }

    @Test
    @DisplayName("Should update and return a TaskStatus of an Task when id exists.")
    void updateTaskStatusCase1(){
        TaskStatus newStatus = TaskStatus.COMPLETED;

        TaskDTO result = taskService.updateTaskStatus(existingId, newStatus);
        
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo("Tarefa 1");
        Assertions.assertThat(result.description()).isEqualTo("Descrição da Tarefa 1");
        Assertions.assertThat(result.status()).isEqualTo(TaskStatus.COMPLETED);

        Optional<Task> updatedTaskOptional = taskRepository.findById(existingId);
        Assertions.assertThat(updatedTaskOptional).isPresent();

        Task updatedTask = updatedTaskOptional.get();
        Assertions.assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should not return a task with a new status and throw a RecordNotFoundException message")
    void updateTaskStatusCase2(){
        Assertions.assertThatThrownBy(() -> {
            taskService.updateTaskStatus(nonExistingId, TaskStatus.CANCELLED);
        }).isInstanceOf(RecordNotFoundException.class)
        .hasMessageContaining("Task not found with id: " + nonExistingId);
    }

    @Test
    @DisplayName("Should delete a task successfully.")
    void deleteTaskCase1(){
        taskService.deleteTask(existingId);
        Assertions.assertThat(taskRepository.count()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should not delete a task when it does not exist and throw a RecordNotFoundException message.")
    void deleteTaskCase2(){
        Assertions.assertThatThrownBy(() -> {
            taskService.deleteTask(nonExistingId);
        }).isInstanceOf(RecordNotFoundException.class)
        .hasMessageContaining("Task not found with id: " + nonExistingId);
    }
    
}
