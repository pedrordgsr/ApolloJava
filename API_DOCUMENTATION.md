# 📘 Apollo ERP - Documentação Completa da API

## Índice
- [Autenticação](#autenticação)
- [Clientes](#clientes)
- [Fornecedores](#fornecedores)
- [Funcionários](#funcionários)
- [Produtos](#produtos)
- [Pedidos](#pedidos)
- [Códigos de Status HTTP](#códigos-de-status-http)
- [Tratamento de Erros](#tratamento-de-erros)

---

## 🔐 Autenticação

### Registro de Usuário

**Endpoint:** `POST /api/auth/register`

**Descrição:** Cria um novo usuário no sistema.

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "username": "string",
  "senha": "string",
  "funcionarioId": long (opcional)
}
```

**Response Success (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin",
  "usuarioId": 1
}
```

**Validações:**
- `username`: Obrigatório, único
- `senha`: Obrigatório, mínimo 6 caracteres
- `funcionarioId`: Opcional, deve existir no banco

---

### Login

**Endpoint:** `POST /api/auth/login`

**Descrição:** Autentica um usuário e retorna um token JWT.

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "username": "string",
  "senha": "string"
}
```

**Response Success (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin",
  "usuarioId": 1
}
```

**Response Error (401):**
```json
{
  "message": "Credenciais inválidas"
}
```

---

## 👥 Clientes

### Criar Cliente

**Endpoint:** `POST /clientes`

**Autenticação:** Requerida

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "nome": "João Silva",
  "tipoPessoa": "FISICA",
  "cpfcnpj": "12345678900",
  "ie": "123456789",
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

**Campos:**
- `tipoPessoa`: "FISICA" ou "JURIDICA"
- `cpfcnpj`: CPF (11 dígitos) ou CNPJ (14 dígitos)
- `uf`: Sigla do estado (2 caracteres)
- `genero`: Específico para clientes

**Response Success (200):**
```json
{
  "idPessoa": 1,
  "status": "ATIVO",
  "nome": "João Silva",
  "categoria": "Cliente",
  "tipoPessoa": "FISICA",
  "cpfcnpj": "12345678900",
  "email": "joao@email.com",
  "telefone": 11999999999,
  "endereco": "Rua A, 123",
  "bairro": "Centro",
  "cidade": "São Paulo",
  "uf": "SP",
  "cep": 12345678,
  "dataCadastro": "2025-10-21T10:30:00",
  "genero": "M"
}
```

---

### Listar Todos os Clientes

**Endpoint:** `GET /clientes`

**Autenticação:** Requerida

**Query Parameters:**
- `page`: Número da página (padrão: 0)
- `size`: Tamanho da página (padrão: 10)

**Exemplo:**
```
GET /clientes?page=0&size=20
```

**Response Success (200):**
```json
{
  "content": [
    {
      "idPessoa": 1,
      "nome": "João Silva",
      "email": "joao@email.com",
      "status": "ATIVO",
      ...
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 5,
  "totalElements": 50,
  "last": false,
  "first": true
}
```

---

### Buscar Cliente por ID

**Endpoint:** `GET /clientes/{id}`

**Autenticação:** Requerida

**Path Parameters:**
- `id`: ID do cliente

**Response Success (200):**
```json
{
  "idPessoa": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  ...
}
```

**Response Error (400):**
```json
{
  "message": "Cliente não encontrado"
}
```

---

### Buscar Clientes por Nome

**Endpoint:** `GET /clientes/buscar`

**Autenticação:** Requerida

**Query Parameters:**
- `nome`: Nome do cliente (busca parcial)
- `page`: Número da página (padrão: 0)
- `size`: Tamanho da página (padrão: 10)

**Exemplo:**
```
GET /clientes/buscar?nome=João&page=0&size=10
```

---

### Atualizar Cliente

**Endpoint:** `PUT /clientes/{id}`

**Autenticação:** Requerida

**Path Parameters:**
- `id`: ID do cliente

**Request Body:** Mesmo formato do POST

**Response Success (200):** Cliente atualizado

---

### Alternar Status do Cliente

**Endpoint:** `PUT /clientes/status/{id}`

**Autenticação:** Requerida

**Descrição:** Alterna o status entre ATIVO e INATIVO

**Response Success (200):**
```json
{
  "idPessoa": 1,
  "status": "INATIVO",
  ...
}
```

---

### Deletar Cliente

**Endpoint:** `DELETE /clientes/{id}`

**Autenticação:** Requerida

**Response Success (200):**
```json
{
  "message": "Cliente deletado com sucesso"
}
```

---

## 🏢 Fornecedores

Os endpoints de fornecedores seguem a mesma estrutura dos clientes:

- `POST /fornecedores` - Criar fornecedor
- `GET /fornecedores` - Listar todos (paginado)
- `GET /fornecedores/{id}` - Buscar por ID
- `GET /fornecedores/buscar?nome={nome}` - Buscar por nome
- `PUT /fornecedores/{id}` - Atualizar
- `PUT /fornecedores/status/{id}` - Alternar status
- `DELETE /fornecedores/{id}` - Deletar

**Diferenças:**
- Fornecedores não possuem o campo `genero`
- A categoria é automaticamente definida como "Fornecedor"

---

## 👔 Funcionários

### Criar Funcionário

**Endpoint:** `POST /funcionarios`

**Request Body:**
```json
{
  "nome": "Maria Santos",
  "tipoPessoa": "FISICA",
  "cpfcnpj": "98765432100",
  "email": "maria@empresa.com",
  "telefone": 11988888888,
  "endereco": "Av. B, 456",
  "bairro": "Jardins",
  "cidade": "São Paulo",
  "uf": "SP",
  "cep": 12345678
}
```

### Outros Endpoints:

- `GET /funcionarios` - Listar todos (paginado)
- `GET /funcionarios/{id}` - Buscar por ID
- `PUT /funcionarios/{id}` - Atualizar

---

## 📦 Produtos

### Criar Produto

**Endpoint:** `POST /api/produtos`

**Autenticação:** Requerida

**Request Body:**
```json
{
  "nome": "Notebook Dell Inspiron",
  "descricao": "Notebook i7 11ª geração, 16GB RAM, 512GB SSD",
  "qntdEstoque": 10,
  "precoCusto": 2500.00,
  "precoVenda": 3500.00
}
```

**Validações:**
- `nome`: Obrigatório, máximo 45 caracteres
- `descricao`: Obrigatório, máximo 45 caracteres
- `qntdEstoque`: Obrigatório, inteiro positivo
- `precoCusto`: Obrigatório, decimal (10,2)
- `precoVenda`: Obrigatório, decimal (10,2)

**Response Success (201):**
```json
{
  "id": 1,
  "status": "ATIVO",
  "nome": "Notebook Dell Inspiron",
  "descricao": "Notebook i7 11ª geração, 16GB RAM, 512GB SSD",
  "qntdEstoque": 10,
  "precoCusto": 2500.00,
  "precoVenda": 3500.00
}
```

---

### Listar Todos os Produtos

**Endpoint:** `GET /api/produtos`

**Query Parameters:**
- `page`: 0
- `size`: 10

**Response:** Página de produtos

---

### Buscar Produto por ID

**Endpoint:** `GET /api/produtos/{id}`

---

### Buscar Produtos por Nome

**Endpoint:** `GET /api/produtos/buscar?nome={nome}&page=0&size=10`

---

### Atualizar Produto

**Endpoint:** `PUT /api/produtos/{id}`

**Request Body:** Mesmo formato do POST

---

### Adicionar Estoque

**Endpoint:** `PUT /api/produtos/add/{id}`

**Request Body:**
```json
50
```

**Descrição:** Adiciona quantidade ao estoque atual

**Response Success (200):**
```json
"Estoque adicionado com sucesso"
```

---

### Remover Estoque

**Endpoint:** `PUT /api/produtos/sub/{id}`

**Request Body:**
```json
10
```

**Descrição:** Remove quantidade do estoque atual

**Validação:** Não permite estoque negativo

---

### Deletar Produto

**Endpoint:** `DELETE /api/produtos/{id}`

**Response Success (200):**
```json
"Produto 1 Deletado!"
```

---

## 🛒 Pedidos

### Criar Pedido

**Endpoint:** `POST /pedidos`

**Autenticação:** Requerida

**Request Body:**
```json
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

**Campos:**
- `tipo`: "VENDA" ou "COMPRA"
- `pessoaId`: ID do cliente (venda) ou fornecedor (compra)
- `funcionarioId`: ID do funcionário responsável
- `formaPagamento`: String livre
- `vencimento`: Data/hora de vencimento (opcional)
- `itens`: Array de produtos do pedido

**Item do Pedido:**
- `produtoId`: ID do produto
- `quantidade`: Quantidade desejada
- `precoUnitario`: Preço unitário do produto

**Processamento Automático:**
- Status inicial: "ABERTO"
- `dataEmissao` é definida automaticamente
- `totalCusto` e `totalVenda` são calculados automaticamente
- `subtotal` de cada item é calculado (quantidade × preço)

**Response Success (200):**
```json
{
  "idPedido": 1,
  "status": "ABERTO",
  "tipo": "VENDA",
  "dataEmissao": "2025-10-21T10:30:00",
  "vencimento": "2025-11-21T00:00:00",
  "totalCusto": 5000.00,
  "totalVenda": 8200.00,
  "formaPagamento": "CARTAO_CREDITO",
  "pessoa": {
    "idPessoa": 1,
    "nome": "João Silva"
  },
  "funcionario": {
    "idPessoa": 1,
    "nome": "Maria Santos"
  },
  "itens": [
    {
      "produto": {
        "id": 1,
        "nome": "Notebook Dell"
      },
      "quantidade": 2,
      "precoUnitario": 3500.00,
      "subtotal": 7000.00
    },
    {
      "produto": {
        "id": 2,
        "nome": "Mouse Wireless"
      },
      "quantidade": 1,
      "precoUnitario": 1200.00,
      "subtotal": 1200.00
    }
  ]
}
```

**Response Error (400):**
```json
{
  "message": "Produto não encontrado"
}
```

---

### Listar Todos os Pedidos

**Endpoint:** `GET /pedidos`

**Autenticação:** Requerida

**Query Parameters:**
- `page`: 0
- `size`: 10

**Response Success (200):**
```json
{
  "content": [
    {
      "idPedido": 1,
      "tipo": "VENDA",
      "status": "ABERTO",
      "totalVenda": 8200.00,
      ...
    }
  ],
  "totalPages": 3,
  "totalElements": 25
}
```

---

## 📊 Códigos de Status HTTP

| Código | Descrição | Quando Ocorre |
|--------|-----------|---------------|
| 200 | OK | Requisição bem-sucedida |
| 201 | Created | Recurso criado com sucesso |
| 400 | Bad Request | Erro de validação ou dados inválidos |
| 401 | Unauthorized | Token JWT inválido ou ausente |
| 403 | Forbidden | Acesso negado |
| 404 | Not Found | Recurso não encontrado |
| 500 | Internal Server Error | Erro interno do servidor |

---

## ⚠️ Tratamento de Erros

### Formato de Erro Padrão

```json
{
  "message": "Descrição do erro"
}
```

### Erros Comuns

**1. Token JWT Inválido:**
```json
{
  "message": "Token JWT inválido ou expirado"
}
```

**2. Validação de Campos:**
```json
{
  "message": "O campo 'nome' é obrigatório"
}
```

**3. Recurso Não Encontrado:**
```json
{
  "message": "Cliente não encontrado"
}
```

**4. Erro de Negócio:**
```json
{
  "message": "Estoque insuficiente para o produto"
}
```

---

## 🔍 Dicas de Uso

### Paginação

Todos os endpoints de listagem suportam paginação:

```
GET /endpoint?page=0&size=10
```

- Primeira página: `page=0`
- Tamanho recomendado: 10-50 itens
- Máximo sugerido: 100 itens por página

### Busca por Nome

As buscas por nome são case-insensitive e parciais:

```
GET /clientes/buscar?nome=joão
```

Encontrará: "João", "joão", "JOÃO SILVA", etc.

### Autenticação

Sempre inclua o token JWT:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Token expira em 24 horas. Faça login novamente após expiração.

### Datas

Formato: ISO 8601
```
2025-10-21T10:30:00
```

---

## 📝 Exemplos Práticos

### Fluxo Completo de Venda

```bash
# 1. Login
POST /api/auth/login
{"username": "admin", "senha": "senha123"}

# 2. Criar Cliente
POST /clientes
{...dados do cliente...}

# 3. Criar Produto
POST /api/produtos
{...dados do produto...}

# 4. Criar Pedido de Venda
POST /pedidos
{
  "tipo": "VENDA",
  "pessoaId": 1,
  "funcionarioId": 1,
  "itens": [...]
}
```

### Fluxo de Gestão de Estoque

```bash
# 1. Consultar estoque atual
GET /api/produtos/1

# 2. Adicionar estoque
PUT /api/produtos/add/1
50

# 3. Verificar atualização
GET /api/produtos/1
```

---

**Documentação gerada para Apollo ERP API v1.0**

