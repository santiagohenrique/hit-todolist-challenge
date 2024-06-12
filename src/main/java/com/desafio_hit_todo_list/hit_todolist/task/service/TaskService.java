package com.desafio_hit_todo_list.hit_todolist.task.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio_hit_todo_list.hit_todolist.exceptions.RecordNotFoundException;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.entity.TaskStatus;
import com.desafio_hit_todo_list.hit_todolist.task.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Transactional(readOnly = true)
    public List<Task> findAllTasks(){
        List<Task> tasks = repository.findAll();
        return tasks;
    }

    @Transactional(readOnly = true)
    public Task findTaskById(Long id){
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Task not found with id: " + id));
    }

    @Transactional
    public Task insertTask(Task task){
        return repository.save(task);
    }

    @Transactional
    public Task updateTask(Long id, Task taskRequest) {
        Task taskToBeUpdated = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Task not found with id: " + id));
        
        updateTaskFields(taskToBeUpdated, taskRequest);

        return repository.save(taskToBeUpdated);
    }

    @Transactional
    public Task updateTaskStatus(Long id, TaskStatus status) {
        Task taskToBeUpdated = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Task not found with id: " + id));
        taskToBeUpdated.setStatus(status);
        return repository.save(taskToBeUpdated);
    }

    @Transactional
    public void deleteTask(Long id){
        Task taskToBeDeleted = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Task not found with id: " + id));
        repository.delete(taskToBeDeleted);
    }

    private void updateTaskFields(Task taskToUpdate, Task taskRequest) {
        taskToUpdate.setTitle(taskRequest.getTitle());
        taskToUpdate.setDescription(taskRequest.getDescription());
        taskToUpdate.setStatus(taskRequest.getStatus());
        taskToUpdate.setPriority(taskRequest.getPriority());
    }


    
}
