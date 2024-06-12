package com.desafio_hit_todo_list.hit_todolist.task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.entity.TaskStatus;
import com.desafio_hit_todo_list.hit_todolist.task.service.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    @GetMapping
    public ResponseEntity<List<Task>> findAllTasks() {
        List<Task> tasks = service.findAllTasks();
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findTaskById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findTaskById(id));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Task> insertTask(@RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskRequest) {
        return ResponseEntity.ok().body(service.updateTask(id, taskRequest));
    }

    @PatchMapping("/{id}/{status}")
    public Task updateTaskStatus(@PathVariable Long id, @PathVariable TaskStatus status) {
        return service.updateTaskStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    
}
