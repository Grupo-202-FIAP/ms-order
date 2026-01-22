#!/bin/bash
# Script Bash para configurar infraestrutura de testes
# MS Order - Testes de Integração

echo "🚀 Configurando infraestrutura de testes..."

# 1. MongoDB
echo ""
echo "📦 1. Configurando MongoDB..."
if docker ps -a --format '{{.Names}}' | grep -q "mongodb-order-tests"; then
    echo "   MongoDB container já existe. Iniciando..."
    docker start mongodb-order-tests > /dev/null 2>&1
else
    echo "   Criando MongoDB container..."
    docker run -d \
        --name mongodb-order-tests \
        -p 27017:27017 \
        -e MONGO_INITDB_ROOT_USERNAME=root \
        -e MONGO_INITDB_ROOT_PASSWORD=password \
        mongo:7.0 > /dev/null 2>&1
fi

sleep 3
echo "   ✅ MongoDB rodando em localhost:27017"

# 2. LocalStack
echo ""
echo "🐳 2. Configurando LocalStack..."
if docker ps -a --format '{{.Names}}' | grep -q "localstack-order-tests"; then
    echo "   LocalStack container já existe. Iniciando..."
    docker start localstack-order-tests > /dev/null 2>&1
else
    echo "   Criando LocalStack container..."
    docker run -d \
        --name localstack-order-tests \
        -p 4566:4566 \
        -e SERVICES=sqs \
        localstack/localstack:latest > /dev/null 2>&1
fi

echo "   ⏳ Aguardando LocalStack iniciar..."
sleep 10

# 3. Filas SQS
echo ""
echo "📬 3. Criando filas SQS..."

echo "   Criando fila: test-order-queue"
docker exec localstack-order-tests \
    awslocal sqs create-queue --queue-name test-order-queue > /dev/null 2>&1

echo "   Criando fila: test-order-callback-queue"
docker exec localstack-order-tests \
    awslocal sqs create-queue --queue-name test-order-callback-queue > /dev/null 2>&1

echo "   ✅ Filas SQS criadas"

# 4. Validação
echo ""
echo "🔍 4. Validando configuração..."

# Validar MongoDB
if docker ps --format '{{.Names}}' | grep -q "mongodb-order-tests"; then
    echo "   ✅ MongoDB: Rodando"
else
    echo "   ❌ MongoDB: NÃO está rodando"
fi

# Validar LocalStack
if docker ps --format '{{.Names}}' | grep -q "localstack-order-tests"; then
    echo "   ✅ LocalStack: Rodando"
else
    echo "   ❌ LocalStack: NÃO está rodando"
fi

# Listar filas
echo ""
echo "📋 Filas SQS criadas:"
docker exec localstack-order-tests awslocal sqs list-queues 2>/dev/null

# 5. Resumo
echo ""
echo "✨ Configuração completa!"
echo ""
echo "📝 Próximos passos:"
echo "   1. Compilar: ./mvnw clean compile"
echo "   2. Executar testes: ./mvnw test"
echo "   3. Ver cobertura: ./mvnw test jacoco:report"

echo ""
echo "🛑 Para parar a infraestrutura:"
echo "   docker stop mongodb-order-tests localstack-order-tests"

echo ""
echo "🗑️  Para remover a infraestrutura:"
echo "   docker rm -f mongodb-order-tests localstack-order-tests"

