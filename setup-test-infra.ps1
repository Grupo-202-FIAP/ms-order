# Script PowerShell para configurar infraestrutura de testes
# MS Order - Testes de Integração

Write-Host "🚀 Configurando infraestrutura de testes..." -ForegroundColor Green

# 1. MongoDB
Write-Host "`n📦 1. Configurando MongoDB..." -ForegroundColor Cyan
$mongoExists = docker ps -a --format "{{.Names}}" | Select-String -Pattern "mongodb-order-tests"

if ($mongoExists) {
    Write-Host "   MongoDB container já existe. Iniciando..." -ForegroundColor Yellow
    docker start mongodb-order-tests | Out-Null
} else {
    Write-Host "   Criando MongoDB container..." -ForegroundColor Yellow
    docker run -d `
        --name mongodb-order-tests `
        -p 27017:27017 `
        -e MONGO_INITDB_ROOT_USERNAME=root `
        -e MONGO_INITDB_ROOT_PASSWORD=password `
        mongo:7.0 | Out-Null
}

Start-Sleep -Seconds 3
Write-Host "   ✅ MongoDB rodando em localhost:27017" -ForegroundColor Green

# 2. LocalStack
Write-Host "`n🐳 2. Configurando LocalStack..." -ForegroundColor Cyan
$localstackExists = docker ps -a --format "{{.Names}}" | Select-String -Pattern "localstack-order-tests"

if ($localstackExists) {
    Write-Host "   LocalStack container já existe. Iniciando..." -ForegroundColor Yellow
    docker start localstack-order-tests | Out-Null
} else {
    Write-Host "   Criando LocalStack container..." -ForegroundColor Yellow
    docker run -d `
        --name localstack-order-tests `
        -p 4566:4566 `
        -e SERVICES=sqs `
        localstack/localstack:latest | Out-Null
}

Write-Host "   ⏳ Aguardando LocalStack iniciar..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# 3. Filas SQS
Write-Host "`n📬 3. Criando filas SQS..." -ForegroundColor Cyan

Write-Host "   Criando fila: test-order-queue" -ForegroundColor Yellow
docker exec localstack-order-tests `
    awslocal sqs create-queue --queue-name test-order-queue 2>$null | Out-Null

Write-Host "   Criando fila: test-order-callback-queue" -ForegroundColor Yellow
docker exec localstack-order-tests `
    awslocal sqs create-queue --queue-name test-order-callback-queue 2>$null | Out-Null

Write-Host "   ✅ Filas SQS criadas" -ForegroundColor Green

# 4. Validação
Write-Host "`n🔍 4. Validando configuração..." -ForegroundColor Cyan

# Validar MongoDB
$mongoRunning = docker ps --format "{{.Names}}" | Select-String -Pattern "mongodb-order-tests"
if ($mongoRunning) {
    Write-Host "   ✅ MongoDB: Rodando" -ForegroundColor Green
} else {
    Write-Host "   ❌ MongoDB: NÃO está rodando" -ForegroundColor Red
}

# Validar LocalStack
$localstackRunning = docker ps --format "{{.Names}}" | Select-String -Pattern "localstack-order-tests"
if ($localstackRunning) {
    Write-Host "   ✅ LocalStack: Rodando" -ForegroundColor Green
} else {
    Write-Host "   ❌ LocalStack: NÃO está rodando" -ForegroundColor Red
}

# Listar filas
Write-Host "`n📋 Filas SQS criadas:" -ForegroundColor Cyan
docker exec localstack-order-tests awslocal sqs list-queues 2>$null

# 5. Resumo
Write-Host "`n✨ Configuração completa!" -ForegroundColor Green
Write-Host "`n📝 Próximos passos:" -ForegroundColor Yellow
Write-Host "   1. Compilar: .\mvnw.cmd clean compile" -ForegroundColor White
Write-Host "   2. Executar testes: .\mvnw.cmd test" -ForegroundColor White
Write-Host "   3. Ver cobertura: .\mvnw.cmd test jacoco:report" -ForegroundColor White

Write-Host "`n🛑 Para parar a infraestrutura:" -ForegroundColor Yellow
Write-Host "   docker stop mongodb-order-tests localstack-order-tests" -ForegroundColor White

Write-Host "`n🗑️  Para remover a infraestrutura:" -ForegroundColor Yellow
Write-Host "   docker rm -f mongodb-order-tests localstack-order-tests" -ForegroundColor White

