package com.desafio_hit_todo_list.hit_todolist;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.desafio_hit_todo_list.hit_todolist.task.entity.Task;
import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;
import com.desafio_hit_todo_list.hit_todolist.task.repository.TaskRepository;

@SpringBootApplication
public class HitTodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(HitTodolistApplication.class, args);
	}

	@Bean
        @Profile("test")
	CommandLineRunner initDatabase(TaskRepository taskRepository) {
		return args -> {
			taskRepository.deleteAll();

                List<Task> tasks = Arrays.asList(
                Task.builder()
                        .title("Tarefa 1")
                        .description("Descrição da Tarefa 1")
                        .status(TaskStatus.PENDING)
                        .priority(1L)
                        .build(),
                Task.builder()
                        .title("Tarefa 2")
                        .description("Descrição da Tarefa 2")
                        .status(TaskStatus.IN_PROGRESS)
                        .priority(2L)
                        .build(),
                Task.builder()
                        .title("Tarefa 3")
                        .description("Revisar capítulo 3 e 4 do livro de certificação")
                        .status(TaskStatus.IN_PROGRESS)
                        .priority(3L)
                        .build(),
                Task.builder()
                        .title("Tarefa 4")
                        .description("Preparar slides e documentos para apresentação do projeto")
                        .status(TaskStatus.PENDING)
                        .priority(2L)
                        .build(),
                Task.builder()
                        .title("Tarefa 5")
                        .description("Corrigir bug crítico de segurança no sistema de autenticação")
                        .status(TaskStatus.COMPLETED)
                        .priority(1L)
                        .build());

                taskRepository.saveAll(tasks);
			
		};
	}

}
