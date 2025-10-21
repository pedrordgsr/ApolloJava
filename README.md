# 📦 Apollo ERP - Sistema de Gestão Empresarial

## 📋 Índice
- [Visão Geral](#visão-geral)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura do Sistema](#arquitetura-do-sistema)
- [Configuração e Instalação](#configuração-e-instalação)
- [Autenticação e Segurança](#autenticação-e-segurança)
- [Módulos do Sistema](#módulos-do-sistema)
- [API Endpoints](#api-endpoints)
- [Modelos de Dados](#modelos-de-dados)
- [Exemplos de Uso](#exemplos-de-uso)
- [Testando a API](#testando-a-api)

---

## 🎯 Visão Geral

**Apollo ERP** é um sistema completo de gestão empresarial (ERP) desenvolvido em Java com Spring Boot. O sistema oferece funcionalidades para gerenciamento de:

- 👥 **Clientes** - Cadastro e gerenciamento de clientes
- 🏢 **Fornecedores** - Controle de fornecedores
- 👔 **Funcionários** - Gestão de recursos humanos
- 📦 **Produtos** - Controle de estoque e catálogo
- 🛒 **Pedidos** - Sistema completo de pedidos (venda e compra)
- 🔐 **Autenticação** - Sistema seguro com JWT

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.4** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança e autenticação
- **Spring Validation** - Validação de dados

### Banco de Dados
- **PostgreSQL** - Banco de dados relacional
- **Hibernate** - ORM (Object-Relational Mapping)

### Segurança
- **JWT (JSON Web Tokens)** - Autenticação stateless
- **BCrypt** - Criptografia de senhas

### Documentação
- **SpringDoc OpenAPI 3** - Documentação automática da API
- **Swagger UI** - Interface interativa para testes

### Ferramentas
- **Lombok** - Redução de boilerplate code
- **Maven** - Gerenciamento de dependências

---

## 🏗️ Arquitetura do Sistema

O sistema segue uma arquitetura em camadas (Layered Architecture) com separação clara de responsabilidades:

```
┌─────────────────────────────────────┐
│         Controllers Layer           │  ← Endpoints REST
├─────────────────────────────────────┤
│          Services Layer             │  ← Lógica de negócio
├─────────────────────────────────────┤
│        Repositories Layer           │  ← Acesso aos dados
├─────────────────────────────────────┤
│          Models Layer               │  ← Entidades JPA
└─────────────────────────────────────┘
         ↓
┌─────────────────────────────────────┐
│       PostgreSQL Database           │
└─────────────────────────────────────┘
```

### Estrutura de Pacotes

```
com.apollo.main
├── config/              # Configurações (OpenAPI, etc)
├── controller/          # Controllers REST
├── dto/
│   ├── request/        # DTOs de requisição
│   └── response/       # DTOs de resposta
├── model/              # Entidades JPA
├── repository/         # Repositórios Spring Data
├── security/           # Segurança (JWT, filters)
├── service/            # Lógica de negócio
├── util/               # Utilitários
└── validation/         # Validações customizadas
```

---

## ⚙️ Configuração e Instalação

### Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Passo 1: Clonar o Repositório

```bash
git clone <seu-repositorio>
cd ApolloJava
```

### Passo 2: Configurar o Banco de Dados

1. Criar banco de dados PostgreSQL:

```sql
CREATE DATABASE apollo;
```

2. Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/apollo
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### Passo 3: Compilar e Executar

```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

### Passo 4: Acessar a Aplicação

- **API Base URL**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/v3/api-docs`

---

## 🔐 Autenticação e Segurança

### Sistema JWT (JSON Web Tokens)

O Apollo ERP utiliza autenticação baseada em JWT para proteger os endpoints da API.

#### Como Funciona

1. **Registro/Login** → Recebe token JWT
2. **Token é incluído** no header das requisições
3. **Sistema valida** o token em cada requisição
4. **Acesso concedido** se o token for válido

### Endpoints Públicos (Sem Autenticação)

- `POST /api/auth/register` - Registro de usuário
- `POST /api/auth/login` - Login
- `GET /swagger-ui/**` - Documentação Swagger
- `GET /v3/api-docs/**` - OpenAPI docs
- `GET /ping` - Health check

### Endpoints Protegidos (Requer Autenticação)

Todos os outros endpoints requerem token JWT válido:
- `/clientes/**`
- `/fornecedores/**`
- `/funcionarios/**`
- `/api/produtos/**`
- `/pedidos/**`

### Registro de Usuário

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "username": "admin",
  "senha": "senha123",
  "funcionarioId": 1
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin",
  "usuarioId": 1
}
```

### Login

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "admin",
  "senha": "senha123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin",
  "usuarioId": 1
}
```

### Usando o Token JWT

Incluir o token no header de todas as requisições protegidas:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Exemplo com cURL:**
```bash
curl -X GET http://localhost:8080/clientes \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Expiração:** Os tokens expiram em 24 horas.

---

## 📦 Módulos do Sistema

### 1. 👥 Módulo de Clientes

Gerenciamento completo de clientes com informações pessoais e comerciais.

**Funcionalidades:**
- Cadastro de clientes (PF/PJ)
- Atualização de dados
- Consulta com paginação
- Busca por nome
- Ativação/desativação de clientes
- Exclusão de registros

### 2. 🏢 Módulo de Fornecedores

Controle de fornecedores e suas informações.

**Funcionalidades:**
- Cadastro de fornecedores
- Gerenciamento de dados
- Listagem paginada
- Busca por nome
- Controle de status
- Exclusão de registros

### 3. 👔 Módulo de Funcionários

Gestão de recursos humanos e funcionários.

**Funcionalidades:**
- Cadastro de funcionários
- Atualização de informações
- Listagem com paginação
- Busca por nome
- Vinculação com usuários do sistema

### 4. 📦 Módulo de Produtos

Controle completo de produtos e estoque.

**Funcionalidades:**
- Cadastro de produtos
- Gerenciamento de preços (custo e venda)
- Controle de estoque
- Adição/remoção de estoque
- Busca e listagem
- Exclusão de produtos

### 5. 🛒 Módulo de Pedidos

Sistema de pedidos de venda e compra.

**Funcionalidades:**
- Criação de pedidos
- Múltiplos produtos por pedido
- Cálculo automático de totais
- Controle de status
- Tipos de pedido (venda/compra)
- Vinculação com cliente/fornecedor
- Registro de funcionário responsável

### 6. 🔐 Módulo de Autenticação

Sistema seguro de autenticação e autorização.

**Funcionalidades:**
- Registro de usuários
- Login com credenciais
- Geração de tokens JWT
- Validação de tokens
- Proteção de endpoints

---

## 🌐 API Endpoints

### Autenticação

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/api/auth/register` | Registrar novo usuário | Não |
| POST | `/api/auth/login` | Fazer login | Não |

### Clientes

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/clientes` | Criar cliente | Sim |
| GET | `/clientes` | Listar todos (paginado) | Sim |
| GET | `/clientes/{id}` | Buscar por ID | Sim |
| GET | `/clientes/buscar?nome={nome}` | Buscar por nome | Sim |
| PUT | `/clientes/{id}` | Atualizar cliente | Sim |
| PUT | `/clientes/status/{id}` | Alternar status | Sim |
| DELETE | `/clientes/{id}` | Excluir cliente | Sim |

### Fornecedores

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/fornecedores` | Criar fornecedor | Sim |
| GET | `/fornecedores` | Listar todos (paginado) | Sim |
| GET | `/fornecedores/{id}` | Buscar por ID | Sim |
| GET | `/fornecedores/buscar?nome={nome}` | Buscar por nome | Sim |
| PUT | `/fornecedores/{id}` | Atualizar fornecedor | Sim |
| PUT | `/fornecedores/status/{id}` | Alternar status | Sim |
| DELETE | `/fornecedores/{id}` | Excluir fornecedor | Sim |

### Funcionários

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/funcionarios` | Criar funcionário | Sim |
| GET | `/funcionarios` | Listar todos (paginado) | Sim |
| GET | `/funcionarios/{id}` | Buscar por ID | Sim |
| PUT | `/funcionarios/{id}` | Atualizar funcionário | Sim |

### Produtos

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/api/produtos` | Criar produto | Sim |
| GET | `/api/produtos` | Listar todos (paginado) | Sim |
| GET | `/api/produtos/{id}` | Buscar por ID | Sim |
| GET | `/api/produtos/buscar?nome={nome}` | Buscar por nome | Sim |
| PUT | `/api/produtos/{id}` | Atualizar produto | Sim |
| PUT | `/api/produtos/add/{id}` | Adicionar estoque | Sim |
| PUT | `/api/produtos/sub/{id}` | Remover estoque | Sim |
| DELETE | `/api/produtos/{id}` | Excluir produto | Sim |

### Pedidos

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/pedidos` | Criar pedido | Sim |
| GET | `/pedidos` | Listar todos (paginado) | Sim |

---

## 📊 Modelos de Dados

### Pessoa (Classe Abstrata)

Base para Cliente, Fornecedor e Funcionário.

```java
{
  "idPessoa": Long,
  "status": "ATIVO" | "INATIVO",
  "nome": String,
  "categoria": String,
  "tipoPessoa": "FISICA" | "JURIDICA",
  "cpfcnpj": String,
  "ie": String,
  "email": String,
  "telefone": Integer,
  "endereco": String,
  "bairro": String,
  "cidade": String,
  "uf": String,
  "cep": Integer,
  "dataCadastro": DateTime
}
```

### Cliente

```java
{
  // ... herda campos de Pessoa
  "genero": String
}
```

### Fornecedor

```java
{
  // ... herda campos de Pessoa
}
```

### Funcionário

```java
{
  // ... herda campos de Pessoa
  // campos específicos de funcionário
}
```

### Produto

```java
{
  "id": Long,
  "status": "ATIVO" | "INATIVO",
  "nome": String,
  "descricao": String,
  "qntdEstoque": Integer,
  "precoCusto": BigDecimal,
  "precoVenda": BigDecimal
}
```

### Pedido

```java
{
  "idPedido": Long,
  "status": "ABERTO" | "APROVADO" | "CANCELADO" | "FINALIZADO",
  "tipo": "VENDA" | "COMPRA",
  "dataEmissao": DateTime,
  "vencimento": DateTime,
  "totalCusto": BigDecimal,
  "totalVenda": BigDecimal,
  "formaPagamento": String,
  "pessoa": Pessoa,
  "funcionario": Funcionario,
  "itens": [PedidoProduto]
}
```

### PedidoProduto

```java
{
  "produto": Produto,
  "quantidade": Integer,
  "precoUnitario": BigDecimal,
  "subtotal": BigDecimal
}
```

### Usuário

```java
{
  "id": Long,
  "username": String,
  "senha": String (criptografada),
  "funcionario": Funcionario
}
```

---

## 💡 Exemplos de Uso

### 1. Criar um Cliente

```bash
POST /clientes
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "nome": "João Silva",
  "tipoPessoa": "FISICA",
  "cpfcnpj": "12345678900",
  "email": "joao@email.com",
  "telefone": 11999999999,
  "endereco": "Rua A, 123",
  "bairro": "Centro",
  "cidade": "São Paulo",
  "uf": "SP",
  "cep": 12345678,
  "genero": "M"
}
```

### 2. Criar um Produto

```bash
POST /api/produtos
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "nome": "Notebook Dell",
  "descricao": "Notebook i7 16GB RAM",
  "qntdEstoque": 10,
  "precoCusto": 2500.00,
  "precoVenda": 3500.00
}
```

### 3. Adicionar Estoque a um Produto

```bash
PUT /api/produtos/add/1
Authorization: Bearer {seu_token}
Content-Type: application/json

20
```

### 4. Criar um Pedido de Venda

```bash
POST /pedidos
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "tipo": "VENDA",
  "pessoaId": 1,
  "funcionarioId": 1,
  "formaPagamento": "CARTAO_CREDITO",
  "vencimento": "2025-11-21T00:00:00",
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2,
      "precoUnitario": 3500.00
    },
    {
      "produtoId": 2,
      "quantidade": 1,
      "precoUnitario": 1200.00
    }
  ]
}
```

### 5. Listar Clientes com Paginação

```bash
GET /clientes?page=0&size=10
Authorization: Bearer {seu_token}
```

### 6. Buscar Produtos por Nome

```bash
GET /api/produtos/buscar?nome=Notebook&page=0&size=10
Authorization: Bearer {seu_token}
```

### 7. Atualizar Status de Fornecedor

```bash
PUT /fornecedores/status/1
Authorization: Bearer {seu_token}
```

---

## 🧪 Testando a API

### Opção 1: Swagger UI (Recomendado)

1. Acesse: `http://localhost:8080/swagger-ui.html`

2. **Autentique-se:**
   - Execute `/api/auth/login` ou `/api/auth/register`
   - Copie o valor do campo `token`
   - Clique no botão **"Authorize"** (🔒) no topo
   - Cole o token (sem "Bearer")
   - Clique em **"Authorize"** e depois **"Close"**

3. **Teste os endpoints:**
   - Todos os endpoints terão um cadeado fechado 🔒
   - Clique em qualquer endpoint
   - Clique em "Try it out"
   - Preencha os parâmetros
   - Clique em "Execute"

### Opção 2: Postman

1. **Criar uma Collection**

2. **Configurar Autenticação:**
   - Na aba "Authorization"
   - Type: Bearer Token
   - Token: {seu_token_jwt}

3. **Criar Requisições:**
   - Importe os endpoints da documentação
   - Configure headers e body conforme exemplos

### Opção 3: cURL

```bash
# 1. Fazer login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"senha123"}'

# 2. Usar o token nas requisições
curl -X GET http://localhost:8080/clientes \
  -H "Authorization: Bearer {seu_token}"
```

---

## 📝 Configurações Importantes

### application.properties

```properties
# Aplicação
spring.application.name=main

# Banco de Dados PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/apollo
spring.datasource.username=postgres
spring.datasource.password=rootpassword
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# JWT
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000  # 24 horas

# SpringDoc/OpenAPI
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.tryItOutEnabled=true
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
```

---

## 🔧 Manutenção e Boas Práticas

### Logs

O sistema exibe SQL formatado no console para debug:
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Validação de Dados

Todas as entradas são validadas com Bean Validation:
- `@NotNull` - Campo obrigatório
- `@NotBlank` - String não vazia
- `@Valid` - Validação em cascata

### Tratamento de Erros

Os controllers capturam exceções e retornam:
- `200 OK` - Sucesso
- `201 Created` - Recurso criado
- `400 Bad Request` - Erro de validação
- `401 Unauthorized` - Não autenticado
- `500 Internal Server Error` - Erro interno

### Paginação

Todos os endpoints de listagem suportam paginação:
- `page` - Número da página (padrão: 0)
- `size` - Tamanho da página (padrão: 10)

---

## 🚀 Roadmap / Melhorias Futuras

- [ ] Implementar roles e permissões (ADMIN, USER, etc)
- [ ] Adicionar relatórios e dashboards
- [ ] Implementar upload de imagens para produtos
- [ ] Adicionar histórico de alterações
- [ ] Implementar notificações
- [ ] Criar módulo de faturamento
- [ ] Adicionar integração com APIs de pagamento
- [ ] Implementar backup automático
- [ ] Adicionar logs de auditoria
- [ ] Criar testes unitários e de integração
- [ ] Dockerizar a aplicação

---

## 📄 Licença

Este projeto é parte do sistema Apollo ERP.

---

## 👨‍💻 Suporte

Para dúvidas ou problemas:
1. Consulte a documentação do Swagger
2. Verifique os logs da aplicação
3. Revise as configurações do `application.properties`

---

## 📚 Documentação Adicional

- [Guia de Autenticação](AUTHENTICATION_README.md)
- [API Docs (OpenAPI)](http://localhost:8080/v3/api-docs)
- [Swagger UI](http://localhost:8080/swagger-ui.html)

---

**Desenvolvido com ☕ Java e ❤️**

