package com.desafio_hit_todo_list.hit_todolist.task.actuator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import com.desafio_hit_todo_list.hit_todolist.task.service.TaskService;

@Component
@Endpoint(id = "taskMetrics")
public class TaskMetricsEndpoint {

    private final TaskService taskService;

    public TaskMetricsEndpoint(TaskService taskService) {
        this.taskService = taskService;
    }

    @ReadOperation
    public Map<String, Object> taskMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalTasks", taskService.countTotalTasks());
        metrics.put("completedTasks", taskService.countCompletedTasks());
        metrics.put("pendingTasks", taskService.countPendingTasks());
        metrics.put("inProgressTasks", taskService.countInProgressTasks());
        metrics.put("highPriorityTasks", taskService.countHighPriorityTasks());
        metrics.put("tasksCreatedLastMonth", taskService.countTasksCreatedLastMonth());
        return metrics;
    }
}