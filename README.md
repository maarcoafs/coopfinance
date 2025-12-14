# 🏦 CoopFinance - Sistema de Gestão Financeira Cooperativa

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=for-the-badge&logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.9.11-C71A36?style=for-the-badge&logo=apache-maven)
![Tests](https://img.shields.io/badge/Tests-12%20Passed-success?style=for-the-badge)
![Coverage](https://img.shields.io/badge/Coverage-64%25-yellow?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

Sistema completo de gestão financeira para cooperativas, desenvolvido com **arquitetura de microserviços**, **Clean Architecture** e **boas práticas de desenvolvimento**.

---

## 📋 Sobre o Projeto

O **CoopFinance** é uma plataforma web fullstack que demonstra habilidades avançadas em desenvolvimento backend Java, incluindo:

- ✅ Arquitetura de microserviços
- ✅ Clean Architecture (Domain, Application, Infrastructure, Presentation)
- ✅ Design Patterns (Repository, DTO, Factory, Strategy)
- ✅ Autenticação segura com JWT
- ✅ API RESTful documentada com Swagger
- ✅ Testes unitários e de integração (12 testes, 0 falhas)
- ✅ Validações robustas com Bean Validation
- ✅ Banco de dados H2 em memória

**Contexto:** Projeto desenvolvido como portfólio para demonstrar competências em desenvolvimento fullstack, com foco em qualidade de código e boas práticas.

---

## 🏗️ Arquitetura

### Microserviços

```
backend/
├── auth-service          # Autenticação e autorização ✅
├── transaction-service   # Gestão de transações (em desenvolvimento)
├── investment-service    # Gestão de investimentos (em desenvolvimento)
└── common               # Código compartilhado
```

### Clean Architecture

```
auth-service/
├── domain/              # Entidades e regras de negócio
│   ├── entity/         # User
│   └── repository/     # UserRepository
├── application/         # Casos de uso
│   ├── dto/            # Data Transfer Objects
│   └── service/        # AuthService (lógica de negócio)
├── infrastructure/      # Frameworks e ferramentas externas
│   ├── config/         # Configurações (Security, Swagger)
│   └── security/       # JWT Utility
└── presentation/        # Interface de entrada
    └── controller/     # AuthController (API REST)
```

---

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem principal
- **Spring Boot 3.2.0** - Framework
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - ORM
- **H2 Database** - Banco de dados em memória
- **JWT (jsonwebtoken 0.12.3)** - Tokens de autenticação
- **Lombok** - Redução de boilerplate
- **JUnit 5 + Mockito** - Testes unitários
- **Swagger/OpenAPI 3** - Documentação de APIs
- **Maven 3.9.11** - Gerenciamento de dependências
- **JaCoCo** - Cobertura de testes

### Frontend (Planejado)
- React 18 + TypeScript
- Vite
- Axios
- SASS

---

## ⚙️ Pré-requisitos

Antes de começar, você precisa ter instalado:

- **Java JDK 17+** → [Download](https://www.oracle.com/java/technologies/downloads/#java17)
- **Maven 3.8+** → [Download](https://maven.apache.org/download.cgi)
- **Git** → [Download](https://git-scm.com/downloads)
- **IDE** (VS Code com Extension Pack for Java ou IntelliJ IDEA)

---

## 🛠️ Como Executar

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/coopfinance.git
cd coopfinance
```

### 2. Navegue até o Auth Service

```bash
cd backend/auth-service
```

### 3. Execute o projeto

```bash
# Baixar dependências e compilar
mvn clean install

# Executar aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8081**

---

## 📚 Documentação da API

### Swagger UI
Acesse a documentação interativa: **http://localhost:8081/swagger-ui.html**

### Endpoints Principais

#### 🔐 Autenticação

**POST /api/auth/register** - Registrar novo usuário

```json
// Request
{
  "name": "João Silva",
  "email": "joao@example.com",
  "password": "senha123"
}

// Response (201 Created)
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "name": "João Silva",
  "email": "joao@example.com"
}
```

**POST /api/auth/login** - Fazer login

```json
// Request
{
  "email": "joao@example.com",
  "password": "senha123"
}

// Response (200 OK)
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "name": "João Silva",
  "email": "joao@example.com"
}
```

**GET /api/auth/me** - Obter dados do usuário logado

```http
Headers:
  X-User-Id: 1

// Response (200 OK)
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "active": true
}
```

---

## 🗄️ Banco de Dados H2

### Console H2
Acesse: **http://localhost:8081/h2-console**

**Configurações:**
- **JDBC URL:** `jdbc:h2:mem:authdb`
- **Username:** `sa`
- **Password:** *(deixe em branco)*

### Estrutura da Tabela Users

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
```

---

## 🧪 Testes

### Executar todos os testes

```bash
mvn test
```

### Gerar relatório de cobertura

```bash
mvn test jacoco:report
```

O relatório será gerado em: `target/site/jacoco/index.html`

### Resultados dos Testes

```
✅ AuthServiceTest: 8 testes passando
  ✓ Deve registrar novo usuário com sucesso
  ✓ Não deve registrar usuário com email duplicado
  ✓ Deve fazer login com sucesso
  ✓ Não deve fazer login com credenciais inválidas
  ✓ Não deve fazer login com usuário não encontrado
  ✓ Não deve fazer login com usuário inativo
  ✓ Deve buscar usuário por ID com sucesso
  ✓ Deve lançar exceção ao buscar usuário inexistente

✅ AuthControllerTest: 4 testes passando
  ✓ Deve registrar com sucesso (POST /api/auth/register)
  ✓ Deve retornar erro com dados inválidos
  ✓ Deve fazer login com sucesso (POST /api/auth/login)
  ✓ Deve retornar usuário logado (GET /api/auth/me)

📊 Total: 12 testes, 0 falhas, 64% de cobertura
```

---

## 🎯 Princípios Aplicados

### SOLID

- **S**ingle Responsibility: Cada classe tem uma única responsabilidade
- **O**pen/Closed: Aberto para extensão, fechado para modificação
- **L**iskov Substitution: Interfaces bem definidas
- **I**nterface Segregation: Interfaces específicas
- **D**ependency Inversion: Dependências de abstrações

### Clean Code

- ✅ Nomes descritivos e significativos
- ✅ Funções pequenas e focadas
- ✅ Comentários apenas quando necessário
- ✅ Tratamento adequado de erros
- ✅ Validações de entrada robustas

### Design Patterns

- **Repository Pattern** - Abstração de acesso a dados
- **DTO Pattern** - Transferência de dados entre camadas
- **Factory Pattern** - Criação de objetos complexos
- **Builder Pattern** - Construção de objetos (Lombok @Builder)
- **Dependency Injection** - Inversão de controle com Spring

---

## 🔒 Segurança

- ✅ Senhas criptografadas com **BCrypt**
- ✅ Tokens JWT com expiração configurável (24 horas)
- ✅ Validação de entrada com **Bean Validation**
- ✅ CORS configurado
- ✅ Headers de segurança (X-Frame-Options, X-Content-Type-Options)
- ✅ Sessões stateless (REST puro)

---

## 📁 Estrutura de Pastas

```
coopfinance/
├── .gitignore
├── README.md
├── test-api.http                    # Requisições REST para teste
├── backend/
│   ├── pom.xml                      # Parent POM
│   └── auth-service/
│       ├── pom.xml
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/coopfinance/auth/
│       │   │   │   ├── AuthServiceApplication.java
│       │   │   │   ├── domain/
│       │   │   │   │   ├── entity/User.java
│       │   │   │   │   └── repository/UserRepository.java
│       │   │   │   ├── application/
│       │   │   │   │   ├── dto/
│       │   │   │   │   │   ├── LoginRequest.java
│       │   │   │   │   │   ├── RegisterRequest.java
│       │   │   │   │   │   ├── AuthResponse.java
│       │   │   │   │   │   └── UserResponse.java
│       │   │   │   │   └── service/AuthService.java
│       │   │   │   ├── infrastructure/
│       │   │   │   │   ├── config/SecurityConfig.java
│       │   │   │   │   └── security/JwtUtil.java
│       │   │   │   └── presentation/
│       │   │   │       └── controller/AuthController.java
│       │   │   └── resources/
│       │   │       └── application.yml
│       │   └── test/
│       │       └── java/com/coopfinance/auth/
│       │           ├── application/service/AuthServiceTest.java
│       │           └── presentation/controller/AuthControllerTest.java
│       └── target/
└── frontend/                        # (Planejado)
```

---

## 🚧 Roadmap

- [x] Auth Service com JWT
- [x] Testes unitários e de integração
- [x] Documentação Swagger
- [x] Clean Architecture
- [ ] Transaction Service (CRUD de transações)
- [ ] Investment Service (Gestão de investimentos)
- [ ] Frontend React + TypeScript
- [ ] Testes E2E
- [ ] CI/CD com GitHub Actions
- [ ] Deploy em cloud (Railway/Render)
- [ ] API Gateway
- [ ] Service Discovery

---

## 📊 Métricas de Qualidade

| Métrica | Valor | Status |
|---------|-------|--------|
| Cobertura de Testes | 64% | 🟡 Bom |
| Testes Passando | 12/12 | 🟢 Excelente |
| Clean Architecture | Sim | 🟢 Excelente |
| Documentação API | Swagger | 🟢 Excelente |
| Design Patterns | 5+ | 🟢 Excelente |
| SOLID Principles | Aplicados | 🟢 Excelente |

---

## 🤝 Como Contribuir

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'feat: adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

### Convenção de Commits

- `feat:` Nova funcionalidade
- `fix:` Correção de bug
- `docs:` Documentação
- `test:` Testes
- `refactor:` Refatoração
- `style:` Formatação
- `chore:` Tarefas gerais

---

## 👨‍💻 Autor

**Seu Nome**

- LinkedIn: [Marco Antonio Flores da Silva](https://www.linkedin.com/in/maarcofs/)
- GitHub: [@maarcoafs](https://github.com/maarcoafs)
- Email: maarco.afs@gmail.com
- Portfolio: [https://portfoliomarcoflores.netlify.app/](https://portfoliomarcoflores.netlify.app/)

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 🙏 Agradecimentos

- 
- 
- 

---

## 📞 Contato

Tem alguma dúvida ou sugestão? Entre em contato!

- 📧 Email: maarco.afs@gmail.com
- 💼 LinkedIn: [Marco Antonio Flores da Silva](https://www.linkedin.com/in/maarcofs/)
- 🐙 GitHub: [@maarcoafs](https://github.com/maarcoafs)

---

<div align="center">

**Desenvolvido com ❤️ para demonstrar habilidades em desenvolvimento fullstack**

⭐ Se este projeto te ajudou, deixe uma estrela!

</div>