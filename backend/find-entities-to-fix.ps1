$repositoriesPath = "C:\Users\alexa\OneDrive\Documentos\NeriTech\backend\src\main\java\com\neritech\saas"
$domainPath = "C:\Users\alexa\OneDrive\Documentos\NeriTech\backend\src\main\java\com\neritech\saas"

# Buscar todos os repositórios que usam findByEmpresaId
$repos = Get-ChildItem -Path $repositoriesPath -Recurse -Filter "*Repository.java" | 
    Select-String -Pattern "findBy.*EmpresaId" | 
    Select-Object -ExpandProperty Path -Unique

Write-Host "Repositórios com métodos findBy*EmpresaId:"
$repos | ForEach-Object { Write-Host $_ }

# Para cada repositório, verificar se a entidade correspondente estende TenantEntity
foreach ($repo in $repos) {
    $repoName = (Get-Item $repo).BaseName
    $entityName = $repoName -replace "Repository$", ""
    
    # Encontrar a entidade correspondente
    $entityFile = Get-ChildItem -Path $domainPath -Recurse -Filter "$entityName.java" | Select-Object -First 1
    
    if ($entityFile) {
        $content = Get-Content $entityFile.FullName -Raw
        if ($content -match "extends\s+TenantEntity") {
            Write-Host "`n✓ $entityName já estende TenantEntity" -ForegroundColor Green
        } elseif ($content -match "extends\s+BaseEntity") {
            Write-Host "`n✗ $entityName estende BaseEntity - PRECISA SER CORRIGIDO" -ForegroundColor Red
            Write-Host "  Arquivo: $($entityFile.FullName)"
        }
    }
}
