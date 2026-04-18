# Script para aguardar Docker e fazer build
Write-Host "==================================" -ForegroundColor Cyan
Write-Host "  Aguardando Docker Desktop..." -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

$maxAttempts = 30
$attempt = 0
$dockerReady = $false

while (($attempt -lt $maxAttempts) -and (-not $dockerReady)) {
    $attempt++
    Write-Host "[$attempt/$maxAttempts] Verificando Docker..." -NoNewline
    
    $ErrorActionPreference = 'SilentlyContinue'
    $result = docker info 2>&1
    $ErrorActionPreference = 'Continue'
    
    if ($LASTEXITCODE -eq 0) {
        $dockerReady = $true
        Write-Host " OK" -ForegroundColor Green
    } else {
        Write-Host " Aguardando..." -ForegroundColor Yellow
        Start-Sleep -Seconds 5
    }
}

if (-not $dockerReady) {
    Write-Host ""
    Write-Host "ERRO: Docker nao iniciou" -ForegroundColor Red
    Write-Host "Por favor, inicie o Docker Desktop manualmente." -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "==================================" -ForegroundColor Cyan
Write-Host "  Iniciando Build do Docker..." -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# Build da imagem
Write-Host "Fazendo build da imagem 'neritech-backend'..." -ForegroundColor Cyan
docker build -t neritech-backend .

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERRO: Build falhou!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Build concluido com sucesso!" -ForegroundColor Green
Write-Host ""
Write-Host "==================================" -ForegroundColor Cyan
Write-Host "  Proximos Passos" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Para executar o container:" -ForegroundColor Yellow
Write-Host ""
Write-Host "docker run -p 8080:8080 ``" -ForegroundColor White
Write-Host "  -e DATABASE_URL=jdbc:postgresql://localhost:5434/nerisys ``" -ForegroundColor White
Write-Host "  -e DATABASE_USERNAME=postgres ``" -ForegroundColor White
Write-Host "  -e DATABASE_PASSWORD=94573730 ``" -ForegroundColor White
Write-Host "  -e JWT_SECRET=test-secret-key-minimum-256-bits-long-for-jwt-testing ``" -ForegroundColor White
Write-Host "  -e SPRING_PROFILES_ACTIVE=prod ``" -ForegroundColor White
Write-Host "  neritech-backend" -ForegroundColor White
Write-Host ""
Write-Host "Depois acesse:" -ForegroundColor Yellow
Write-Host "  - Health Check: http://localhost:8080/api/actuator/health" -ForegroundColor Cyan
Write-Host "  - Swagger UI: http://localhost:8080/api/swagger-ui.html" -ForegroundColor Cyan
Write-Host ""
