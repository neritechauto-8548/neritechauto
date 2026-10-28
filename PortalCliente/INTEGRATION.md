# Contrato de integração — Portal do Cliente (Next.js)

## Estado verificado na branch feature/develop

O backend atual tem `/auth/login` para **usuários internos da oficina**, não proprietários.
`PublicOrdemServicoController` exige `hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')` e `SecurityConfig` marca `/public/ordem-servico/**` como autenticado.
`GET /v1/ordens-servico/cliente/{clienteId}` também exige perfil interno e retorna `Page<OrdemServicoResponse>`.
Não existem `POST /v1/ordens-servico/{id}/aprovar` e `/negar` nesse controller. **Não reduzir as restrições para fazer o portal funcionar.**

## APIs segregadas requeridas no Spring Boot

Os nomes abaixo são apenas **exemplos de contratos novos**, não endpoints implementados. Configure paths reais nas variáveis de ambiente somente após o backend seguro estar pronto.

1. **Login do proprietário**: `POST /portal/cliente/auth/login` recebe `{email, senha}`, aplica rate limit e autentica **cliente da empresa**. Retorna `{accessToken, scope:"PORTAL_CLIENTE", clienteId:number, empresaId:number}`. Não retorna token de funcionário. É obrigatório impedir reutilização de JWT de usuário interno.
2. **Histórico**: `GET /portal/cliente/ordens` com `Authorization: Bearer <token>` válido de proprietário. A API **deriva clienteId e empresaId do JWT**; nunca aceita IDs fornecidos no frontend como ownership. Retorna array ou `Page` restrito ao proprietário.
3. **Detalhe**: `GET /portal/cliente/ordens/{id}`; valida no backend se a OS pertence ao proprietário **e** ao tenant. 403/404 para demais IDs, sem vazar informações. Resposta com campos autorizados e `podeAprovar:boolean` calculado no servidor.
4. **Aprovação**: `POST /portal/cliente/ordens/{id}/aprovar`. Validar vínculo proprietário/tenant, versão do orçamento, prazo/status e permissões; registrar data, hora e auditoria. Tratar concorrência (409), duplo clique/idempotência e orçamento já alterado.
5. **Revisão/recusa**: `POST /portal/cliente/ordens/{id}/revisar` recebe `{motivo}` de 5 a 1000 caracteres. Mesmas garantias de ownership, auditoria e idempotência.
6. **Consulta reduzida**: `POST /portal/cliente/consulta` recebe `{cpf,numeroOs,codigoAcesso}`; código individual imprevisível, expira/revoga, rate limit por identidade/IP, resposta mínima e equivalente para buscas inválidas. CPF e OS sozinhos não devem autorizar o detalhamento.

## Variáveis de ambiente do BFF

```bash
PORTAL_BACKEND_URL=https://api.seudominio.com
PORTAL_CUSTOMER_LOGIN_ENDPOINT=/portal/cliente/auth/login
PORTAL_CUSTOMER_HISTORY_ENDPOINT=/portal/cliente/ordens
PORTAL_CUSTOMER_DETAIL_ENDPOINT=/portal/cliente/ordens/{id}
PORTAL_CUSTOMER_APPROVE_ENDPOINT=/portal/cliente/ordens/{id}/aprovar
PORTAL_CUSTOMER_REVISION_ENDPOINT=/portal/cliente/ordens/{id}/revisar
PORTAL_PUBLIC_LOOKUP_ENDPOINT=/portal/cliente/consulta
PORTAL_PUBLIC_LOOKUP_ENABLED=true
PORTAL_SESSION_MAX_AGE_SECONDS=3600
```

O BFF exige HTTPS em produção, falha fechado se não houver endpoint, não redireciona requisições de backend e não repassa corpo de erros de servidor. A sessão fica em cookie HttpOnly com SameSite=Lax; `PORTAL_SESSION_MAX_AGE_SECONDS` não pode exceder a expiração efetiva do token. Logout limpa cookie e deve ser complementado por revogação no backend quando essa API estiver disponível.

## Campos de OS lidos pela UI

`id` numérico, `numeroOs` (ou `numeroOS`), `status`, `veiculo` / `nomeVeiculo`, `placa`, `dataEntrada`, `previsaoEntrega`, `km`, `observacoes`, `valorTotal`, `valorServicos`, `valorPecas`, `valorDesconto`, `servicos[]`, `pecas[]`, `historicoStatus[]`, `podeAprovar`. Ausentes são mostrados como indisponíveis, nunca inventados.

Em produção, adicione política de CSP, monitoramento de erros sem PII, testes de segurança, sessão expirada, CSRF, rate limit, isolamento entre empresas A/B e aprovação concorrente. **Frontend migrado não significa backend integrado ou homologado.**
