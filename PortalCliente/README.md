# Portal do Cliente — NERITECH AUTO

Aplicação independente para os proprietários de veículos, agora em **React 19 + Next.js 16 (App Router)**. O sistema interno continua em Angular/Spring Boot e a landing page Next.js permanece isolada em `landpage/`.

## Rotas

| Rota | Descrição |
| --- | --- |
| `/` | Redireciona para `/consulta` |
| `/consulta` | Consulta reduzida por CPF, número da OS e código de acesso |
| `/login` | Login **específico para proprietário**, sem reutilizar o login da oficina |
| `/historico` | Ordens pertencentes ao cliente autenticado |
| `/os/[id]` | Detalhes, peças, serviços, andamento e decisão de orçamento autorizada |

A autenticação e as consultas são encaminhadas ao Spring Boot pelo **BFF do Next.js** em `/api/portal/**`, com cookie HttpOnly/SameSite e chamadas de servidor a servidor. Nenhum JWT é armazenado em localStorage nem entregue ao JavaScript do navegador.

## Começar

Requer Node.js 22 ou superior.

```bash
cd PortalCliente
npm ci
cp .env.example .env.local
npm run dev
```

Acesse http://localhost:3002. Para validar:

```bash
npm run typecheck
npm run build
```

## Configuração de integração (não habilitada automaticamente)

No `.env.local`, informe `PORTAL_BACKEND_URL` e as **rotas próprias do portal** disponíveis na API. Consulte [INTEGRATION.md](./INTEGRATION.md) para contrato, autorização, auditoria e configurações.

**IMPORTANTE:** o backend desta branch ainda não implementa os endpoints de cliente exigidos por este portal. Os endpoints `/auth/login`, `/public/ordem-servico/**` e `/v1/ordens-servico/cliente/:id` são internos e **não devem ser expostos aos clientes**. Por segurança, todas as integrações não configuradas respondem com indisponibilidade explícita (503). Não existe modo mock, token em localStorage nem aprovação fingida.

Para consulta reduzida, somente habilite `PORTAL_PUBLIC_LOOKUP_ENABLED=true` após implementar um endpoint POST que valide simultaneamente CPF, OS, código de acesso, expiração, tenant, rate limit e anti-enumeração.

## Visual e UX

- Identidade NERITECH AUTO com tema claro, branco, grafite e acento `#635BFF`.
- Formulários com validação, mensagens claras, loading, empty, 401/403/404/409 e 503.
- Cards móveis, status acessível por texto, detalhes e timeline por dados reais.
- Aprovação somente se a API de cliente retornar `podeAprovar: true` e o endpoint seguro estiver configurado.
- Sem carregamento externo de fonte; foto existente em `public/bg-oficina.png`.

## Implantação

Defina o diretório raiz do projeto Vercel como `PortalCliente`. Configure as variáveis de ambiente **somente no servidor**. Use HTTPS no backend em produção. O CI específico em `.github/workflows/portal-cliente-quality.yml` executa `npm ci`, TypeScript e build.

## Limites desta migração

Este commit migra **somente o frontend** e prepara o BFF. Falta implementar/homologar a autenticação de clientes, ownership de OS e endpoints de consulta/aprovação no Spring Boot. Nenhuma funcionalidade sensível é marcada como operacional até esses testes de ponta a ponta.
