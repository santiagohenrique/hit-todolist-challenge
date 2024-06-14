package com.desafio_hit_todo_list.hit_todolist.task.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.desafio_hit_todo_list.hit_todolist.exceptions.RecordNotFoundException;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskPageDTO;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;
import com.desafio_hit_todo_list.hit_todolist.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
	private TaskService taskService;

    @Autowired
	private ObjectMapper objectMapper;

    Long existingId;
    Long nonExistingId;
    Task newTask;
    TaskDTO taskDTO;
    
    @BeforeEach
    void setup(){
        
        existingId = 1L;
        nonExistingId = 999L;
        
        List<Task> tasks = Arrays.asList(
                createTask(1L, "Task 1", "Description 1", TaskStatus.PENDING, 1L),
                createTask(2L, "Task 2", "Description 2", TaskStatus.COMPLETED, 2L),
                createTask(3L, "Task 3", "Description 3", TaskStatus.COMPLETED, 1L)
        );

        
        TaskPageDTO pageDTO = new TaskPageDTO(tasks, 3, 1);
        Mockito.when(taskService.findAllTasks(0, 10)).thenReturn(pageDTO);

        TaskPageDTO emptyPageDTO = new TaskPageDTO(Arrays.asList(), 5, 1);
        Mockito.when(taskService.findAllTasks(100, 10)).thenReturn(emptyPageDTO);
        
        taskDTO = new TaskDTO("Task title", "Task description", TaskStatus.COMPLETED, 3L);
        Mockito.when(taskService.findTaskById(existingId)).thenReturn(taskDTO);
        Mockito.when(taskService.findTaskById(nonExistingId)).thenThrow(RecordNotFoundException.class);

        newTask = createTask(4L, "New task title", "New task description", TaskStatus.PENDING, 2L);
        Mockito.when(taskService.insertTask(ArgumentMatchers.any())).thenReturn(newTask);

        Mockito.when(taskService.updateTask(existingId, taskDTO)).thenReturn(taskDTO);
        Mockito.when(taskService.updateTask(nonExistingId, taskDTO)).thenThrow(RecordNotFoundException.class);

        Mockito.doNothing().when(taskService).deleteTask(existingId);
        Mockito.doThrow(RecordNotFoundException.class).when(taskService).deleteTask(nonExistingId);


        Mockito.when(taskService.updateTaskStatus(existingId, TaskStatus.COMPLETED)).thenReturn(taskDTO);


    }

    @Test
    @DisplayName("Should return a page of tasks.")
    void findAllTasksCase1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks?page=0&size=10")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.tasks").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[0].title").value("Task 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[1].title").value("Task 2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[2].id").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[2].title").value("Task 3"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));
    }

    @Test
    @DisplayName("Should return empty when page does not exist.")
    void findAllTasksCase2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks?page=100&size=10")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tasks").isEmpty())
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));
    }

    @Test
    @DisplayName("Should return a task when id exists.")
    void findTaskByIdCase1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", existingId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Task title"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Task description"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("COMPLETED"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("3"));
            
    }

    @Test
    @DisplayName("Should not return a task when id does not exist and throw a RecordNotFoundException message.")
    void findTaskByIdCase2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", nonExistingId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should insert and return a new task.")
    void insertTaskCase1() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New task title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("New task description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PENDING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("2"));

                
                
    }

    @Test
    @DisplayName("Should update an existing task.")
    void updateTaskCase1() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Task title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Task description"));
    }

    @Test
    @DisplayName("Should not return a TaskDTO when id does not exist and should throw an RecordNotFoundException message.")
    void updateTaskCase2() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{id}", nonExistingId)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should update task status.")
    void updateTaskStatusCase1() throws Exception  {
        mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/{id}/{status}", existingId, TaskStatus.COMPLETED)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("Should delete task when id exists.")
    void deleteTaskCase1() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/{id}", existingId))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    @DisplayName("Should not delete a task when it does not exist and throw a RecordNotFoundException message.")
    void deleteTaskCase2() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/{id}", nonExistingId))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
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
