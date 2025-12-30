🔐 Enterprise Authentication & Authorization API

STATUS: PRODUCTION_READY
Java: 17
Spring Boot: 3.x
Security: Spring Security + JWT

API de autenticação e autorização desenvolvida em Java 17 com Spring Boot, simulando um serviço corporativo reutilizável para aplicações web e sistemas distribuídos.
O foco do projeto é segurança, organização de código, boas práticas e prontidão para ambiente real.

────────────────────────────────────────
🎯 CONTEXTO E OBJETIVO
────────────────────────────────────────
Em aplicações modernas, a autenticação e o controle de acesso são responsabilidades críticas.
Este projeto representa um Auth Service independente, que poderia ser utilizado por múltiplas aplicações (frontends web, mobile ou outros serviços backend).

O sistema foi desenvolvido seguindo padrões amplamente utilizados em empresas, como:
- Autenticação stateless com JWT
- Separação clara de responsabilidades (Controller / Service / Repository)
- Controle de acesso baseado em papéis (RBAC)
- Código testável e manutenível

────────────────────────────────────────
🏗️ ARQUITETURA GERAL
────────────────────────────────────────
- API RESTful
- Arquitetura em camadas
- Autenticação desacoplada do cliente
- Persistência via JPA/Hibernate
- Segurança centralizada via Spring Security

Este projeto não é um CRUD genérico, mas um serviço focado em segurança e identidade.

────────────────────────────────────────
🛠️ STACK TECNOLÓGICA
────────────────────────────────────────

Backend:
- Java 17 (LTS)
- Spring Boot 3
- Spring Security
- Spring Data JPA (Hibernate)
- JWT (JSON Web Token)
- Bean Validation
- BCrypt

Infraestrutura & Qualidade:
- Maven
- H2 Database (ambiente local)
- JUnit 5
- Mockito
- Spring Boot Starter Mail (SMTP)

────────────────────────────────────────
🔑 FUNCIONALIDADES
────────────────────────────────────────

Autenticação:
- POST /api/auth/signup
  - Cadastro de usuários
  - Validação de dados
  - Criptografia de senha com BCrypt

- POST /api/auth/signin
  - Login
  - Geração de token JWT
  - Autenticação stateless

Autorização (RBAC):
- Papéis:
  - ROLE_USER
  - ROLE_ADMIN
- Proteção de endpoints com:
  - @PreAuthorize
  - Configuração centralizada de segurança

Recuperação de Senha:
- POST /api/auth/forgot-password
  - Envio de e-mail com token de uso único
  - Token com tempo de expiração

- POST /api/auth/reset-password
  - Validação do token
  - Atualização segura da senha

Testes Automatizados:
- Testes unitários na Service Layer
- Uso de Mockito para mockar:
  - Repositórios
  - Serviços externos (e-mail)
- Isolamento da lógica de negócio

────────────────────────────────────────
🚀 EXECUTANDO O PROJETO LOCALMENTE
────────────────────────────────────────

Pré-requisitos:
- Java 17
- Maven
- Git
- Conta Gmail com Senha de App (SMTP)

1) Clonar o repositório:
git clone https://github.com/murylomarques/sistema-autenticacao-api.git
cd sistema-autenticacao-api

2) Configurar variáveis de ambiente:
Editar o arquivo src/main/resources/application.properties:

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu-email@gmail.com
spring.mail.password=sua-senha-de-app
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

3) Executar a aplicação:
mvn spring-boot:run

API disponível em:
http://localhost:8080

4) Executar testes:
mvn test

────────────────────────────────────────
✨ DEMONSTRAÇÃO
────────────────────────────────────────
Exemplo real do e-mail de recuperação de senha enviado pela aplicação:
assets/email-recuperacao-senha.png

────────────────────────────────────────
📌 ROADMAP (PRÓXIMOS PASSOS)
────────────────────────────────────────
- Docker + Docker Compose
- Swagger / OpenAPI
- Migrações com Flyway
- Refresh Token
- Rate Limiting
- Deploy em Cloud (AWS / Render)

────────────────────────────────────────
👨‍💻 AUTOR
────────────────────────────────────────
Desenvolvido por Murylo Marques

Projeto focado em Backend Java, segurança e boas práticas corporativas.
