# 🔐 Enterprise Authentication & Authorization API

![Status](https://img.shields.io/badge/STATUS-PRODUCTION_READY-brightgreen)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-orange)

API de autenticação e autorização desenvolvida em **Java 17 com Spring Boot**, projetada para **atuar como um serviço corporativo reutilizável** para aplicações web e sistemas distribuídos. O foco principal do projeto é a **segurança, a organização de código, as boas práticas de desenvolvimento e a prontidão para um ambiente de produção**.

---

## 🎯 Contexto e Objetivo

Em um ecossistema de aplicações modernas, a autenticação e o controle de acesso são responsabilidades críticas e complexas. Este projeto implementa um **serviço de autenticação (Auth Service) independente**, que pode ser consumido por múltiplos clientes, como front-ends web, aplicativos mobile ou outros serviços de backend. Esse tipo de arquitetura é comum em ambientes baseados em **microsserviços e sistemas distribuídos**.

O sistema foi desenvolvido seguindo padrões amplamente adotados no mercado, como:
-   **Autenticação Stateless:** Utilização de JSON Web Tokens (JWT) para garantir que o servidor não precise armazenar o estado da sessão.
-   **Separação de Responsabilidades:** Arquitetura em camadas (Controller, Service, Repository) para um código mais limpo e manutenível.
-   **Controle de Acesso Baseado em Papéis (RBAC):** Gerenciamento de permissões através de papéis (roles) para proteger os endpoints da API.
-   **Código Testável e Manutenível:** Foco em testes unitários para garantir a qualidade e a confiabilidade da lógica de negócio.

---

## 🏗️ Arquitetura Geral

-   **API RESTful:** Interface baseada nos princípios REST para comunicação entre cliente e servidor.
-   **Arquitetura em Camadas:** Divisão lógica que isola as responsabilidades de apresentação, negócio e persistência de dados.
-   **Autenticação Desacoplada:** O serviço de identidade é independente das aplicações que o consomem.
-   **Persistência de Dados:** Uso de **Spring Data JPA** e **Hibernate** para mapeamento objeto-relacional.
-   **Segurança Centralizada:** Configuração robusta e centralizada com **Spring Security**.

> Este projeto não é um CRUD genérico, mas um serviço especializado em **segurança e gerenciamento de identidade**.

---

## 🛠️ Stack Tecnológica

| Categoria                | Tecnologia                                                                        |
| ------------------------ | --------------------------------------------------------------------------------- |
| **Backend**              | Java 17 (LTS), Spring Boot 3, Spring Security, Spring Data JPA, JWT, Bean Validation |
| **Criptografia**         | BCrypt                                                                            |
| **Banco de Dados**       | H2 Database (ambiente de desenvolvimento)                                         |
| **Testes**               | JUnit 5, Mockito                                                                  |
| **Dependências & Build** | Maven                                                                             |
| **Comunicação**          | Spring Boot Starter Mail (SMTP)                                                   |

---

## 🔑 Funcionalidades

### 1. Autenticação
-   `POST /api/auth/signup`: Cadastro de novos usuários com validação de dados e criptografia de senha (BCrypt).
-   `POST /api/auth/signin`: Login de usuários e geração de um token JWT para autenticação stateless.

### 2. Autorização (RBAC)
-   **Papéis (Roles) Definidos:** `ROLE_USER` e `ROLE_ADMIN`.
-   **Proteção de Endpoints:** Uso de anotações como `@PreAuthorize` e configurações centralizadas para restringir o acesso a recursos específicos com base no papel do usuário.

### 3. Recuperação de Senha Segura
-   `POST /api/auth/forgot-password`: Inicia o fluxo de recuperação enviando um e-mail com um token de uso único e tempo de expiração.
-   `POST /api/auth/reset-password`: Permite que o usuário defina uma nova senha após validar o token recebido.

### 4. Testes Automatizados
-   **Testes Unitários:** Cobertura da camada de serviço (Service Layer) para validar a lógica de negócio.
-   **Mocking de Dependências:** Uso de **Mockito** para simular o comportamento de repositórios e serviços externos (como o serviço de e-mail), garantindo testes isolados e rápidos.

---

## 🚀 Executando o Projeto Localmente

### Pré-requisitos
-   Java 17
-   Maven
-   Git
-   Uma conta Gmail com **Senha de App** habilitada para uso com SMTP. [Saiba como gerar aqui](https://support.google.com/accounts/answer/185833).

### 1️⃣ Clone o Repositório
```bash
git clone https://github.com/murylomarques/sistema-autenticacao-api.git
cd sistema-autenticacao-api
```

### 2️⃣ Configure o Ambiente
Edite o arquivo `src/main/resources/application.properties` e insira as credenciais do seu e-mail para o serviço SMTP.

```properties
# Configuração do serviço de e-mail (Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu-email@gmail.com
spring.mail.password=sua-senha-de-app-gerada-no-google
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 3️⃣ Execute a Aplicação
```bash
mvn spring-boot:run
```
A API estará disponível em `http://localhost:8080`.

### 4️⃣ Execute os Testes
Para rodar a suíte de testes automatizados, execute o comando:
```bash
mvn test
```

---

## 📌 Roadmap (Próximas Evoluções)

-   [ ] **Containerização:** Adicionar `Dockerfile` e `docker-compose.yml`.
-   [ ] **Documentação da API:** Implementar Swagger / OpenAPI para documentação interativa.
-   [ ] **Migrações de Banco de Dados:** Utilizar Flyway para versionamento do schema.
-   [ ] **Refresh Token:** Implementar um fluxo de renovação de tokens JWT.
-   [ ] **Rate Limiting:** Adicionar proteção contra ataques de força bruta.
-   [ ] **Deploy:** Publicar a aplicação em um provedor de nuvem (AWS, Render, etc.).

---

## 👨‍💻 Autor

Desenvolvido por **Murylo Marques**.

*Este projeto foi criado com foco em demonstrar habilidades em Backend Java, segurança de APIs e boas práticas de desenvolvimento de software em um contexto corporativo.*
