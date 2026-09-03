# NeriTech Auto — Contexto Transversal para Agentes

> Documento de consulta sob demanda. **Não leia este arquivo inteiro em toda tarefa.** Pesquise por seção e carregue apenas o trecho necessário ao módulo/risco atual.

## 1. Fontes de verdade

1. Decisão explícita do usuário na tarefa atual.
2. Notion oficial: `NERITECH — Documentação Oficial` (`3bb27279-1906-815d-b711-d225de4c2b06`).
3. Especificação canônica `TELA-*` do módulo/tela atual.
4. `DESIGN.md` para contrato visual e UX.
5. `CONVENCOES_CODIGO.md` para idioma e convenções de código.
6. `DX.md` para arquitetura/ambiente quando a tarefa exigir.
7. `BRAND.md` somente quando identidade/marca/copy forem relevantes.
8. Código atual de `feature/neritech-auto-rebuild` como evidência do que existe.
9. Legado apenas como referência, nunca como autoridade.

Em conflito, uma especificação oficial mais recente prevalece sobre código antigo.

## 2. Economia de contexto

- Não percorrer toda a árvore do Notion.
- Não carregar todos os documentos raiz por padrão.
- Começar pelo código do slice/tela solicitado.
- Usar busca por `TELA-*`, nome do módulo, regra, endpoint, entidade ou campo específico.
- Para UI, ler somente as seções relevantes de `DESIGN.md` e a `TELA-*` correspondente.
- Para regra de negócio, buscar apenas a página/regra do módulo atual.
- Para naming/banco/comentários, consultar `CONVENCOES_CODIGO.md`.
- Para infraestrutura/execução, consultar somente a seção necessária de `DX.md`.
- Consultar Notion apenas quando houver lacuna, conflito ou requisito não representado nos arquivos locais.
- Se o Notion não estiver acessível, não bloquear trabalho reversível: usar as regras locais e registrar a lacuna material.

## 3. Idioma obrigatório

A regra completa está em `CONVENCOES_CODIGO.md`.

Resumo obrigatório para código novo/reconstruído:
- domínio NeriTech em português brasileiro;
- classes, entidades, DTOs, métodos, variáveis, constantes e enums próprios em português;
- tabelas e colunas PostgreSQL em português (`snake_case`);
- migrations Flyway com descrição em português;
- comentários, JavaDoc, TSDoc e documentação inline em português;
- cenários e nomes de testes de domínio em português;
- mensagens de validação e domínio em português.

Exceções: palavras-chave da linguagem, APIs de framework, protocolos, headers, claims, bibliotecas e contratos externos imutáveis (Stripe, Stone, fiscal etc.). Não fazer tradução global do legado sem migration/versionamento compatível.

## 4. Stack e direção técnica

### Frontend
- Angular 20.x, standalone components.
- PrimeNG 20.x.
- Tailwind CSS para layout, spacing e responsividade.
- Preset semântico próprio NeriTech; não usar Aura default como identidade.
- Tabler Icons via wrapper local `NeriTechIcon` enquanto Angular 20 for a base.
- Não introduzir Lucide, PrimeIcons, Material Symbols, Font Awesome ou Angular Material em UI reconstruída sem requisito explícito de compatibilidade.

### Backend
- Java 21.
- Spring Boot 3.2.x.
- Spring Security/JWT.
- PostgreSQL + Flyway.
- OpenAPI.
- Multi-tenancy.
- JasperReports quando aplicável.

## 5. Modelo de UX

Fórmula de referência:

`NeriTech Auto = UX operacional Shopmonkey + disciplina visual Stripe + domínio brasileiro de oficinas + identidade NeriTech`.

Regras rápidas:
- interface Minimal Enterprise;
- contexto antes de ação;
- próxima ação segura e clara;
- preservar continuidade Cliente → Veículo → Orçamento → OS → Execução → Financeiro → Entrega;
- uma ação primária dominante por contexto;
- estados de loading, vazio, erro, bloqueio e sucesso explícitos;
- desktop, tablet e mobile previstos;
- não fabricar KPI, gráfico, valor financeiro ou timeline inexistente no backend.

