# 🏗️ Apollo ERP - Arquitetura e Guia de Desenvolvimento

## Índice
- [Visão Geral da Arquitetura](#visão-geral-da-arquitetura)
- [Camadas da Aplicação](#camadas-da-aplicação)
- [Padrões de Design](#padrões-de-design)
- [Estrutura de Pacotes](#estrutura-de-pacotes)
- [Banco de Dados](#banco-de-dados)
- [Segurança](#segurança)
- [Boas Práticas](#boas-práticas)
- [Como Contribuir](#como-contribuir)

---

## 🎯 Visão Geral da Arquitetura

O Apollo ERP foi desenvolvido seguindo uma arquitetura em camadas (Layered Architecture) com princípios de Clean Architecture e Domain-Driven Design (DDD).

### Princípios Fundamentais

1. **Separação de Responsabilidades** - Cada camada tem uma função específica
2. **Inversão de Dependência** - Dependências apontam para abstrações
3. **Baixo Acoplamento** - Componentes independentes e desacoplados
4. **Alta Coesão** - Funcionalidades relacionadas agrupadas
5. **SOLID Principles** - Código limpo e manutenível

### Diagrama da Arquitetura

```
┌─────────────────────────────────────────────────┐
│              CLIENTE (Frontend)                  │
└──────────────────┬──────────────────────────────┘
                   │ HTTP/REST
                   ▼
┌─────────────────────────────────────────────────┐
│           SECURITY LAYER (JWT Filter)           │
└──────────────────┬──────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────┐
│         CONTROLLER LAYER (REST APIs)            │
│  - AuthController                               │
│  - ClienteController                            │
│  - FornecedorController                         │
│  - FuncionarioController                        │
│  - ProdutoController                            │
│  - PedidoController                             │
└──────────────────┬──────────────────────────────┘
                   │ DTOs
                   ▼
┌─────────────────────────────────────────────────┐
│          SERVICE LAYER (Business Logic)         │
│  - AuthService                                  │
│  - ClienteService                               │
│  - FornecedorService                            │
│  - FuncionarioService                           │
│  - ProdutoService                               │
│  - PedidoService                                │
└──────────────────┬──────────────────────────────┘
                   │ Entities
                   ▼
┌─────────────────────────────────────────────────┐
│       REPOSITORY LAYER (Data Access)            │
│  - ClienteRepository                            │
│  - FornecedorRepository                         │
│  - FuncionarioRepository                        │
│  - ProdutoRepository                            │
│  - PedidoRepository                             │
│  - UsuarioRepository                            │
└──────────────────┬──────────────────────────────┘
                   │ JPA/Hibernate
                   ▼
┌─────────────────────────────────────────────────┐
│            DATABASE (PostgreSQL)                │
└─────────────────────────────────────────────────┘
```

---

## 📚 Camadas da Aplicação

### 1. Controller Layer (Camada de Apresentação)

**Responsabilidade:** Receber requisições HTTP e retornar respostas

**Características:**
- Endpoints REST (@RestController)
- Mapeamento de URLs (@RequestMapping)
- Validação de entrada (@Valid)
- Tratamento de exceções (try-catch)
- Conversão de DTOs

**Exemplo:**
```java
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ClienteRequestDTO dto) {
        try {
            return ResponseEntity.ok(clienteService.create(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
```

**Boas Práticas:**
- Não contém lógica de negócio
- Delega processamento para a camada de serviço
- Retorna ResponseEntity com status HTTP apropriado
- Usa DTOs para entrada e saída

---

### 2. Service Layer (Camada de Negócio)

**Responsabilidade:** Implementar regras de negócio e orquestrar operações

**Características:**
- Lógica de negócio (@Service)
- Transações (@Transactional)
- Validações de domínio
- Conversão entre DTOs e Entities
- Orquestração de múltiplos repositórios

**Exemplo:**
```java
@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Transactional
    public ProdutoResponseDTO create(ProdutoRequestDTO dto) {
        // Validações de negócio
        if (dto.getPrecoVenda() < dto.getPrecoCusto()) {
            throw new IllegalArgumentException("Preço de venda menor que custo");
        }
        
        // Criar entidade
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setStatus(StatusAtivo.ATIVO);
        // ... outros campos
        
        // Salvar
        Produto saved = produtoRepository.save(produto);
        
        // Converter para DTO de resposta
        return toResponseDTO(saved);
    }
}
```

**Boas Práticas:**
- Contém toda a lógica de negócio
- Usa @Transactional para operações que modificam dados
- Valida dados antes de persistir
- Lança exceções significativas

---

### 3. Repository Layer (Camada de Dados)

**Responsabilidade:** Acesso e persistência de dados

**Características:**
- Interfaces que estendem JpaRepository
- Queries customizadas (@Query)
- Métodos de busca derivados (findBy...)
- Paginação e ordenação

**Exemplo:**
```java
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Page<Cliente> findByNomeContainingIgnoreCase(
        String nome, 
        Pageable pageable
    );
    
    Optional<Cliente> findByCpfcnpj(String cpfcnpj);
    
    @Query("SELECT c FROM Cliente c WHERE c.status = :status")
    List<Cliente> findByStatus(@Param("status") StatusAtivo status);
}
```

**Boas Práticas:**
- Apenas operações de dados
- Sem lógica de negócio
- Uso de Optional para evitar null
- Queries otimizadas

---

### 4. Model Layer (Camada de Domínio)

**Responsabilidade:** Representar entidades do domínio

**Características:**
- Entidades JPA (@Entity)
- Mapeamento objeto-relacional
- Relacionamentos (@OneToMany, @ManyToOne)
- Herança (@Inheritance)

**Exemplo:**
```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPessoa;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusAtivo status;
    
    @Column(nullable = false, length = 45)
    private String nome;
    
    // ... outros campos
}
```

---

### 5. DTO Layer (Camada de Transferência)

**Responsabilidade:** Transferir dados entre camadas

**Tipos de DTOs:**

**Request DTOs:**
```java
public class ProdutoRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Preço de custo é obrigatório")
    @DecimalMin(value = "0.01")
    private BigDecimal precoCusto;
    
    // ... getters e setters
}
```

**Response DTOs:**
```java
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private StatusAtivo status;
    private BigDecimal precoVenda;
    
    // ... getters e setters
}
```

**Vantagens:**
- Desacoplamento entre camadas
- Controle sobre dados expostos
- Validação centralizada
- Versionamento de API facilitado

---

## 🎨 Padrões de Design

### 1. Repository Pattern

**Objetivo:** Abstração da camada de persistência

```java
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Spring Data JPA implementa automaticamente
}
```

### 2. Service Layer Pattern

**Objetivo:** Encapsular lógica de negócio

```java
@Service
public class ProdutoService {
    // Lógica de negócio centralizada
}
```

### 3. DTO Pattern

**Objetivo:** Transferência segura de dados

```java
// Request DTO - Entrada
public class ClienteRequestDTO { }

// Response DTO - Saída
public class ClienteResponseDTO { }
```

### 4. Dependency Injection

**Objetivo:** Inversão de controle

```java
@Autowired
public ClienteController(ClienteService clienteService) {
    this.clienteService = clienteService;
}
```

### 5. Strategy Pattern (Herança)

**Objetivo:** Compartilhar comportamento comum

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa {
    // Campos comuns
}

@Entity
public class Cliente extends Pessoa {
    // Campos específicos
}
```

---

## 📦 Estrutura de Pacotes

```
com.apollo.main
│
├── config/                    # Configurações
│   └── OpenApiConfig.java    # Configuração do Swagger
│
├── controller/                # Controllers REST
│   ├── AuthController.java
│   ├── ClienteController.java
│   ├── FornecedorController.java
│   ├── FuncionarioController.java
│   ├── PedidoController.java
│   ├── PessoaController.java
│   └── ProdutoController.java
│
├── dto/                       # Data Transfer Objects
│   ├── request/              # DTOs de entrada
│   │   ├── ClienteRequestDTO.java
│   │   ├── FornecedorRequestDTO.java
│   │   ├── FuncionarioRequestDTO.java
│   │   ├── PedidoRequestDTO.java
│   │   ├── ProdutoRequestDTO.java
│   │   ├── LoginRequest.java
│   │   └── RegisterRequest.java
│   │
│   └── response/             # DTOs de saída
│       ├── ClienteResponseDTO.java
│       ├── ProdutoResponseDTO.java
│       ├── PedidoResponseDTO.java
│       └── AuthResponse.java
│
├── model/                     # Entidades JPA
│   ├── Pessoa.java           # Classe abstrata
│   ├── Cliente.java
│   ├── Fornecedor.java
│   ├── Funcionario.java
│   ├── Produto.java
│   ├── Pedido.java
│   ├── PedidoProduto.java
│   ├── Usuario.java
│   ├── StatusAtivo.java      # Enum
│   ├── StatusPedido.java     # Enum
│   ├── TipoPedido.java       # Enum
│   └── TipoPessoa.java       # Enum
│
├── repository/                # Repositórios Spring Data
│   ├── ClienteRepository.java
│   ├── FornecedorRepository.java
│   ├── FuncionarioRepository.java
│   ├── PedidoRepository.java
│   ├── PessoaRepository.java
│   ├── ProdutoRepository.java
│   └── UsuarioRepository.java
│
├── security/                  # Segurança
│   ├── JwtUtil.java          # Utilitário JWT
│   ├── JwtAuthenticationFilter.java
│   ├── CustomUserDetailsService.java
│   └── SecurityConfig.java   # Configuração Spring Security
│
├── service/                   # Serviços (Lógica de negócio)
│   ├── AuthService.java
│   ├── ClienteService.java
│   ├── FornecedorService.java
│   ├── FuncionarioService.java
│   ├── PedidoService.java
│   ├── PessoaService.java
│   └── ProdutoService.java
│
├── util/                      # Utilitários
│   └── PingController.java   # Health check
│
├── validation/                # Validações customizadas
│
└── MainApplication.java       # Classe principal
```

---

## 🗄️ Banco de Dados

### Modelo de Dados

#### Tabela: pessoa (Superclasse)

```sql
CREATE TABLE pessoa (
    id_pessoa SERIAL PRIMARY KEY,
    status VARCHAR(45) NOT NULL,
    nome VARCHAR(45) NOT NULL,
    categoria VARCHAR(45) NOT NULL,
    tipo_pessoa VARCHAR(45) NOT NULL,
    cpfcnpj VARCHAR(45) NOT NULL UNIQUE,
    ie VARCHAR(45),
    email VARCHAR(45),
    telefone INTEGER,
    endereco VARCHAR(45) NOT NULL,
    bairro VARCHAR(45) NOT NULL,
    cidade VARCHAR(45) NOT NULL,
    uf VARCHAR(45) NOT NULL,
    cep INTEGER NOT NULL,
    data_cadastro TIMESTAMP NOT NULL
);
```

#### Tabela: cliente

```sql
CREATE TABLE cliente (
    id_pessoa BIGINT PRIMARY KEY REFERENCES pessoa(id_pessoa),
    genero VARCHAR(45)
);
```

#### Tabela: fornecedor

```sql
CREATE TABLE fornecedor (
    id_pessoa BIGINT PRIMARY KEY REFERENCES pessoa(id_pessoa)
);
```

#### Tabela: funcionario

```sql
CREATE TABLE funcionario (
    id_pessoa BIGINT PRIMARY KEY REFERENCES pessoa(id_pessoa)
    -- campos específicos de funcionário
);
```

#### Tabela: produto

```sql
CREATE TABLE produto (
    id SERIAL PRIMARY KEY,
    status VARCHAR(45) NOT NULL,
    nome VARCHAR(45) NOT NULL,
    descricao VARCHAR(45) NOT NULL,
    qntd_estoque INTEGER NOT NULL,
    preco_custo NUMERIC(10,2) NOT NULL,
    preco_venda NUMERIC(10,2) NOT NULL
);
```

#### Tabela: pedido

```sql
CREATE TABLE pedido (
    id_pedido SERIAL PRIMARY KEY,
    status VARCHAR(45) NOT NULL,
    tipo VARCHAR(45) NOT NULL,
    data_emissao TIMESTAMP NOT NULL,
    vencimento TIMESTAMP,
    total_custo NUMERIC(10,2),
    total_venda NUMERIC(10,2),
    forma_pagamento VARCHAR(255),
    id_pessoa BIGINT REFERENCES pessoa(id_pessoa),
    id_funcionario BIGINT REFERENCES funcionario(id_pessoa)
);
```

#### Tabela: pedido_produto

```sql
CREATE TABLE pedido_produto (
    id SERIAL PRIMARY KEY,
    id_pedido BIGINT REFERENCES pedido(id_pedido),
    id_produto BIGINT REFERENCES produto(id),
    quantidade INTEGER NOT NULL,
    preco_unitario NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL
);
```

#### Tabela: usuario

```sql
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    funcionario_id BIGINT REFERENCES funcionario(id_pessoa)
);
```

### Relacionamentos

- **Pessoa** ← Cliente (1:1, Herança)
- **Pessoa** ← Fornecedor (1:1, Herança)
- **Pessoa** ← Funcionario (1:1, Herança)
- **Pedido** → Pessoa (N:1, ManyToOne)
- **Pedido** → Funcionario (N:1, ManyToOne)
- **Pedido** → PedidoProduto (1:N, OneToMany)
- **PedidoProduto** → Produto (N:1, ManyToOne)
- **Usuario** → Funcionario (1:1, OneToOne)

---

## 🔐 Segurança

### Fluxo de Autenticação

```
1. Cliente → POST /api/auth/login {username, senha}
2. AuthService valida credenciais
3. Se válido, JwtUtil gera token JWT
4. Token é retornado ao cliente
5. Cliente inclui token em requisições: Authorization: Bearer {token}
6. JwtAuthenticationFilter intercepta requisições
7. JwtUtil valida o token
8. Se válido, usuário é autenticado
9. Requisição prossegue para o controller
```

### Componentes de Segurança

#### 1. JwtUtil

Responsabilidades:
- Gerar tokens JWT
- Validar tokens
- Extrair informações (username, expiração)

#### 2. JwtAuthenticationFilter

Responsabilidades:
- Interceptar todas as requisições
- Extrair token do header Authorization
- Validar token
- Autenticar usuário no contexto do Spring Security

#### 3. SecurityConfig

Responsabilidades:
- Configurar endpoints públicos/protegidos
- Configurar filtros de segurança
- Definir política de CORS
- Configurar BCrypt para senhas

#### 4. CustomUserDetailsService

Responsabilidades:
- Carregar usuário do banco de dados
- Integração com Spring Security

### Configuração de Segurança

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        return http.build();
    }
}
```

---

## ✅ Boas Práticas

### 1. Código Limpo

- Nomes descritivos para variáveis e métodos
- Métodos pequenos e focados (Single Responsibility)
- Evitar código duplicado (DRY - Don't Repeat Yourself)
- Comentários apenas quando necessário

### 2. Validação

- Validação na camada de controller (@Valid)
- Validação de negócio na camada de service
- Mensagens de erro claras e descritivas

### 3. Tratamento de Exceções

- Capturar exceções específicas
- Retornar mensagens significativas
- Usar status HTTP apropriados

### 4. Transações

- Usar @Transactional em métodos que modificam dados
- Rollback automático em caso de erro

### 5. Paginação

- Sempre paginar listagens grandes
- Tamanho padrão: 10-20 itens
- Máximo recomendado: 100 itens

### 6. Performance

- Usar índices no banco de dados
- Evitar N+1 queries (usar JOIN FETCH)
- Cache quando apropriado

### 7. Segurança

- Nunca logar senhas
- Validar todas as entradas
- Usar HTTPS em produção
- Tokens com expiração

---

## 🚀 Como Contribuir

### Adicionando um Novo Módulo

#### Passo 1: Criar a Entidade

```java
@Entity
public class NovaEntidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // campos...
}
```

#### Passo 2: Criar o Repository

```java
@Repository
public interface NovaEntidadeRepository 
    extends JpaRepository<NovaEntidade, Long> {
}
```

#### Passo 3: Criar DTOs

```java
// Request DTO
public class NovaEntidadeRequestDTO {
    @NotBlank
    private String campo;
}

// Response DTO
public class NovaEntidadeResponseDTO {
    private Long id;
    private String campo;
}
```

#### Passo 4: Criar o Service

```java
@Service
public class NovaEntidadeService {
    @Autowired
    private NovaEntidadeRepository repository;
    
    public NovaEntidadeResponseDTO create(NovaEntidadeRequestDTO dto) {
        // implementação
    }
}
```

#### Passo 5: Criar o Controller

```java
@RestController
@RequestMapping("/novas-entidades")
@Tag(name = "Nova Entidade")
public class NovaEntidadeController {
    @Autowired
    private NovaEntidadeService service;
    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NovaEntidadeRequestDTO dto) {
        // implementação
    }
}
```

---

**Documentação Técnica - Apollo ERP v1.0**

