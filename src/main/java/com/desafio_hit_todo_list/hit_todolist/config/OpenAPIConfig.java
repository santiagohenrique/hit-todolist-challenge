package com.desafio_hit_todo_list.hit_todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("HIT Communications - Desafio API de Gerenciamento de Tarefas")
                    .version("1.0.0")
                    .description("API para gerenciar tarefas em uma aplicação de lista de tarefas.")
                    .contact(new Contact()
                        .name("Henrique S. Pires")
                        .url("https://www.linkedin.com/in/henrique-santiago-pires/")
                        .email("henrique14piressantiago@gmail.com"))
                );
    }
}
