package com.desafio_hit_todo_list.hit_todolist.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio_hit_todo_list.hit_todolist.exceptions.RecordNotFoundException;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.mapper.TaskMapper;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.entity.TaskStatus;
import com.desafio_hit_todo_list.hit_todolist.task.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper mapper;

    @Transactional(readOnly = true)
    public Page<Task> findAllTasks(Pageable pageable){
        Page<Task> tasks = repository.findAll(pageable);
        return tasks;
    }

    @Transactional(readOnly = true)
    public TaskDTO findTaskById(Long id){
        Task task = repository.findById(id)
        .orElseThrow(() -> new RecordNotFoundException("Task not found with id: " + id));
        return mapper.toDTO(task);
    }

    @Transactional
    public Task insertTask(TaskDTO taskDTO){
        Task task = mapper.toEntity(taskDTO);
        return repository.save(task);
    }

    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task taskToBeUpdated = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Task not found with id: " + id));
        
        updateTaskFields(taskToBeUpdated, taskDTO);
        repository.save(taskToBeUpdated);
        return mapper.toDTO(taskToBeUpdated);
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

    private void updateTaskFields(Task taskToUpdate, TaskDTO taskRequest) {
        taskToUpdate.setTitle(taskRequest.title());
        taskToUpdate.setDescription(taskRequest.description());
        taskToUpdate.setStatus(taskRequest.status());
        taskToUpdate.setPriority(taskRequest.priority());
    }


    
}
