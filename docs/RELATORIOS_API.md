# Documentação dos Endpoints de Relatórios

## Visão Geral
Os endpoints de relatórios fornecem análises consolidadas sobre vendas, compras e relacionamentos com clientes e fornecedores.

**IMPORTANTE:** Todos os relatórios consideram apenas pedidos com status **FATURADO**. Pedidos com status ORCAMENTO ou CANCELADO não são incluídos nos relatórios.

## Base URL
```
/api/relatorios
```

## Endpoints Disponíveis

### 1. Relatório de Vendas por Período
**GET** `/vendas/periodo`

Retorna estatísticas consolidadas de vendas em um período específico. **Considera apenas pedidos faturados.**

**Parâmetros:**
- `dataInicio` (required): Data de início no formato dd/MM/yyyy
- `dataFim` (required): Data de fim no formato dd/MM/yyyy

**Exemplo de requisição:**
```
GET /api/relatorios/vendas/periodo?dataInicio=01/01/2025&dataFim=31/01/2025
```

**Resposta:**
```json
{
  "dataInicio": "2025-01-01T00:00:00",
  "dataFim": "2025-01-31T23:59:59",
  "quantidadePedidos": 45,
  "totalVendas": 15000.00,
  "totalCusto": 9000.00,
  "lucro": 6000.00,
  "ticketMedio": 333.33
}
```

---

### 2. Relatório de Compras por Período
**GET** `/compras/periodo`

Retorna estatísticas consolidadas de compras em um período específico. **Considera apenas pedidos faturados.**

**Parâmetros:**
- `dataInicio` (required): Data de início no formato dd/MM/yyyy
- `dataFim` (required): Data de fim no formato dd/MM/yyyy

**Exemplo de requisição:**
```
GET /api/relatorios/compras/periodo?dataInicio=01/01/2025&dataFim=31/01/2025
```

**Resposta:**
```json
{
  "dataInicio": "2025-01-01T00:00:00",
  "dataFim": "2025-01-31T23:59:59",
  "quantidadePedidos": 20,
  "totalCompras": 12000.00,
  "ticketMedio": 600.00
}
```

---

### 3. Relatório Consolidado por Cliente
**GET** `/clientes`

Retorna estatísticas de todos os clientes que realizaram compras, com possibilidade de filtrar por período. **Considera apenas pedidos faturados.**

**Parâmetros:**
- `dataInicio` (opcional): Data de início no formato dd/MM/yyyy
- `dataFim` (opcional): Data de fim no formato dd/MM/yyyy

**Exemplo de requisição sem filtro:**
```
GET /api/relatorios/clientes
```

**Exemplo de requisição com filtro de período:**
```
GET /api/relatorios/clientes?dataInicio=01/01/2025&dataFim=31/01/2025
```

**Resposta:**
```json
[
  {
    "idPessoa": 1,
    "nome": "João Silva",
    "cpfCnpj": "123.456.789-00",
    "tipo": "CLIENTE",
    "quantidadePedidos": 10,
    "totalValor": 5000.00,
    "ticketMedio": 500.00,
    "ultimoPedido": "15/01/2025 14:30"
  },
  {
    "idPessoa": 2,
    "nome": "Maria Santos",
    "cpfCnpj": "987.654.321-00",
    "tipo": "CLIENTE",
    "quantidadePedidos": 5,
    "totalValor": 3000.00,
    "ticketMedio": 600.00,
    "ultimoPedido": "20/01/2025 10:15"
  }
]
```

---

### 4. Detalhamento de Pedidos por Cliente
**GET** `/clientes/{idCliente}/detalhes`

Retorna todos os pedidos de um cliente específico com detalhes, com possibilidade de filtrar por período. **Considera apenas pedidos faturados.**

**Parâmetros:**
- `idCliente` (path): ID do cliente
- `dataInicio` (opcional): Data de início no formato dd/MM/yyyy
- `dataFim` (opcional): Data de fim no formato dd/MM/yyyy

**Exemplo de requisição sem filtro:**
```
GET /api/relatorios/clientes/1/detalhes
```

**Exemplo de requisição com filtro de período:**
```
GET /api/relatorios/clientes/1/detalhes?dataInicio=01/01/2025&dataFim=31/01/2025
```

**Resposta:**
```json
[
  {
    "idPedido": 101,
    "tipo": "VENDA",
    "status": "CONCLUIDO",
    "dataEmissao": "2025-01-15T14:30:00",
    "nomePessoa": "João Silva",
    "nomeFuncionario": "Carlos Vendedor",
    "totalCusto": 300.00,
    "totalVenda": 500.00,
    "formaPagamento": "Cartão de Crédito"
  },
  {
    "idPedido": 105,
    "tipo": "VENDA",
    "status": "PENDENTE",
    "dataEmissao": "2025-01-20T10:00:00",
    "nomePessoa": "João Silva",
    "nomeFuncionario": "Ana Vendedora",
    "totalCusto": 450.00,
    "totalVenda": 750.00,
    "formaPagamento": "PIX"
  }
]
```

---

### 5. Relatório Consolidado por Fornecedor
**GET** `/fornecedores`

Retorna estatísticas de todos os fornecedores com os quais foram feitas compras, com possibilidade de filtrar por período. **Considera apenas pedidos faturados.**

