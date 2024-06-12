package com.desafio_hit_todo_list.hit_todolist.task.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {


    @GetMapping
    public String getAllTodos() {
        return "hello";
    }
    
}
