package com.desafio_hit_todo_list.hit_todolist.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskPageDTO;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;
import com.desafio_hit_todo_list.hit_todolist.task.service.TaskService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    @GetMapping
    public ResponseEntity<TaskPageDTO> findAllTasks(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok().body(service.findAllTasks(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findTaskById(@PathVariable @NotNull @Positive Long id) {
        return ResponseEntity.ok().body(service.findTaskById(id));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Task> insertTask(@RequestBody @Valid TaskDTO task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid TaskDTO taskDTO) {
        return ResponseEntity.ok().body(service.updateTask(id, taskDTO));
    }

    @PatchMapping("/{id}/{status}")
    public TaskDTO updateTaskStatus(@PathVariable @NotNull @Positive Long id, @PathVariable @Valid TaskStatus status) {
        return service.updateTaskStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable @NotNull @Positive Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    
}
