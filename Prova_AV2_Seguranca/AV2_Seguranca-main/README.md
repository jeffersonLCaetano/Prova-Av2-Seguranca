# Prova AV2: API REST com Segurança JWT, Monitoramento e Deploy

Este projeto é uma API RESTful completa desenvolvida em Java com Spring Boot. A API implementa um CRUD de Produtos e Categorias e adiciona uma camada de segurança robusta utilizando JSON Web Tokens (JWT) para autenticação e autorização de acesso aos endpoints.

O projeto está totalmente conteinerizado com Docker, preparado para monitoramento via Actuator e foi deployado na nuvem, estando publicamente acessível.

## 🚀 API Online

A aplicação está deployada na plataforma Render e pode ser acessada através da seguinte URL base:

**`https://github.com/Tiagommoraes/AV2_Seguranca`**

*(Nota: A API pode demorar alguns segundos para "acordar" na primeira requisição devido ao plano gratuito da plataforma Render.)*

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security 6.x:** Para autenticação e autorização.
- **JWT (JSON Web Token):** Para gerenciamento de sessões stateless, com a biblioteca `java-jwt` da Auth0.
- **Spring Data JPA / Hibernate:** Para persistência de dados.
- **H2 Database:** Banco de dados em memória para o perfil de desenvolvimento (`dev`).
- **Lombok:** Para redução de código boilerplate.
- **Springdoc OpenAPI (Swagger):** Para documentação interativa e visual da API.
- **Spring Boot Actuator:** Para exposição de métricas de monitoramento (`/health`, `/metrics`, etc.).
- **Docker:** Para conteinerização da aplicação.
- **Render:** Plataforma de nuvem (PaaS) para o deploy.
- **Maven:** Como gerenciador de dependências e build do projeto.

## 📋 Pré-requisitos para Rodar Localmente

- Java JDK 17 ou superior.
- Apache Maven 3.8 ou superior.

## ⚙️ Como Rodar o Projeto Localmente

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/seu-repositorio.git](https://github.com/seu-usuario/seu-repositorio.git)
    cd seu-repositorio
    ```
    *(Lembre-se de trocar pela URL do seu repositório)*

2.  **Execute a aplicação com o Maven:**
    ```bash
    ./mvnw spring-boot:run
    ```
    A aplicação iniciará na porta `8080` utilizando o perfil `dev` com o banco de dados H2 em memória por padrão.

3.  **Acesse o Banco de Dados H2 (opcional):**
    - URL do Console: `http://localhost:8080/h2-console`
    - **JDBC URL (Importante):** `jdbc:h2:mem:testdb`
    - **User Name:** `sa`
    - **Password:** `password`

## 📖 Endpoints da API

A documentação interativa completa da API está disponível via Swagger UI, que é a melhor forma de explorar e testar os endpoints:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html` (para rodar localmente) ou `https://prova-api-gleminhos.onrender.com/swagger-ui.html` (na versão online).

### Autenticação

| Método | Endpoint          | Proteção | Descrição                                    | Exemplo de Corpo (JSON)                                |
| :----- | :---------------- | :------- | :------------------------------------------- | :----------------------------------------------------- |
| `POST` | `/auth/register`  | Público  | Registra um novo usuário (`USER` ou `ADMIN`).  | `{"login":"user", "password":"123", "role":"USER"}` |
| `POST` | `/auth/login`     | Público  | Autentica um usuário e retorna um token JWT. | `{"login":"user", "password":"123"}`                |

### Produtos (Exemplo)

| Método   | Endpoint          | Proteção                     | Descrição                    |
| :------- | :---------------- | :--------------------------- | :--------------------------- |
| `GET`    | `/produtos`       | Requer Token (`USER`/`ADMIN`) | Lista todos os produtos.     |
| `GET`    | `/produtos/{id}`  | Requer Token (`USER`/`ADMIN`) | Busca um produto por ID.     |
| `POST`   | `/produtos`       | Requer Token (`ADMIN`)       | Cria um novo produto.        |
| `PUT`    | `/produtos/{id}`  | Requer Token (`ADMIN`)       | Atualiza um produto existente. |
| `DELETE` | `/produtos/{id}`  | Requer Token (`ADMIN`)       | Exclui um produto.           |

*(Você pode adicionar uma tabela similar para seus endpoints de Categoria aqui)*

## 🐳 Como Rodar com Docker

O projeto inclui um `Dockerfile` multi-stage otimizado para produção.

1.  **Construa a imagem Docker:**
    ```bash
    docker build -t prova-api .
    ```

2.  **Execute o contêiner:**
    ```bash
    docker run -p 8080:8080 prova-api
    ```
    A aplicação estará acessível em `http://localhost:8080`.
