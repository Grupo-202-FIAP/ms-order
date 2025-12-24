# MS-Order - Microsserviço de Pedidos

Microsserviço responsável pelo gerenciamento de pedidos em uma arquitetura de microsserviços. Este serviço gerencia o ciclo de vida completo dos pedidos, desde a criação até o processamento e rastreamento de eventos.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Funcionalidades](#funcionalidades)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)
- [Configuração](#configuração)
- [API Endpoints](#api-endpoints)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Testes](#testes)
- [Docker](#docker)
- [Contribuindo](#contribuindo)

## 🎯 Visão Geral

O MS-Order é um microsserviço desenvolvido em Java com Spring Boot que gerencia pedidos de um sistema de e-commerce. Ele utiliza uma arquitetura hexagonal (Ports and Adapters) para garantir desacoplamento e testabilidade, integrando-se com MongoDB para persistência e AWS SQS para comunicação assíncrona.

### Principais Características

- ✅ Arquitetura Hexagonal (Clean Architecture)
- ✅ Comunicação assíncrona via AWS SQS
- ✅ Persistência com MongoDB
- ✅ Documentação automática com Swagger/OpenAPI
- ✅ Logging estruturado em JSON
- ✅ Tratamento centralizado de exceções
- ✅ Cobertura de testes unitários abrangente
- ✅ Mutation Testing com PIT

## 🛠 Tecnologias

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data MongoDB**
- **Spring Cloud AWS SQS**
- **MapStruct** - Mapeamento de objetos
- **Lombok** - Redução de boilerplate
- **MongoDB** - Banco de dados NoSQL
- **AWS SQS** - Mensageria
- **Swagger/OpenAPI** - Documentação da API
- **JUnit 5** - Framework de testes
- **Mockito** - Mocking para testes
- **AssertJ** - Asserções fluentes
- **PIT Mutation Testing** - Testes de mutação
- **JaCoCo** - Cobertura de código

## 🏗 Arquitetura

O projeto segue os princípios da **Arquitetura Hexagonal** (Ports and Adapters), organizando o código em camadas:

```
┌─────────────────────────────────────┐
│      Infrastructure Layer           │
│  (Controllers, Adapters, Repos)     │
└─────────────────────────────────────┘
              ↕
┌─────────────────────────────────────┐
│      Application Layer              │
│  (Use Cases, Mappers, Gateways)     │
└─────────────────────────────────────┘
              ↕
┌─────────────────────────────────────┐
│      Domain Layer                   │
│  (Entities, Enums, Exceptions)      │
└─────────────────────────────────────┘
```

### Camadas

1. **Domain**: Contém as entidades de negócio, enums e exceções de domínio
2. **Application**: Contém os casos de uso, interfaces de portas e mappers
3. **Infrastructure**: Contém adaptadores, controllers, repositórios e integrações externas

## 🚀 Funcionalidades

### Gestão de Pedidos

- **Criar Pedido**: Criação de novos pedidos com validação de itens
- **Listar Pedidos**: Listagem de todos os pedidos cadastrados
- **Buscar Pedido por ID**: Busca de pedido específico
- **Listar por Status**: Filtragem de pedidos por status (RECEIVED, PREPARING, READY, COMPLETED, CANCELLED)

### Gestão de Eventos

- **Buscar Evento por Filtros**: Busca de eventos por `orderId` ou `transactionId`
- **Listar Todos os Eventos**: Listagem completa de eventos ordenados por data

### Processamento Assíncrono

- **Produção de Eventos**: Envio de eventos para fila SQS após criação de pedido
- **Consumo de Eventos**: Consumo de eventos da fila de callback

## 📦 Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- MongoDB 4.4+
- Docker e Docker Compose (para ambiente local)
- AWS Account (para produção) ou LocalStack (para desenvolvimento local)

## 🔧 Instalação e Execução

### Executando com Docker Compose

1. Clone o repositório:
```bash
git clone https://github.com/Grupo-202-FIAP/ms-order
cd ms-order
```

2. Execute o ambiente completo com Docker Compose:
```bash
docker-compose up -d
```

Isso irá iniciar:
- MongoDB na porta 27017
- LocalStack (simulação AWS) na porta 4566
- A aplicação na porta 8079

### Executando Localmente

1. Configure as variáveis de ambiente ou use o perfil `local`:
```bash
export MONGO_DB_URI=mongodb://localhost:27017/order-db
export AWS_REGION=us-east-1
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export SQS_ORDER_QUEUE=order-queue
export SQS_ORDER_CALLBACK_QUEUE=order-callback-queue
```

2. Execute a aplicação:
```bash
mvn spring-boot:run
```

Ou usando o perfil local:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## ⚙️ Configuração

### Variáveis de Ambiente

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `MONGO_DB_URI` | URI de conexão do MongoDB | - |
| `AWS_REGION` | Região AWS | `us-east-1` |
| `AWS_ACCESS_KEY_ID` | Chave de acesso AWS | - |
| `AWS_SECRET_ACCESS_KEY` | Chave secreta AWS | - |
| `SQS_ORDER_QUEUE` | Nome da fila SQS para pedidos | - |
| `SQS_ORDER_CALLBACK_QUEUE` | Nome da fila SQS de callback | - |

### Perfis Spring

- **default**: Configuração para produção
- **local**: Configuração para desenvolvimento local com LocalStack

## 📡 API Endpoints

### Pedidos

#### Criar Pedido
```http
POST /api/order/create
Content-Type: application/json

{
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "priceAtPurchase": 25.50
    }
  ],
  "customerId": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Resposta:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "identifier": "ORD-ABCD-1234",
  "totalPrice": 51.00,
  "status": "RECEIVED",
  "createdAt": "2024-01-15T10:30:00",
  "items": [...]
}
```

#### Listar Todos os Pedidos
```http
GET /api/order
```

#### Listar Pedidos por Status
```http
GET /api/order/status?status=RECEIVED
```

### Eventos

#### Buscar Evento por Filtros
```http
GET /api/event/filter?orderId={orderId}
GET /api/event/filter?transactionId={transactionId}
```

#### Listar Todos os Eventos
```http
GET /api/event/all
```

### Documentação Swagger

Acesse a documentação interativa da API em:
```
http://localhost:8079/swagger-ui.html
```

## 📁 Estrutura do Projeto

```
ms-order/
├── src/
│   ├── main/
│   │   ├── java/com/nexTime/order/
│   │   │   ├── application/          # Camada de aplicação
│   │   │   │   ├── config/           # Configurações
│   │   │   │   ├── gateways/        # Portas (interfaces)
│   │   │   │   ├── mapper/          # Mappers MapStruct
│   │   │   │   └── usecases/        # Casos de uso
│   │   │   ├── domain/              # Camada de domínio
│   │   │   │   ├── enums/           # Enumeradores
│   │   │   │   └── exception/       # Exceções de domínio
│   │   │   ├── infrastructure/      # Camada de infraestrutura
│   │   │   │   ├── adapters/        # Adaptadores
│   │   │   │   ├── controller/      # Controllers REST
│   │   │   │   ├── messaging/       # Mensageria (SQS)
│   │   │   │   └── persistence/     # Persistência (MongoDB)
│   │   │   └── utils/               # Utilitários
│   │   └── resources/
│   │       ├── application.yaml      # Configuração principal
│   │       └── application-local.yaml # Configuração local
│   └── test/                        # Testes unitários
├── docker/                          # Scripts Docker
├── docker-compose.yml               # Orquestração de containers
├── pom.xml                          # Dependências Maven
└── README.md                        # Este arquivo
```

## 🧪 Testes

### Executando Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn clean test jacoco:report

# Visualizar relatório de cobertura
# Abra: target/site/jacoco/index.html
```

### Mutation Testing

O projeto utiliza PIT (Pitest) para testes de mutação, que verifica a qualidade dos testes:

```bash
# Executar mutation testing
mvn org.pitest:pitest-maven:mutationCoverage

# Visualizar relatório
# Abra: target/pit-reports/index.html
```

### Cobertura de Testes

O projeto possui cobertura abrangente de testes unitários para:
- ✅ Use Cases (100%)
- ✅ Controllers (100%)
- ✅ Adapters (100%)
- ✅ Utils (100%)
- ✅ Domain Models (100%)
- ✅ Exceptions (100%)
- ✅ Exception Handlers (100%)

**Meta de Cobertura**: 80% mínimo

## 🐳 Docker

### Dockerfile

O projeto inclui um Dockerfile para containerização:

```bash
# Build da imagem
docker build -t ms-order:latest .

# Executar container
docker run -p 8079:8079 ms-order:latest
```

### Docker Compose

O `docker-compose.yml` inclui:
- MongoDB
- LocalStack (simulação AWS)
- Aplicação MS-Order

## 📊 Status dos Pedidos

O sistema suporta os seguintes status de pedido:

- **RECEIVED**: Pedido recebido
- **PREPARING**: Pedido em preparação
- **READY**: Pedido pronto
- **COMPLETED**: Pedido completado
- **CANCELLED**: Pedido cancelado

## 🔍 Logging

O projeto utiliza logging estruturado em JSON com Logstash Logback Encoder, facilitando a integração com sistemas de monitoramento como ELK Stack.

## 🚨 Tratamento de Exceções

O projeto possui tratamento centralizado de exceções através do `ExceptionGlobalHandler`, que:
- Captura exceções de validação
- Retorna respostas padronizadas
- Registra logs apropriados

## 📝 Exemplos de Uso

### Criar um Pedido

```bash
curl -X POST http://localhost:8079/api/order/create \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 2,
        "priceAtPurchase": 25.50
      }
    ],
    "customerId": "550e8400-e29b-41d4-a716-446655440000"
  }'
```

### Buscar Pedidos por Status

```bash
curl http://localhost:8079/api/order/status?status=RECEIVED
```

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### Padrões de Código

- Siga as convenções do Checkstyle configurado
- Mantenha cobertura de testes acima de 80%
- Escreva testes unitários para novas funcionalidades
- Documente APIs públicas

## 📄 Licença

Este projeto é parte de um sistema maior e está sujeito às políticas de licenciamento da organização.

## 👥 Autores

- Equipe NexTime

## 📞 Suporte

Para questões e suporte, entre em contato com a equipe de desenvolvimento.

---

**Última atualização**: 2025
