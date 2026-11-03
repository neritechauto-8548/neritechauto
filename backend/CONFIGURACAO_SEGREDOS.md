# Chave de proteção das credenciais

As configurações SMTP e os arquivos/senhas de certificado A1 são cifrados com AES-256-GCM antes de serem gravados no PostgreSQL.

Configure `NERITECH_CHAVE_CRIPTOGRAFIA` no ambiente do backend como uma chave Base64 de 32 bytes. Gere uma chave nova com `openssl rand -base64 32` e mantenha-a em um cofre de segredos. Use a mesma chave em todas as instâncias e preserve-a em backups protegidos: sem ela, os segredos já armazenados não poderão ser lidos. Não grave a chave no repositório nem em logs.

O Docker Compose exige essa variável. Na execução local por Maven, o Spring carrega `backend/.env`; mantenha a chave nesse arquivo local, ignorado pelo Git, ou defina-a antes de iniciar a aplicação:

```powershell
$env:NERITECH_CHAVE_CRIPTOGRAFIA = '<chave-base64-estável-de-32-bytes>'
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
```

Gere a chave uma única vez com `openssl rand -base64 32`, mantenha o mesmo valor em todas as reinicializações e instâncias e guarde-o em um cofre de segredos. Não gere uma chave diferente a cada execução.

Sem uma chave válida, a aplicação inicia normalmente, mas rejeita gravações de configurações que contenham segredos com erro controlado `503`.

## Chave temporária de desenvolvimento

A chave temporária fica somente no arquivo `backend/.env` local (ignorado pelo Git) e no ambiente do usuário Windows desta estação. Preserve exatamente o mesmo valor enquanto houver dados cifrados com ela. Não copie a chave para este documento nem para arquivos versionados.