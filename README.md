# Feature Flag Service with Integration Buffer

Este projeto implementa um sistema de feature flags com buffer de integração que permite controlar dinamicamente o comportamento da aplicação através de flags configuradas externamente.

## Arquitetura

O projeto segue os princípios SOLID e Clean Architecture:

- **Domain Layer**: Entidades e interfaces de domínio
- **Application Layer**: Configurações e schedulers
- **Infrastructure Layer**: Gateways e implementações de acesso a dados
- **Resources Layer**: Controllers e exceções

## Componentes Principais

### Feature Flag System

- `FeatureFlagService`: Serviço principal para verificar feature flags
- `FeatureFlagGateway`: Gateway para comunicação com API externa de feature flags
- `FeatureFlagScheduler`: Scheduler para polling periódico de feature flags
- `FeatureFlagConfig`: Configuração das propriedades do sistema

### Integration Buffer System

- `IntegrationBufferService`: Serviço para bufferizar eventos baseado em feature flags
- `IntegrationController`: Controller REST para interação com o buffer
- `FeatureFlagDA`: Interface de acesso a dados para feature flags

## Configuração

### application.yml

```yaml
feature:
  flag:
    api:
      url: http://192.168.0.114:1880/feature-flags/parameters
      auth:
        token: secret
      timeout-ms: 10000
    poll:
      interval-ms: 15000
      initial-delay-ms: 5000
    ingestion:
      key: ENABLE_EVENT_INGESTION_FLAG
```

## Uso

### 1. Verificar Feature Flag

```kotlin
@Service
class MyService(
    private val featureFlagService: FeatureFlagService
) {
    fun processEvent(event: String) {
        if (featureFlagService.getEventIngestionFlag()) {
            // Lógica quando feature flag está habilitada
            logger.info("Processing event: $event")
        } else {
            // Lógica quando feature flag está desabilitada
            logger.warn("Event processing disabled by feature flag")
        }
    }
}
```

### 2. Usar Integration Buffer

```kotlin
@Service
class EventService(
    private val integrationBufferService: IntegrationBufferService
) {
    fun handleEvent(event: String) {
        // O buffer verifica automaticamente a feature flag
        integrationBufferService.bufferEvent(event)
    }
}
```

### 3. API REST

```bash
# Testar buffer de integração
POST /api/integration/test

# Bufferizar evento
POST /api/integration/events
{
    "type": "USER_LOGIN",
    "userId": "123",
    "timestamp": "2024-01-01T10:00:00Z"
}

# Verificar status
GET /api/integration/status
```

## Feature Flags

### ENABLE_EVENT_INGESTION_FLAG

Controla se os eventos devem ser processados pelo buffer de integração:

- `true`: Eventos são bufferizados e processados
- `false`: Eventos são ignorados

## Monitoramento

O sistema inclui métricas e logs para monitoramento:

- **Métricas Micrometer**: Tempo de resposta das APIs
- **Logs Estruturados**: Com prefixos de classe para fácil identificação
- **Health Checks**: Endpoints para verificação de status

## Testes

Execute os testes com:

```bash
./gradlew test
```

Os testes cobrem:

- Verificação de feature flags
- Comportamento do buffer de integração
- Tratamento de erros
- Cenários de feature flags desabilitadas

## Desenvolvimento

### Estrutura de Pacotes

```
src/main/kotlin/com/banking/
├── application/
│   ├── config/          # Configurações
│   └── scheduler/       # Schedulers
├── domain/
│   ├── dataccess/       # Interfaces de acesso a dados
│   ├── entities/        # Entidades de domínio
│   └── services/        # Serviços de domínio
└── resources/
    ├── controller/      # Controllers REST
    ├── exceptions/      # Exceções customizadas
    └── gateways/        # Gateways para APIs externas
```

### Princípios Aplicados

- **Dependency Inversion**: Interfaces no domínio, implementações na infraestrutura
- **Single Responsibility**: Cada classe tem uma responsabilidade específica
- **Open/Closed**: Extensível sem modificar código existente
- **Interface Segregation**: Interfaces pequenas e específicas
- **Dependency Injection**: Injeção de dependências via construtor

## Troubleshooting

### Dependências Circulares

Se encontrar erros de dependência circular, verifique:

1. Se há dependências entre beans que não deveriam existir
2. Se as configurações estão corretas
3. Se os qualifiers estão sendo usados adequadamente

### Feature Flags Não Funcionando

1. Verifique se a API externa está acessível
2. Confirme se as configurações de URL e token estão corretas
3. Verifique os logs para erros de comunicação
4. Teste a API externa diretamente

### Buffer Não Processando

1. Verifique se a feature flag `ENABLE_EVENT_INGESTION_FLAG` está habilitada
2. Confirme se o scheduler está rodando
3. Verifique os logs para erros de processamento
4. Teste o endpoint `/api/integration/test`
