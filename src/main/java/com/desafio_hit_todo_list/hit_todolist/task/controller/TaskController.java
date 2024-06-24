package com.desafio_hit_todo_list.hit_todolist.task.controller;

import java.util.HashMap;
import java.util.Map;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Tag(name = "task", description = "Operações relacionadas ao controller Task.")

@Validated
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    @Operation(summary = "Listar todas tasks", description = "Recuperar uma lista paginada de tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Encontrou as tasks", content = { 
            @Content(mediaType = "application/json", schema = @Schema(implementation = TaskPageDTO.class)) 
        }),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    @GetMapping
    public ResponseEntity<TaskPageDTO> findAllTasks(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok().body(service.findAllTasks(page, size));
    }

    
    @Operation(summary = "Obter task por ID", description = "Recuperar uma task pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Encontrou a task", content = { 
            @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class)) 
        }),
        @ApiResponse(responseCode = "404", description = "Task não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findTaskById(@PathVariable @NotNull @Positive Long id) {
        return ResponseEntity.ok().body(service.findTaskById(id));
    }

    
    @Operation(summary = "Criar nova task", description = "Criar uma nova task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task criada", content = { 
            @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class)) 
        }),
        @ApiResponse(responseCode = "400", description = "Dados da task inválidos")
    })
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Task> insertTask(@RequestBody @Valid TaskDTO task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertTask(task));
    }

    @Operation(summary = "Atualizar task", description = "Atualizar uma task existente pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task atualizada", content = { 
            @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class)) 
        }),
        @ApiResponse(responseCode = "404", description = "Task não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid TaskDTO taskDTO) {
        return ResponseEntity.ok().body(service.updateTask(id, taskDTO));
    }

    @Operation(summary = "Atualizar status da task", description = "Atualizar o status de uma task existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status da task atualizado", content = { 
            @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class)) 
        }),
        @ApiResponse(responseCode = "404", description = "Task não encontrada")
    })
    @PatchMapping("/{id}/{status}")
    public TaskDTO updateTaskStatus(@PathVariable @NotNull @Positive Long id, @PathVariable @Valid TaskStatus status) {
        return service.updateTaskStatus(id, status);
    }

    @Operation(summary = "Excluir task", description = "Excluir uma task existente pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Task excluída"),
        @ApiResponse(responseCode = "404", description = "Task não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable @NotNull @Positive Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obter métricas de tasks", description = "Recuperar métricas sobre tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Métricas recuperadas", content = { 
            @Content(mediaType = "application/json") 
        })
    })
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getTaskMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalTasks", service.countTotalTasks());
        metrics.put("completedTasks", service.countCompletedTasks());
    return ResponseEntity.ok().body(metrics);
}

    
}
