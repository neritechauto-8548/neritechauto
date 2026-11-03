# Chave de proteção das credenciais

As configurações SMTP e os arquivos/senhas de certificado A1 são cifrados com AES-256-GCM antes de serem gravados no PostgreSQL.

Configure `NERITECH_CHAVE_CRIPTOGRAFIA` no ambiente do backend como uma chave Base64 de 32 bytes. Gere uma chave nova com `openssl rand -base64 32` e mantenha-a em um cofre de segredos. Use a mesma chave em todas as instâncias e preserve-a em backups protegidos: sem ela, os segredos já armazenados não poderão ser lidos. Não grave a chave no repositório nem em logs.

Sem uma chave válida, a aplicação inicia normalmente, mas rejeita gravações de configurações que contenham segredos com erro controlado `503`.
