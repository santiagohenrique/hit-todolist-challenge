package com.desafio_hit_todo_list.hit_todolist.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    
}
