# Script para corrigir URLs incorretas nos services do frontend
# Remove "/api" duplicado das URLs já que baseUrl já é "/api"

$servicesPath = "src\app\routes"
$files = Get-ChildItem -Path $servicesPath -Filter "*.service.ts" -Recurse

$corrections = 0
$errors = 0

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    $originalContent = $content

    # Padrão 1: baseUrl + '/api/v1/...' -> baseUrl + '/v1/...'
    $content = $content -replace "baseUrl\s*\+\s*['\`"]/api/v1/", "baseUrl + '/v1/"

    # Padrão 2: baseUrl + '/api/...' -> baseUrl + '/v1/...' (exceto /auth)
    $content = $content -replace "baseUrl\s*\+\s*['\`"]/api/(?!auth)", "baseUrl + '/v1/"

    # Padrão 3: '/api/v1/...' -> '/v1/...' (quando não usa baseUrl)
    $content = $content -replace "['\`"]/api/v1/", "'/v1/"

    # Padrão 4: '/api/...' -> '/v1/...' (quando não usa baseUrl, exceto /auth)
    $content = $content -replace "['\`"]/api/(?!auth)", "'/v1/"

    if ($content -ne $originalContent) {
        try {
            Set-Content -Path $file.FullName -Value $content -NoNewline
            Write-Host "Corrigido: $($file.Name)" -ForegroundColor Green
            $corrections++
        } catch {
            Write-Host "Erro ao corrigir: $($file.FullName) - $_" -ForegroundColor Red
            $errors++
        }
    }
}

Write-Host "`nResumo:" -ForegroundColor Cyan
Write-Host "   Arquivos corrigidos: $corrections" -ForegroundColor Green
if ($errors -gt 0) {
    Write-Host "   Erros: $errors" -ForegroundColor Red
} else {
    Write-Host "   Erros: $errors" -ForegroundColor Green
}