**Parâmetros:**
- `dataInicio` (opcional): Data de início no formato dd/MM/yyyy
- `dataFim` (opcional): Data de fim no formato dd/MM/yyyy

**Exemplo de requisição sem filtro:**
```
GET /api/relatorios/fornecedores
```

**Exemplo de requisição com filtro de período:**
```
GET /api/relatorios/fornecedores?dataInicio=01/01/2025&dataFim=31/01/2025
```

**Resposta:**
```json
[
  {
    "idPessoa": 10,
    "nome": "Fornecedor ABC Ltda",
    "cpfCnpj": "12.345.678/0001-90",
    "tipo": "FORNECEDOR",
    "quantidadePedidos": 15,
    "totalValor": 25000.00,
    "ticketMedio": 1666.67,
    "ultimoPedido": "10/01/2025 09:00"
  }
]
```

---

### 6. Detalhamento de Pedidos por Fornecedor
**GET** `/fornecedores/{idFornecedor}/detalhes`

Retorna todos os pedidos de um fornecedor específico com detalhes, com possibilidade de filtrar por período. **Considera apenas pedidos faturados.**

**Parâmetros:**
- `idFornecedor` (path): ID do fornecedor
- `dataInicio` (opcional): Data de início no formato dd/MM/yyyy
- `dataFim` (opcional): Data de fim no formato dd/MM/yyyy

**Exemplo de requisição sem filtro:**
```
GET /api/relatorios/fornecedores/10/detalhes
```

**Exemplo de requisição com filtro de período:**
```
GET /api/relatorios/fornecedores/10/detalhes?dataInicio=01/01/2025&dataFim=31/01/2025
```

**Resposta:**
```json
[
  {
    "idPedido": 201,
    "tipo": "COMPRA",
    "status": "CONCLUIDO",
    "dataEmissao": "2025-01-10T09:00:00",
    "nomePessoa": "Fornecedor ABC Ltda",
    "nomeFuncionario": "Pedro Comprador",
    "totalCusto": 5000.00,
    "totalVenda": null,
    "formaPagamento": "Boleto"
  }
]
```

---

## Códigos de Status HTTP

- `200 OK`: Requisição bem-sucedida
- `400 Bad Request`: Parâmetros inválidos ou faltando
- `401 Unauthorized`: Autenticação necessária
- `404 Not Found`: Recurso não encontrado
- `500 Internal Server Error`: Erro no servidor

## Notas Importantes

1. **Formato de Data**: As datas nos parâmetros de requisição devem estar no formato brasileiro: `dd/MM/yyyy`
   - Exemplo: `15/01/2025`
   - As datas serão automaticamente convertidas para o início do dia (00:00:00)

2. **Autenticação**: Todos os endpoints podem requerer autenticação (verificar configuração do SecurityConfig)

3. **Valores Monetários**: Todos os valores são retornados com 2 casas decimais

4. **Cálculos**:
   - **Ticket Médio**: Total de valor dividido pela quantidade de pedidos
   - **Lucro** (apenas vendas): Total de vendas menos total de custos

5. **Filtros por Tipo**:
   - Relatórios de **vendas** consideram apenas pedidos do tipo `VENDA`
   - Relatórios de **compras** consideram apenas pedidos do tipo `COMPRA`
   - Relatórios por **cliente** filtram pedidos de venda
   - Relatórios por **fornecedor** filtram pedidos de compra

6. **Filtro por Status**: 
   - **Apenas pedidos com status `FATURADO` são incluídos nos relatórios**
   - Pedidos com status `ORCAMENTO` ou `CANCELADO` são excluídos automaticamente

## Exemplos de Uso com cURL

### Vendas por Período
```bash
curl -X GET "http://localhost:8080/api/relatorios/vendas/periodo?dataInicio=01/01/2025&dataFim=31/01/2025" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Compras por Período
```bash
curl -X GET "http://localhost:8080/api/relatorios/compras/periodo?dataInicio=01/01/2025&dataFim=31/01/2025" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Relatório de Clientes
```bash
curl -X GET "http://localhost:8080/api/relatorios/clientes" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Relatório de Clientes com Período
```bash
curl -X GET "http://localhost:8080/api/relatorios/clientes?dataInicio=01/01/2025&dataFim=31/01/2025" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Detalhes por Cliente
```bash
curl -X GET "http://localhost:8080/api/relatorios/clientes/1/detalhes" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Detalhes por Cliente com Período
```bash
curl -X GET "http://localhost:8080/api/relatorios/clientes/1/detalhes?dataInicio=01/01/2025&dataFim=31/01/2025" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Relatório de Fornecedores
```bash
curl -X GET "http://localhost:8080/api/relatorios/fornecedores" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Relatório de Fornecedores com Período
```bash
curl -X GET "http://localhost:8080/api/relatorios/fornecedores?dataInicio=01/01/2025&dataFim=31/01/2025" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Detalhes por Fornecedor
```bash
curl -X GET "http://localhost:8080/api/relatorios/fornecedores/10/detalhes" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Detalhes por Fornecedor com Período
```bash
curl -X GET "http://localhost:8080/api/relatorios/fornecedores/10/detalhes?dataInicio=01/01/2025&dataFim=31/01/2025" \
  -H "Authorization: Bearer YOUR_TOKEN"
```
