package com.desafio_hit_todo_list.hit_todolist.task.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio_hit_todo_list.hit_todolist.exceptions.RecordNotFoundException;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.TaskPageDTO;
import com.desafio_hit_todo_list.hit_todolist.task.dto.mapper.TaskMapper;
import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;
import com.desafio_hit_todo_list.hit_todolist.task.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper mapper;

    @Transactional(readOnly = true)
    public TaskPageDTO findAllTasks(int page, int size){
        Page<Task> tasksPage = repository.findAll(PageRequest.of(page, size));
        // List<Task> tasks = tasksPage.get().map(mapper::toDTO).collect(Collectors.toList());
        List<Task> tasks = tasksPage.toList();
        return new TaskPageDTO(tasks, tasksPage.getTotalElements(), tasksPage.getTotalPages());
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
    public TaskDTO updateTaskStatus(Long id, TaskStatus status) {
        Task taskToBeUpdated = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Task not found with id: " + id));
        taskToBeUpdated.setStatus(status);
        repository.save(taskToBeUpdated);
        return mapper.toDTO(taskToBeUpdated);
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

    public long countTotalTasks() {
        return repository.count();
    }

    public long countCompletedTasks() {
        return repository.countByStatus(TaskStatus.COMPLETED);
    }

    public long countPendingTasks() {
        return repository.countByStatus(TaskStatus.PENDING);
    }

    public long countInProgressTasks() {
        return repository.countByStatus(TaskStatus.IN_PROGRESS);
    }

    public long countHighPriorityTasks() {
        return repository.countByPriority(3L);
    }

    public long countTasksCreatedLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return repository.countByCreatedAtAfter(oneMonthAgo);
    }

    
}
