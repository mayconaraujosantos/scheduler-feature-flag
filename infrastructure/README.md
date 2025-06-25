# Infrastructure Layer

Esta camada contém todos os componentes de infraestrutura do projeto, organizados por responsabilidade.

## Estrutura

### `/mock-api`

- **db.json**: Dados mockados para simular a API de feature flags
- **json-server.json**: Configuração do json-server para servir os dados mockados

### `/middleware`

- **middleware.js**: Middleware para interceptar e modificar requisições HTTP

### `/docker`

- **docker-compose.yml**: Configuração do Docker Compose para orquestrar os serviços

## Como usar

### Mock API

```bash
# Iniciar o json-server
cd infrastructure/mock-api
json-server --watch db.json --port 3001
```

### Docker

```bash
# Iniciar todos os serviços
cd infrastructure/docker
docker-compose up -d
```

### Middleware

```bash
# O middleware é executado automaticamente pelo json-server
# quando configurado no json-server.json
```
