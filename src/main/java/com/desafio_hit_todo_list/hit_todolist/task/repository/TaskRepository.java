package com.desafio_hit_todo_list.hit_todolist.task.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByStatus(TaskStatus status);
    
    long countByPriority(Long priority);
    
    long countByCreatedAtAfter(LocalDateTime dateTime);
    
    List<Task> findByStatus(TaskStatus status);
}
