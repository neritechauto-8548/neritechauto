# Arquitetura Docker - Neritech Auto

Esta solução utiliza Docker Compose para orquestrar os seguintes serviços:

- **neritech-db**: Banco de dados PostgreSQL 15.
- **neritech-api**: Backend Spring Boot (Java).
- **neritech-proxy**: Nginx atuando como Proxy Reverso e gerenciando SSL.
- **neritech-certbot**: Renovação automática de certificados SSL.
- **neritech-watchtower**: Atualização automática do container da API.

O diagrama visual foi gerado e está disponível na documentação anexa.