## 6. Segurança e multi-tenancy

- Tenant só é autorizado pelo backend autenticado + vínculo persistido usuário/empresa.
- `localStorage`, query params e headers do navegador nunca concedem tenant.
- `X-Tenant-Id`, se mantido por compatibilidade, apenas seleciona tenant já autorizado.
- Backend é autoridade final de permissões; menu/guards frontend são UX.
- Deny by default.
- Não introduzir `empresaId: 1`, `tenantId` de browser, credenciais hardcoded ou bypass ADMIN implícito.
- Acesso cross-tenant nunca pode vazar existência/dados de outra empresa.
- Minimizar PII em DTOs de leitura.
- Não registrar tokens, credenciais, PII ou payloads sensíveis em logs.

## 7. Integridade de dados já estabelecida

- Exclusão operacional de cliente/veículo é desativação lógica; histórico é preservado.
- Placa é normalizada e única dentro do tenant; pode repetir entre tenants.
- Chassi/VIN, quando presente, é normalizado e único dentro do tenant.
- Regressão de hodômetro não pode sobrescrever silenciosamente.
- Consulta externa de veículo é sugestão; dado interno canônico prevalece.
- Migrações não devem apagar/mesclar dados de produção silenciosamente.

## 8. Permissões

Persistência ainda contém códigos legados como `GERAL_USUARIO`, `CLIENTE_CRIAR`, `CLIENTE_EDITAR`, `CLIENTE_EXCLUIR`, `VEICULO_CRIAR`, `VEICULO_EDITAR`, `VEICULO_EXCLUIR` e outros.

Não inventar novo código de permissão persistido apenas para satisfazer uma tela. Mapear ou migrar formalmente quando necessário.

## 9. Ordem de Serviço — checkpoint atual

Já existe na rebuild:
- Cockpit 360;
- execução/apontamentos;
- itens/peças;
- diagnósticos;
- checklist;
- evidências;
- adicionais;
- diário/comentários;
- financeiro;
- comunicação;
- revisão de fechamento;
- conclusão operacional com `If-Match`, `Idempotency-Key` e snapshot auditável.

Regra crítica:
- concluir operacionalmente **não** gera automaticamente pagamento, contas a receber, fatura ou documento fiscal;
- faturamento é etapa separada;
- emissão fiscal é decisão separada.

Pendências prioritárias conhecidas:
1. comando canônico para concluir serviço depois de encerrar o relógio;
2. bloquear mutações incompatíveis após `CONCLUIDA_OPERACIONAL`;
3. implementar `release-to-billing` separadamente;
4. validar build/testes/CI quando o bloco estiver pronto.

## 10. Riscos legados conhecidos

### Orçamento
Remover autoridade de `empresaId` fornecido/hardcoded no navegador e geração de identificador/número autoritativo no cliente. Backend deve possuir tenant e numeração.

### Agendamento
Remover `empresaId` do navegador como autoridade e derivar tenant no backend.

### Trial/assinaturas/planos
Branches históricas podem conter implementações úteis de consistência de trial/assinatura, acesso por plano e limite de usuários. Não fazer merge cego; transportar apenas o que for superior/compatível com a rebuild.

## 11. Definição de pronto por slice

1. Especificação relevante consultada de forma direcionada.
2. Código existente analisado antes de criar algo novo.
3. Reuso/refatoração preferidos a duplicação.
4. Contrato backend/frontend coerente.
5. Segurança, tenant e permissão verificados.
6. UI responsiva e estados reais cobertos.
7. Testes focados adicionados/atualizados.
8. Formatter/linter/type-check/build relevantes executados quando o ambiente permitir.
9. Falhas introduzidas pelo trabalho corrigidas.
10. Commit focado e descritivo.
11. Não reportar como pronto o que não foi verificado.

## 12. Regra de progresso

Não aumentar percentual apenas porque arquivos foram criados. Progresso exige comportamento implementado e verificável. O `Relatorio_Implementacao_NeriTech.md` é informativo; confirme no código antes de confiar em percentuais.
