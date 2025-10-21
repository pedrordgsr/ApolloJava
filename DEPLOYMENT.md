# 🚀 Apollo ERP - Guia de Deploy e Produção

## Índice
- [Preparação para Produção](#preparação-para-produção)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Deploy com Docker](#deploy-com-docker)
- [Deploy Tradicional](#deploy-tradicional)
- [Configurações de Produção](#configurações-de-produção)
- [Monitoramento](#monitoramento)
- [Backup](#backup)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Preparação para Produção

### Checklist Pré-Deploy

- [ ] Testes executados e passando
- [ ] Variáveis de ambiente configuradas
- [ ] Banco de dados criado e configurado
- [ ] SSL/TLS configurado
- [ ] Secrets e senhas seguras
- [ ] Logs configurados
- [ ] Backup configurado
- [ ] Monitoramento configurado

---

## ⚙️ Configuração do Ambiente

### Variáveis de Ambiente

Criar arquivo `.env` ou configurar no servidor:

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=apollo_prod
DB_USERNAME=apollo_user
DB_PASSWORD=senha_super_segura

# JWT
JWT_SECRET=gerar_uma_chave_segura_aqui_com_256_bits
JWT_EXPIRATION=86400000

# Application
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
```

### application-prod.properties

Criar arquivo `src/main/resources/application-prod.properties`:

```properties
# Application
spring.application.name=apollo-erp-prod

# Database
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA - IMPORTANTE: Mudar para validate em produção
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Connection Pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}

# Logging
logging.level.root=INFO
logging.level.com.apollo.main=INFO
logging.file.name=logs/apollo-erp.log
logging.file.max-size=10MB
logging.file.max-history=30

# Security
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12

# Actuator (Monitoramento)
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
```

---

## 🐳 Deploy com Docker

### Dockerfile

Criar arquivo `Dockerfile` na raiz do projeto:

```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar pom.xml e baixar dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fonte e compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Criar usuário não-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR da stage de build
COPY --from=build /app/target/*.jar app.jar

# Expor porta
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ping || exit 1

# Executar aplicação
ENTRYPOINT ["java", \
  "-Xms512m", \
  "-Xmx1024m", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=prod", \
  "-jar", \
  "app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: apollo-postgres
    environment:
      POSTGRES_DB: apollo_prod
      POSTGRES_USER: apollo_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init-db.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"
    networks:
      - apollo-network
    restart: unless-stopped
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U apollo_user"]
      interval: 10s
      timeout: 5s
      retries: 5

  apollo-api:
    build: .
    container_name: apollo-api
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_HOST: postgres
      DB_PORT: 5432
      DB_NAME: apollo_prod
      DB_USERNAME: apollo_user
      DB_PASSWORD: ${DB_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
      JWT_EXPIRATION: 86400000
    ports:
      - "8080:8080"
    networks:
      - apollo-network
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "wget", "--spider", "-q", "http://localhost:8080/ping"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s

volumes:
  postgres_data:
    driver: local

networks:
  apollo-network:
    driver: bridge
```

### Comandos Docker

```bash
# Build da imagem
docker-compose build

# Iniciar containers
docker-compose up -d

# Ver logs
docker-compose logs -f apollo-api

# Parar containers
docker-compose down

# Parar e remover volumes (CUIDADO!)
docker-compose down -v

# Rebuild completo
docker-compose up -d --build --force-recreate
```

---

## 📦 Deploy Tradicional

### 1. Preparar Servidor

```bash
# Atualizar sistema (Ubuntu/Debian)
sudo apt update && sudo apt upgrade -y

# Instalar Java 21
sudo apt install openjdk-21-jdk -y

# Verificar instalação
java -version

# Instalar PostgreSQL
sudo apt install postgresql postgresql-contrib -y
```

### 2. Configurar Banco de Dados

```bash
# Acessar PostgreSQL
sudo -u postgres psql

# Criar banco e usuário
CREATE DATABASE apollo_prod;
CREATE USER apollo_user WITH ENCRYPTED PASSWORD 'senha_segura';
GRANT ALL PRIVILEGES ON DATABASE apollo_prod TO apollo_user;
\q
```

### 3. Compilar Aplicação

```bash
# No seu computador local
mvn clean package -DskipTests

# O JAR será gerado em: target/main-0.0.1-SNAPSHOT.jar
```

### 4. Transferir para Servidor

```bash
# Usar SCP
scp target/main-0.0.1-SNAPSHOT.jar usuario@servidor:/opt/apollo/

# Ou SFTP, FTP, etc.
```

### 5. Criar Script de Inicialização

Criar arquivo `/opt/apollo/start.sh`:

```bash
#!/bin/bash

export SPRING_PROFILES_ACTIVE=prod
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=apollo_prod
export DB_USERNAME=apollo_user
export DB_PASSWORD=senha_segura
export JWT_SECRET=sua_chave_jwt_segura

java -Xms512m -Xmx1024m \
     -jar /opt/apollo/main-0.0.1-SNAPSHOT.jar \
     > /opt/apollo/logs/app.log 2>&1
```

```bash
# Dar permissão de execução
chmod +x /opt/apollo/start.sh
```

### 6. Criar Serviço Systemd

Criar arquivo `/etc/systemd/system/apollo.service`:

```ini
[Unit]
Description=Apollo ERP API
After=postgresql.service
Requires=postgresql.service

[Service]
Type=simple
User=apollo
WorkingDirectory=/opt/apollo
ExecStart=/opt/apollo/start.sh
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=apollo-erp

# Limites de recursos
LimitNOFILE=65536
LimitNPROC=4096

[Install]
WantedBy=multi-user.target
```

### 7. Iniciar Serviço

```bash
# Recarregar systemd
sudo systemctl daemon-reload

# Habilitar inicialização automática
sudo systemctl enable apollo

# Iniciar serviço
sudo systemctl start apollo

# Verificar status
sudo systemctl status apollo

# Ver logs
sudo journalctl -u apollo -f
```

---

## 🔧 Configurações de Produção

### Nginx como Reverse Proxy

Criar arquivo `/etc/nginx/sites-available/apollo`:

```nginx
upstream apollo_backend {
    server 127.0.0.1:8080;
}

server {
    listen 80;
    server_name api.seudominio.com;
    
    # Redirecionar para HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name api.seudominio.com;

    # SSL
    ssl_certificate /etc/letsencrypt/live/api.seudominio.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.seudominio.com/privkey.pem;
    
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # Logs
    access_log /var/log/nginx/apollo_access.log;
    error_log /var/log/nginx/apollo_error.log;

    # Tamanho máximo de upload
    client_max_body_size 10M;

    location / {
        proxy_pass http://apollo_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # Swagger UI (opcional - considere desabilitar em produção)
    location /swagger-ui/ {
        proxy_pass http://apollo_backend;
        proxy_set_header Host $host;
    }
}
```

```bash
# Habilitar site
sudo ln -s /etc/nginx/sites-available/apollo /etc/nginx/sites-enabled/

# Testar configuração
sudo nginx -t

# Recarregar nginx
sudo systemctl reload nginx
```

### Certificado SSL com Let's Encrypt

```bash
# Instalar Certbot
sudo apt install certbot python3-certbot-nginx -y

# Obter certificado
sudo certbot --nginx -d api.seudominio.com

# Renovação automática (já configurado pelo Certbot)
sudo certbot renew --dry-run
```

---

## 📊 Monitoramento

### Spring Boot Actuator

Endpoints de monitoramento (já configurados):

```bash
# Health check
curl http://localhost:8080/actuator/health

# Métricas
curl http://localhost:8080/actuator/metrics

# Info da aplicação
curl http://localhost:8080/actuator/info
```

### Script de Monitoramento Simples

Criar arquivo `monitor.sh`:

```bash
#!/bin/bash

URL="http://localhost:8080/ping"
EMAIL="admin@seudominio.com"

if ! curl -f -s -o /dev/null "$URL"; then
    echo "Apollo ERP está DOWN!" | mail -s "ALERTA: Apollo DOWN" "$EMAIL"
    
    # Tentar reiniciar
    sudo systemctl restart apollo
fi
```

### Configurar Cron

```bash
# Editar crontab
crontab -e

# Adicionar (verificar a cada 5 minutos)
*/5 * * * * /opt/apollo/monitor.sh
```

---

## 💾 Backup

### Script de Backup do Banco de Dados

Criar arquivo `backup.sh`:

```bash
#!/bin/bash

BACKUP_DIR="/backup/apollo"
DATE=$(date +%Y%m%d_%H%M%S)
DB_NAME="apollo_prod"
DB_USER="apollo_user"

mkdir -p "$BACKUP_DIR"

# Backup do banco
pg_dump -U "$DB_USER" "$DB_NAME" | gzip > "$BACKUP_DIR/apollo_${DATE}.sql.gz"

# Remover backups com mais de 30 dias
find "$BACKUP_DIR" -name "apollo_*.sql.gz" -mtime +30 -delete

echo "Backup concluído: apollo_${DATE}.sql.gz"
```

### Automatizar Backup

```bash
# Editar crontab
crontab -e

# Backup diário às 2h da manhã
0 2 * * * /opt/apollo/backup.sh
```

### Restaurar Backup

```bash
# Descompactar e restaurar
gunzip -c apollo_20251021_020000.sql.gz | psql -U apollo_user -d apollo_prod
```

---

## 🔍 Troubleshooting

### Aplicação não inicia

```bash
# Ver logs
sudo journalctl -u apollo -n 100

# Verificar Java
java -version

# Verificar portas
sudo netstat -tulpn | grep 8080

# Verificar processos
ps aux | grep java
```

### Erro de conexão com banco

```bash
# Testar conexão PostgreSQL
psql -h localhost -U apollo_user -d apollo_prod

# Verificar se PostgreSQL está rodando
sudo systemctl status postgresql

# Ver logs do PostgreSQL
sudo tail -f /var/log/postgresql/postgresql-15-main.log
```

### Problemas de memória

```bash
# Ajustar heap do Java no start.sh
java -Xms1024m -Xmx2048m -jar app.jar

# Ver uso de memória
free -h
htop
```

### Logs não aparecem

```bash
# Verificar permissões do diretório de logs
ls -la /opt/apollo/logs/

# Criar diretório se não existir
mkdir -p /opt/apollo/logs
chown apollo:apollo /opt/apollo/logs
```

---

## 📈 Otimizações de Performance

### JVM Tuning

```bash
java -server \
     -Xms1024m \
     -Xmx2048m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -XX:+UseStringDeduplication \
     -XX:+OptimizeStringConcat \
     -jar app.jar
```

### PostgreSQL Tuning

Editar `/etc/postgresql/15/main/postgresql.conf`:

```ini
# Memória
shared_buffers = 256MB
effective_cache_size = 1GB
work_mem = 16MB

# Connections
max_connections = 100

# WAL
wal_buffers = 16MB
checkpoint_completion_target = 0.9

# Query Planning
random_page_cost = 1.1
effective_io_concurrency = 200
```

---

## 🔒 Segurança Adicional

### Firewall (UFW)

```bash
# Instalar UFW
sudo apt install ufw -y

# Configurar regras
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow ssh
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# Ativar
sudo ufw enable
```

### Fail2ban

```bash
# Instalar
sudo apt install fail2ban -y

# Configurar para proteger nginx
sudo nano /etc/fail2ban/jail.local
```

---

**Guia de Deploy - Apollo ERP v1.0**

