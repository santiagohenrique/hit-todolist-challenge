package com.desafio_hit_todo_list.hit_todolist.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
}
