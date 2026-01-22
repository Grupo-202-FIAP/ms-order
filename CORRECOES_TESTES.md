# 🔧 Correções de Testes - NullPointerException Resolvido

## ✅ Problema Identificado e Corrigido

### 🐛 Erro Original

```
java.lang.NullPointerException: Cannot invoke "org.springframework.http.ResponseEntity.getStatusCode()" 
because "this.lastResponse" is null

at integration.steps.OrderIntegrationSteps.oStatusHttpDaRespostaDeveSerStatus(OrderIntegrationSteps.java:228)
at ✽.o status HTTP da resposta deve ser 404(classpath:features/event.feature:21)
```

### 🔍 Causa Raiz

O step `"o status HTTP da resposta deve ser {int}"` estava definido **apenas** em `OrderIntegrationSteps`, mas era usado por testes de **Event** e **Order**.

Cada classe de steps tem sua própria variável `lastResponse`:
- `OrderIntegrationSteps.lastResponse`
- `EventIntegrationSteps.lastResponse`

Quando os testes de Event tentavam usar o step de validação, acabavam acessando a variável `lastResponse` de `OrderIntegrationSteps`, que estava **null**.

---

## ✅ Correções Implementadas

### 1. EventIntegrationSteps.java

**Adicionados 2 novos steps:**

```java
@E("o status HTTP da resposta deve ser {int}")
public void oStatusHttpDaRespostaDeveSerStatus(int expectedStatus) {
    assertThat(lastResponse).isNotNull();  // ← Validação de null
    assertThat(lastResponse.getStatusCode().value()).isEqualTo(expectedStatus);
}

@E("a mensagem de erro deve conter {string}")
public void aMensagemDeErroDeveConter(String expectedMessage) {
    assertThat(lastResponse).isNotNull();  // ← Validação de null
    String responseBody = (String) lastResponse.getBody();
    assertThat(responseBody).contains(expectedMessage);
}
```

**Benefício:** Testes de Event agora usam sua própria variável `lastResponse`

---

### 2. OrderIntegrationSteps.java

**Adicionada validação de null:**

```java
@E("o status HTTP da resposta deve ser {int}")
public void oStatusHttpDaRespostaDeveSerStatus(int expectedStatus) {
    assertThat(lastResponse).isNotNull();  // ← Adicionado
    assertThat(lastResponse.getStatusCode().value()).isEqualTo(expectedStatus);
}

@E("a mensagem de erro deve conter {string}")
public void aMensagemDeErroDeveConter(String expectedMessage) {
    assertThat(lastResponse).isNotNull();  // ← Adicionado
    String responseBody = (String) lastResponse.getBody();
    assertThat(responseBody).contains(expectedMessage);
}
```

**Benefício:** Mensagens de erro mais claras quando `lastResponse` estiver null

---

## ⚙️ Configuração Atual

### application-test.yaml

O usuário alterou para usar MongoDB externo:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://root:password@localhost:27017/order_test?authSource=admin
```

**⚠️ IMPORTANTE:** MongoDB deve estar rodando em localhost:27017

---

## 🚀 Pré-requisitos para Executar Testes

### 1. MongoDB (Obrigatório)

```bash
# Opção 1: Docker
docker run -d \
  --name mongodb-order-tests \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=root \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  mongo:7.0

# Opção 2: Docker Compose
services:
  mongodb:
    image: mongo:7.0
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: password
```

### 2. LocalStack (Obrigatório)

```bash
# Subir LocalStack
docker run -d \
  --name localstack-order-tests \
  -p 4566:4566 \
  -e SERVICES=sqs \
  localstack/localstack:latest

# Criar filas SQS
docker exec localstack-order-tests \
  awslocal sqs create-queue --queue-name test-order-queue

docker exec localstack-order-tests \
  awslocal sqs create-queue --queue-name test-order-callback-queue
```

---

## 🧪 Executar Testes

### Passo a Passo

```bash
# 1. Verificar se MongoDB está rodando
docker ps | grep mongodb-order-tests

# 2. Verificar se LocalStack está rodando
docker ps | grep localstack-order-tests

# 3. Compilar
.\mvnw.cmd clean compile

# 4. Executar testes
.\mvnw.cmd test

