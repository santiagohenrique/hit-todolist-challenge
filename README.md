# Desafio HIT Communications TODO List - API de Gerenciamento de Tarefas

## Descrição
- Este projeto consiste em uma API RESTful para gerenciamento de tarefas (TODO list), desenvolvida com Spring Boot, utilizando Spring Data JPA para acesso ao banco de dados com validação de dados. A API permite operações CRUD básicas para tarefas, incluindo criação, leitura, atualização e remoção de tarefas. Além disso, há suporte para atualização de status das tarefas e paginação na listagem.

## Tecnologias Utilizadas
- Spring Boot: Framework para criação de aplicativos Java baseados em padrões.
- Spring Data JPA: Facilita a implementação de repositórios baseados em JPA.
- Hibernate Validator: Para validação de dados.
- H2 Database: Banco de dados em memória para desenvolvimento.
- MySQL: Banco de dados relacional para persistência de dados em produção.
- Docker Compose: Para configurar o ambiente de desenvolvimento com MySQL.
- Lombok: Biblioteca Java que auxilia na redução de código boilerplate.
- Jakarta Persistence (JPA): Especificação para persistência de dados em Java.
- JUnit 5: Framework de teste para Java.
- Maven: Gerenciador de dependências.

## Funcionalidades:
### Listagem de Tarefas

#### Endpoint: GET /tasks
- Descrição: Retorna uma lista paginada de todas as tarefas cadastradas.

### Buscar Tarefa por ID
#### Endpoint: GET /tasks/{id}
- Descrição: Retorna uma tarefa específica com base no ID fornecido.

### Adicionar Tarefa
#### Endpoint: POST /tasks
- Descrição: Cria uma nova tarefa com os dados fornecidos.

### Atualizar Tarefa
#### Endpoint: PUT /tasks/{id}
- Descrição: Atualiza uma tarefa existente com base no ID fornecido.

### Atualizar Status da Tarefa
#### Endpoint: PATCH /tasks/{id}/{status}
- Descrição: Atualiza o status de uma tarefa existente com base no ID fornecido.
- É necessário checar se está de acordo com o TaskStatus enum para atualizar o status corretamente.

### Deletar Tarefa
#### Endpoint: DELETE /tasks/{id}
- Descrição: Deleta uma tarefa existente com base no ID fornecido.


## Como executar o programa
- Certifique-se de ter o Java JDK 17 ou superior instalado.
- Você precisará de um ambiente de desenvolvimento configurado com o Maven e um IDE de sua preferência (como IntelliJ IDEA, Eclipse, etc.).
- O Docker e Docker Compose são necessários para executar o banco de dados MySQL e o serviço Redis.
- No diretório raiz do projeto, execute o Docker Compose para configurar o MySQL: `docker-compose up`
- Caso preferir, instale as dependências com Maven para executar a aplicação: `mvn clean install` e...
- Execute a aplicação: `mvn spring-boot:run`
