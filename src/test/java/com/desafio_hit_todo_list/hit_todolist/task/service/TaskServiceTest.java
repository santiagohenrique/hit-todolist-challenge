package com.desafio_hit_todo_list.hit_todolist.task.service;

import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.desafio_hit_todo_list.hit_todolist.exceptions.RecordNotFoundException;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskPageDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.mapper.TaskMapper;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;
import com.desafio_hit_todo_list.hit_todolist.task.repository.TaskRepository;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {


    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskService taskService;

    Long existingId;
    Long nonExistingId;


    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        existingId = 1L;
        nonExistingId = 999L;
    
        List<Task> tasks = Arrays.asList(
                createTask(1L, "Task 1", "Description 1", TaskStatus.PENDING, 1L),
                createTask(2L, "Task 2", "Description 2", TaskStatus.COMPLETED, 2L)
        );
        Page<Task> tasksPage = new PageImpl<>(tasks, PageRequest.of(0, 10), tasks.size());

        Mockito.when(taskRepository.findAll(PageRequest.of(0, 10))).thenReturn(tasksPage);

        Mockito.when(taskRepository.findById(existingId)).thenReturn(Optional.of(tasks.get(0)));
        Mockito.when(taskRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(mapper.toEntity(Mockito.any(TaskDTO.class))).thenAnswer(invocation -> {
            TaskDTO taskDTO = invocation.getArgument(0);
            return createTask(null, taskDTO.title(), taskDTO.description(), taskDTO.status(), taskDTO.priority());
        });
        
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            return task;
        });
        

        Mockito.when(mapper.toDTO(tasks.get(0))).thenReturn(new TaskDTO("Task 1", "Description 1", TaskStatus.PENDING, 1L));
        Mockito.when(mapper.toDTO(tasks.get(1))).thenReturn(new TaskDTO("Task 2", "Description 2", TaskStatus.COMPLETED, 2L));

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

    @Test
    @DisplayName("Should return a task successfully when id exists")
    void findTaskByIdCase1(){
        
        TaskDTO result = taskService.findTaskById(existingId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo("Task 1");
        Assertions.assertThat(result.description()).isEqualTo("Description 1");

        Mockito.verify(taskRepository, times(1)).findById(existingId);
    }

    @Test
    @DisplayName("Should not return a task when id does not exist and should throw an RecordNotFoundException message.")
    void findTaskByIdCase2(){
        
        Assertions.assertThatThrownBy(() -> {
            taskService.findTaskById(nonExistingId);
        }).isInstanceOf(RecordNotFoundException.class)
            .hasMessageContaining("Task not found with id: " + nonExistingId);

        Mockito.verify(taskRepository, times(1)).findById(nonExistingId);
    }

    @Test
    @DisplayName("Should return a task successfully when created.")
    void insertTaskCase1() {
        TaskDTO taskDTO = new TaskDTO("New task", "New task description", TaskStatus.PENDING, 3L);

        Task result = taskService.insertTask(taskDTO);
        
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTitle()).isEqualTo("New task");
        Assertions.assertThat(result.getDescription()).isEqualTo("New task description");

        Mockito.verify(taskRepository, times(1)).save(Mockito.any(Task.class));
    }

    @Test
    @DisplayName("Should update and return a TaskDTO when id exists.")
    void updateTaskCase1(){

        TaskDTO taskToBeUpdatedDTO = new TaskDTO("Updated Task", "Updated description", TaskStatus.COMPLETED, 1L);
        Task updatedTask = createTask(3L, "Updated Task", "Updated description", TaskStatus.COMPLETED, 1L);

        Mockito.when(mapper.toDTO(Mockito.any(Task.class))).thenReturn(taskToBeUpdatedDTO);
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(updatedTask);

        TaskDTO result = taskService.updateTask(existingId, taskToBeUpdatedDTO);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo("Updated Task");
        Assertions.assertThat(result.description()).isEqualTo("Updated description");
        Assertions.assertThat(result.status()).isEqualTo(TaskStatus.COMPLETED);

        Mockito.verify(taskRepository, times(1)).findById(existingId);
        Mockito.verify(taskRepository, times(1)).save(Mockito.any(Task.class));
        Mockito.verify(mapper, times(1)).toDTO(Mockito.any(Task.class));
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
    @DisplayName("Should update and return a TaskDTO when id exists.")
    void updateTaskStatusCase1(){
        TaskDTO taskToBeUpdatedDTO = new TaskDTO("Updated Task", "Updated description", TaskStatus.COMPLETED, 1L);
        Task updatedTask = createTask(1L, "Updated Task", "Updated description", TaskStatus.COMPLETED, 1L);
    
        Mockito.when(mapper.toDTO(Mockito.any(Task.class))).thenReturn(taskToBeUpdatedDTO);
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(updatedTask);
    
        TaskDTO result = taskService.updateTaskStatus(existingId, TaskStatus.COMPLETED);
    
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo("Updated Task");
        Assertions.assertThat(result.description()).isEqualTo("Updated description");
        Assertions.assertThat(result.status()).isEqualTo(TaskStatus.COMPLETED);
    
        Mockito.verify(taskRepository, times(1)).findById(existingId);
        Mockito.verify(taskRepository, times(1)).save(Mockito.any(Task.class));
        Mockito.verify(mapper, times(1)).toDTO(Mockito.any(Task.class));
    }


    @Test
    @DisplayName("Should delete a task successfully.")
    void deleteTaskCase1() {
        
        taskService.deleteTask(existingId);
        Mockito.verify(taskRepository, times(1)).findById(existingId);
        Mockito.verify(taskRepository, times(1)).delete(Mockito.any());

    }

    @Test
    @DisplayName("Should not delete a task when it does not exist and throw a RecordNotFoundException message.")
    void deleteTaskCase2(){
        Assertions.assertThatThrownBy(() -> {
            taskService.deleteTask(nonExistingId);
        }).isInstanceOf(RecordNotFoundException.class)
            .hasMessageContaining("Task not found with id: " + nonExistingId);
    
        Mockito.verify(taskRepository, times(1)).findById(nonExistingId);
        Mockito.verify(taskRepository, times(0)).delete(Mockito.any());
    }


    private Task createTask(Long id, String title, String description, TaskStatus status, Long priority) {
        return Task.builder()
                .id(id)
                .title(title)
                .description(description)
                .status(status)
                .priority(priority)
                .build();
    }

}
