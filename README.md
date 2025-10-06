# Sistema de Autenticação - API (Back-end)

![Status](https://img.shields.io/badge/STATUS-EM%20DESENVOLVIMENTO-yellow)

## 📖 Sobre o Projeto

API RESTful completa para autenticação e autorização de usuários, desenvolvida com o ecossistema Spring. O objetivo deste projeto é demonstrar habilidades em segurança de APIs com JWT, boas práticas de desenvolvimento back-end e gerenciamento de acesso por papéis (Roles).

Este projeto é o back-end de um sistema full-stack. O front-end em React pode ser encontrado [aqui](link-para-seu-futuro-repo-do-front).

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**: Versão LTS mais recente do Java, garantindo performance e segurança.
- **Spring Boot 3+**: Framework principal para a criação da aplicação.
- **Spring Security**: Para a camada de segurança e controle de acesso.
- **JWT (JSON Web Token)**: Para a autenticação stateless da API, utilizando a biblioteca `jjwt`.
- **Spring Data JPA**: Para a persistência de dados de forma simplificada.
- **Maven**: Gerenciador de dependências do projeto.
- **H2 Database**: Banco de dados em memória para o ambiente de desenvolvimento.
- **Lombok**: Para reduzir a verbosidade do código Java.

---

## ✅ Funcionalidades Implementadas e Planejadas

- [x] **Endpoints de Autenticação**:
    - [x] `POST /api/auth/signup`: Registro de novos usuários com validação e criptografia de senha (BCrypt).
    - [x] `POST /api/auth/signin`: Login de usuários e geração de token de acesso JWT.
- [ ] **Controle de Acesso por Papéis**:
    - [x] Distinção entre usuários comuns (`ROLE_USER`) e administradores (`ROLE_ADMIN`) no banco de dados.
    - [ ] Proteção de endpoints específicos por papel.
- [ ] **Recuperação de Senha**:
    - [ ] `POST /auth/forgot-password`: Envio de e-mail com token de recuperação.
    - [ ] `POST /auth/reset-password`: Redefinição da senha com o token.
- [ ] **Testes Unitários**:
    - [ ] Cobertura de testes para as principais regras de negócio.

---

## 🚀 Como Executar o Projeto (Instruções Futuras)

*Instruções detalhadas sobre como configurar o ambiente e executar o projeto serão adicionadas ao final do desenvolvimento.*