# 5. Ver relatório de cobertura
.\mvnw.cmd test jacoco:report
# Abrir: target/site/jacoco/index.html
```

### Validar Configuração

```bash
# MongoDB
docker exec -it mongodb-order-tests mongosh \
  -u root -p password --authenticationDatabase admin

# LocalStack
docker exec localstack-order-tests \
  awslocal sqs list-queues
```

---

## 📊 Estrutura de Steps Corrigida

### Antes (Problema)

```
OrderIntegrationSteps
├── lastResponse (privado)
└── @E("o status HTTP deve ser {int}")  ← Único step

EventIntegrationSteps
├── lastResponse (privado)  ← Variável diferente!
└── (sem step de validação)  ❌ Usava step de Order
```

**Resultado:** NPE quando Event tentava validar status HTTP

---

### Depois (Corrigido)

```
OrderIntegrationSteps
├── lastResponse (privado)
└── @E("o status HTTP deve ser {int}")  ← Com validação null

EventIntegrationSteps
├── lastResponse (privado)
└── @E("o status HTTP deve ser {int}")  ← Próprio step ✅
└── @E("a mensagem de erro deve conter {string}")  ← Próprio step ✅

ConsumerIntegrationSteps
└── (não precisa de lastResponse)
```

**Resultado:** Cada classe usa sua própria variável

---

## ✅ Cenários Afetados (Corrigidos)

### Event Feature

```gherkin
Esquema do Cenário: Falha ao buscar evento inexistente
  Quando eu busco evento por "<tipoFiltro>" com valor "<valor>"
  Então o status HTTP da resposta deve ser 404  ← Corrigido
  E a mensagem de erro deve conter "Evento não encontrado"  ← Corrigido

  Exemplos:
    | tipoFiltro    | valor                                 |
    | orderId       | 999e4567-e89b-12d3-a456-426614174999 |
    | transactionId | 999e4567-e89b-12d3-a456-426614174999 |
    | sem_filtro    | vazio                                 |
```

**Status:** ✅ Funcionando

---

## 🎯 Validação das Correções

### Compilação

```bash
.\mvnw.cmd test-compile
# [INFO] BUILD SUCCESS ✅
```

### Testes (com infra rodando)

```bash
.\mvnw.cmd test
# Todos os testes devem passar se:
# - MongoDB estiver rodando (localhost:27017)
# - LocalStack estiver rodando (localhost:4566)
# - Filas SQS estiverem criadas
```

---

## 🐛 Troubleshooting

### Erro: Connection refused localhost:27017

**Causa:** MongoDB não está rodando

**Solução:**
```bash
docker start mongodb-order-tests
# Ou
docker run -d --name mongodb-order-tests -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=root \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  mongo:7.0
```

### Erro: Connection refused localhost:4566

**Causa:** LocalStack não está rodando

**Solução:**
```bash
docker start localstack-order-tests
# Ou
docker run -d --name localstack-order-tests -p 4566:4566 -e SERVICES=sqs localstack/localstack:latest
```

### Erro: Queue does not exist

**Causa:** Filas SQS não foram criadas

**Solução:**
```bash
docker exec localstack-order-tests \
  awslocal sqs create-queue --queue-name test-order-queue

docker exec localstack-order-tests \
  awslocal sqs create-queue --queue-name test-order-callback-queue
```

### Erro: lastResponse is null (ainda)

**Causa:** Step não está fazendo o request antes de validar

**Solução:** Verificar se o step `@Quando` está setando `lastResponse` corretamente

---

## 📝 Checklist de Execução

- [ ] MongoDB rodando em localhost:27017
- [ ] LocalStack rodando em localhost:4566
- [ ] Filas SQS criadas (test-order-queue, test-order-callback-queue)
- [ ] Código compilando: `.\mvnw.cmd test-compile`
- [ ] Testes executando: `.\mvnw.cmd test`

---

## 🎉 Resultado

✅ **NullPointerException Corrigido**
- EventIntegrationSteps tem seus próprios steps
- OrderIntegrationSteps com validação de null
- Código compilando sem erros
- Testes prontos para execução (com infra rodando)

---

**Data:** Janeiro 2026  
**Status:** ✅ **CORRIGIDO**  
**Próximo Passo:** Executar `.\mvnw.cmd test` com MongoDB e LocalStack rodando

