# Sistema de Autenticação Spring Security + JWT

## Implementação Concluída

Foi implementado um sistema completo de autenticação utilizando Spring Security e JWT (JSON Web Tokens).

## Arquivos Criados

### 1. Segurança (security/)
- **JwtUtil.java**: Utilitário para gerar e validar tokens JWT
- **JwtAuthenticationFilter.java**: Filtro que intercepta requisições e valida tokens
- **CustomUserDetailsService.java**: Serviço que carrega usuários do banco de dados
- **SecurityConfig.java**: Configuração principal de segurança do Spring

### 2. DTOs (dto/)
- **LoginRequest.java**: DTO para requisição de login
- **RegisterRequest.java**: DTO para registro de novos usuários
- **AuthResponse.java**: DTO de resposta com token JWT

### 3. Repositório
- **UsuarioRepository.java**: Repository para buscar usuários

### 4. Serviço
- **AuthService.java**: Serviço de autenticação (login e registro)

### 5. Controller
- **AuthController.java**: Endpoints de autenticação

### 6. Model
- **Usuario.java**: Atualizado para implementar UserDetails do Spring Security

### 7. Configuração
- **OpenApiConfig.java**: Configuração do Swagger com suporte a JWT

## Configuração

As seguintes configurações foram adicionadas ao `application.properties`:

```properties
# JWT Configuration
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000  # 24 horas
```

## Endpoints de Autenticação

### 1. Registro de Usuário
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "usuario",
  "senha": "senha123",
  "funcionarioId": 1  // opcional
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "usuario",
  "usuarioId": 1
}
```

### 2. Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "usuario",
  "senha": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "usuario",
  "usuarioId": 1
}
```

## Como Usar

### 1. Registrar um novo usuário
Faça uma requisição POST para `/api/auth/register` com username e senha.

### 2. Fazer login
Faça uma requisição POST para `/api/auth/login` com as credenciais.

### 3. Usar o token nas requisições
Adicione o token JWT no header Authorization de todas as requisições protegidas:

```http
GET /api/clientes
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Testando com Swagger UI

O Swagger agora está configurado para trabalhar com autenticação JWT!

### Passo a passo:

1. **Acesse o Swagger UI**: `http://localhost:8080/swagger-ui.html`

2. **Registre ou faça login**:
   - Vá até a seção "Autenticação"
   - Execute o endpoint `/api/auth/login` ou `/api/auth/register`
   - Copie o valor do campo `token` da resposta

3. **Configure a autenticação no Swagger**:
   - Clique no botão **"Authorize"** (ícone de cadeado) no topo da página
   - Cole o token JWT no campo (sem o prefixo "Bearer")
   - Clique em **"Authorize"**
   - Clique em **"Close"**

4. **Teste os endpoints protegidos**:
   - Agora você pode testar todos os endpoints (clientes, produtos, pedidos, etc.)
   - O token será automaticamente incluído em todas as requisições
   - Você verá um ícone de cadeado fechado nos endpoints que estão autenticados

### Observações Importantes:
- O token expira em 24 horas. Após isso, será necessário fazer login novamente
- Se receber erro 401 (Unauthorized), verifique se o token está configurado corretamente
- Para sair, clique em "Authorize" novamente e depois em "Logout"

## Endpoints Públicos (sem autenticação)

- `/api/auth/**` - Endpoints de autenticação
- `/swagger-ui/**` - Documentação Swagger
- `/v3/api-docs/**` - OpenAPI docs
- `/ping` - Endpoint de teste

## Endpoints Protegidos

Todos os outros endpoints (clientes, produtos, pedidos, etc.) agora requerem autenticação JWT.

## Próximos Passos Recomendados

1. **Recarregar o projeto no IDE**: 
   - IntelliJ IDEA: File > Invalidate Caches / Restart
   - Eclipse: Right-click projeto > Maven > Update Project
   - VS Code: Recarregar janela (Ctrl+Shift+P > Reload Window)

2. **Testar os endpoints**: Use Postman, Insomnia ou o Swagger UI

3. **Criar primeiro usuário**: Faça uma requisição para `/api/auth/register`

4. **Adicionar roles/permissões** (futuro): Pode-se expandir o sistema para incluir diferentes níveis de acesso

## Segurança

- Senhas são criptografadas usando BCrypt
- Tokens JWT expiram em 24 horas
- Sessões são stateless (não armazenadas no servidor)
- CSRF está desabilitado (API REST)

## Troubleshooting

Se o IDE mostrar erros de importação do Spring Security:
1. Execute `mvn clean install` no terminal
2. Recarregue o projeto no IDE
3. Verifique se as dependências foram baixadas em `.m2/repository`
