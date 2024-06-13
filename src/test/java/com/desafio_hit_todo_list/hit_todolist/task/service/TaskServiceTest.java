package com.desafio_hit_todo_list.hit_todolist.task.service;

import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskPageDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.mapper.TaskMapper;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;
import com.desafio_hit_todo_list.hit_todolist.task.repository.TaskRepository;


public class TaskServiceTest {


    @Mock
    private TaskRepository taskRepository;

    @Mock
    @Autowired
    private TaskMapper mapper;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.PENDING);
        task1.setPriority(1L);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus(TaskStatus.COMPLETED);
        task2.setPriority(2L);
        List<Task> tasks = Arrays.asList(task1, task2);
        Page<Task> tasksPage = new PageImpl<>(tasks, PageRequest.of(0, 10), tasks.size());

        Mockito.when(taskRepository.findAll(PageRequest.of(0, 10))).thenReturn(tasksPage);
        Mockito.when(mapper.toDTO(task1)).thenReturn(new TaskDTO("Task 1", "Description 1", TaskStatus.PENDING, 1L));
        Mockito.when(mapper.toDTO(task2)).thenReturn(new TaskDTO("Task 2", "Description 2", TaskStatus.COMPLETED, 2L));
    }
    
    @Test
    @DisplayName("Should return a page of tasks.")
    void findAllTasksCase1(){

        int page = 0;
        int size = 10;
        TaskPageDTO result = taskService.findAllTasks(page, size);
		Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.tasks()).hasSize(2);
        Assertions.assertThat(result.tasks().get(0).getTitle()).isEqualTo("Task 1");
        Assertions.assertThat(result.tasks().get(1).getTitle()).isEqualTo("Task 2");
        Assertions.assertThat(result.totalElements()).isEqualTo(2);
        Assertions.assertThat(result.totalPages()).isEqualTo(1);
        Mockito.verify(taskRepository, times(1)).findAll(PageRequest.of(page, size));

    }





}
