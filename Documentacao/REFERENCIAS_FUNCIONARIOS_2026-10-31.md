# Gestão de pessoas — Inventário de referência visual e desenvolvimento

Inclui ADM-004 (Funcionários/EPIs), ADM-005 (Cargos) e FIN-007 (Comissões).

Data: 2026-10-31. Origem: 12 capturas do módulo de gestão de pessoas UltraCar fornecidas pelo usuário. Destino: NERITECH AUTO, repositório `neritechauto-8548/neritechauto`, branch `master`. Estas capturas são referências de experiência, não código/fonte de autoridade técnica.

## Evidências visuais observadas

| Estado ou tela | Elementos observados | Evidência |
| --- | --- | --- |
| Listagem | abas Funcionários/Cargos/EPIs/Comissões; busca; novo funcionário; filtros; relatórios; colunas; tabela com nome/gênero/registro/função | Capturas 01 e 01.10 |
| Modal colunas | coluna Funcionário fixa, seleção, ordem, restaurar e salvar | Captura 01.1 |
| Drawer filtro | nome, função, registro, situação; cancelar/resetar/filtrar; ações fixas no rodapé | Captura 01.2 |
| Drawer cadastro | foto, CPF, nome, RG, nascimento, gênero, endereço e empresas vinculadas | Capturas 01.3 e 01.4 |
| Perfil Geral | foto, dados pessoais, endereço, empresas, ativar/desativar/criar usuário/remover | Captura 01.5 |
| Dados profissionais | função/cargo, registro, tipo, área, admissão/CTPS/PIS, salário, comissões, transporte, observações | Captura 01.6 |
| EPIs | lista vazia, seleção de empresa/EPI e datas de entrega/validade, múltiplos itens | Capturas 01.7 e 01.8 |
| Contatos | lista vazia, criar contato, nome/gênero/data/contatos alternativos | Capturas 01.9 e 01.10 |
| Criar usuário | login/e-mail/senha/confirmação/grupo/funcionário de origem | Captura 01.11 |

`Observado` significa que o elemento é visível na captura; não significa que fluxos ou validações do UltraCar foram comprovados.

## Implementação real reaproveitada

- `FrontEnd/src/app/routes/configuracoes/colaboradores/colaboradores.ts|html`: listagem Angular já existente, agora com busca, colunas e ordem configuráveis por empresa neste navegador, drawer avançado, paginação/ordenação, exportação **somente da página carregada** para CSV, novo funcionário/edição no drawer e inativação via API.
- `FrontEnd/src/app/routes/configuracoes/colaboradores/cadastro-colaborador/`: formulário Angular preexistente, com integração para criação/edição e foto, agora reutilizado também no drawer da listagem. Mantidas suas abas e a experiência de rota direta.
- `backend/src/main/java/com/neritech/saas/rh/`: ampliado GET `/v1/rh/funcionarios` para `nome`, `funcao`, `registro` combináveis com busca geral `nomeCompleto`, `status`, `cargoId` e `departamentoId`. Paginação server-side e tenant guard existentes preservados. Nenhuma migração nova.
- `FrontEnd/src/app/routes/configuracoes/usuarios/` e controladores de usuários: identificados para reuso, mas **não** reconstruídos para o modal de captura nesta entrega.
- A aba EPIs da listagem agora aponta para `/configuracoes/epis` e o cadastro do funcionário contém histórico e registro de entregas de EPI. Sem simulação de persistência.

## Regras e riscos

- Documento ADM-004 do projeto continua canônico. Funcionário e usuário são entidades distintas; inativar preserva históricos; permissions/RBAC e empresa são validados no servidor.
- O backend atual exige matrícula, data de admissão e endereço mínimo (CEP, logradouro, bairro, cidade, UF); o drawer de referência não comprova essas obrigatoriedades. **Não** gerar dados falsos para preencher campos. A migração para cadastro em etapas exige mudança explícita e compatível de DTO/validações/modelo.
- A imagem mostra associação de várias empresas; a entidade `Funcionario` usa `empresaId` único. Não adicionar relação multiempresa sem revisar permissões e escopo cruzado.
- Comissões são regras financeiras com módulo proprietário; não duplicar cálculo no perfil.
- Dados pessoais/salário: reforçar autorização por função e mascaramento em visualizações de menor privilégio.
- Um colaborador pode não ter login; eventual criação de usuário exige grupo/capability autorizados e vínculo auditado.

## QA e evidências

- Testes de contrato Angular estendidos: `funcionario.service.spec.ts`, conferindo cabeçalho e parâmetro de tenant.
- Teste multiempresa Spring estendido: `FuncionarioTenantIntegrationTest`, pesquisando nome/função/registro/situação e negando empresa alheia.
- Workflow: `.github/workflows/funcionarios-quality.yml` (push master: build Angular, teste de serviço e teste de integração Spring).
- **Execução CI confirmada:** [workflow Funcionários — filtros avançados e integração](https://github.com/neritechauto-8548/neritechauto/actions/runs/41202031201), HEAD testado `75ced46d6dcfc5ee3ceccc81c3e3f2bfd0386d7c`. Job Angular (compilação + teste HTTP): **success**; job Spring Boot (integração e tenant): **success**.
- **Screenshot de comparação, WCAG, breakpoints, teste de teclado completo e compatibilidade integral do cadastro no drawer: NÃO COMPROVADOS nesta edição.** Não homologar nem declarar equivalência de 100%.

## Pendências de paridade demonstráveis

1. **EPIs entregues:** catálogo e histórico básico já implementados e testados; faltam homologação visual, múltiplos itens em uma única submissão e regras adicionais de devolução/substituição/validade quando definidas no contrato.
2. Criar **contatos múltiplos por funcionário**, WhatsApp e seleção de contato principal com modelo autorizado, persistência e testes.
3. Resolver **empresas vinculadas** com modelo de autorização e regras de multiempresa sem acesso cruzado indevido.
4. Harmonizar separação entre cadastro básico e dados profissionais sem burlar regras RH/legais; revisar os campos obrigatórios em conjunto com ADM-004.
5. Integrar **criação de usuário em fluxo separado** à experiência do perfil, respeitando política de senha/RBAC, sem duplicar usuários.
6. Melhorar fidelidade visual do formulário profissional, abas Geral/Dados Profissionais/Contatos, relatórios completos, busca no modal colunas e suporte mobile/teclado.
7. Executar testes ponta a ponta, comparar screenshots 320/768/1440 e WCAG 2.2 AA; os testes de compilação e contrato HTTP/tenant do workflow focado já passaram.

Situação geral: **implementação parcial, integração principal de listagem e CRUD reaproveitada, homologação pendente**.

## Complemento — Capturas 02, 03 e 04 (Cargos, EPIs e Comissões)

**Capturas analisadas em 2026-10-31:** `02 - Cargo`, `02.1 - Cargo`, `03 - EPI's`, `03.1 - EPI's`, `03.2 - EPI's`, `04 - Comissão` até `04.5 - Comissão`. As imagens exibem apenas estados; nenhuma função externa é considerada confirmada sem evidência.

| Área | Elementos observados | Estado da NERITECH AUTO |
| --- | --- | --- |
| Cargos | listagem com Descrição/Código/Tipo, pesquisa e novo cargo; modal com Descrição e CBO | Listagem Angular refatorada; modal com nome/CBO obrigatórios na UI, CRUD, busca paginada, edição, histórico e inativação pela API existente; dados administrativos avançados recolhidos em seção complementar |
| EPIs | listagem com descrição/CA e drawer com nome, CA, categoria, fabricante, tamanho, especificações e descrição | Catálogo real com `V293__rh_epis_catalogo_entregas.sql`, API `/v1/rh/epis`, tela Angular em `/configuracoes/epis`, edição/inativação lógica e auditoria |
| EPIs por colaborador | selecionar empresa e EPI, data de entrega, validade e múltiplas seleções | Aba EPIs no cadastro, endpoints `/v1/rh/funcionarios/{id}/epis` para consulta e registro real; empresa vem da sessão JWT. Cada entrega é gravada individualmente, sem lote |
| Comissões | listagem por funcionário/OS/cliente/placa/empresa, valores, estados e pesquisa/filtro | `/financeiro/comissoes` exibe agora registros individuais reais e paginação; API de `fin_comissoes_funcionarios` com empresa JWT, busca por funcionário, OS, situação e período; origem textual detalhada ainda depende da modelagem completa |
| Quitação e estorno | drawer de pagamento e observação; opção conta a pagar; estado quitado, confirmação de estorno | Aprovação, registro de quitação e estorno por comissão via transações auditadas e bloqueio pessimista; geração/liquidação de contas a pagar **não habilitada**, nunca simulada |

### Principais arquivos
- `FrontEnd/src/app/routes/configuracoes/cargos/cargos.ts`: modal da referência e listagem conectados ao `CargoService` existente.
- `FrontEnd/src/app/routes/configuracoes/epis/*`: tela, serviço tipado e teste Angular.
- `FrontEnd/src/app/routes/configuracoes/colaboradores/cadastro-colaborador/*`: aba EPIs e histórico de entregas.
- `FrontEnd/src/app/routes/financeiro/comissoes/*`: listagem real, filtros, relatórios PDF existentes, drawers de quitação/estorno, serviço tipado e teste HTTP.
- `backend/src/main/java/com/neritech/saas/rh/controller/EpiController.java` e `service/EpiService.java`: API catálogo/entregas/auditoria, isolamento por empresa.
- `backend/src/main/java/com/neritech/saas/financeiro/`: contrato e mapper, filtros, autorizações, transações de aprovar/quitar/estornar e validação de vínculos cruzados.
- `backend/src/main/resources/db/migration/V293__rh_epis_catalogo_entregas.sql`: catálogo e entregas sem exclusão física.
- `backend/src/test/java/com/neritech/saas/rh/EpiTenantIntegrationTest.java` e `backend/src/test/java/com/neritech/saas/financeiro/ComissaoQuitacaoTenantIntegrationTest.java`.

### Evidências de validação desta entrega
- Workflow GitHub Actions: [Gestão de pessoas — Cargos EPIs Comissões](https://github.com/neritechauto-8548/neritechauto/actions/runs/41203657347).
- **Código exato testado:** `649310c888905ab2313ae12abdcdd2b7cd79e4fd` (`master`).
- **Angular:** build da aplicação e testes de serviço de Cargos/EPIs/Comissões concluídos com sucesso.
- **Spring Boot:** 3 testes de integração (`CargoTenantIntegrationTest`, `EpiTenantIntegrationTest`, `ComissaoQuitacaoTenantIntegrationTest`): 3 executados, 0 falhas, 0 erros, 0 ignorados; Maven `BUILD SUCCESS`.
- O teste de EPIs aplica a migração diretamente via `@Sql` porque o perfil de teste não executa as migrações Flyway automaticamente; execução em ambiente de deploy ainda deve ser validada.
- Nenhuma comparação de screenshots, validação WCAG 2.2 AA completa, testes E2E de jornada ou evidência de deploy/homologação foi produzida.

### Diferenças e pendências que não devem ser ocultadas

1. **Cargos:** a coluna `Tipo` da referência não tem contrato equivalente comprovado no backend; mantidos funcionários/situação até decisão de modelagem. Ainda sem comparação pixel a pixel.
2. **EPIs:** a referência mostra seleção por empresa no drawer; a NERITECH usa a empresa autenticada, sem permitir troca arbitrária. Validação externa de certificado de aprovação, devolução e substituição ainda não implementadas.
3. **Comissões:** origem completa da OS (placa/cliente, datas de autorização/fechamento, valor líquido por item), quitação em lote, contas a pagar automáticas, pagamento parcial e reversão bancária/contábil não foram implementados. Precisam de contrato transacional entre OS, faturamento e Financeiro antes de habilitação.
4. **Quitação:** a ação implementada registra quitação no registro de comissão após aprovação e, quando vinculada a fatura, exige recebimento integral confirmado. **Não representa transferência de dinheiro nem movimentação financeira automática**. O backend recusa `gerarContaPagar=true` com HTTP 422.
5. A experiência visual em mobile/tablet, foco/teclado completo e comparação visual 320/768/1440 ainda dependem de homologação prática. Não classificar como 100% concluído.

**Situação de desenvolvimento:** telas e integrações principais dos três módulos implementadas e aprovadas pelos testes focados, com paridade operacional e visual **parcial**, ainda sem homologação integral.

## Revisão do contrato das 14 capturas — 31/10/2026

O usuário autorizou expressamente a remoção física de colunas legadas do projeto inicial e definiu que todo o novo código de domínio e nomes de tabelas e colunas sejam em português.

### Execução técnica

1. `V294`: cadastro básico sem obrigação de data de admissão, novos campos profissionais e tabelas `rh_funcionario_contatos`/`rh_funcionario_contato_meios` em português.
2. `V295`: coluna `perfil_profissional`.
3. `V296`: remoção física de 26 colunas legadas (estado civil, filiação, benefícios não exibidos, dados bancários, CNH etc.). Os campos técnicos necessários aos módulos dependentes foram preservados.
4. `FuncionarioRequest`/`FuncionarioResponse`, entidade `Funcionario` e serviço: contratos ajustados, validador correspondente e matrícula interna gerada apenas como identificador técnico.
5. `FuncionarioContatoController`/`FuncionarioContatoService`: CRUD real de contatos, meios e principal único, protegido por empresa e auditado.
6. Cadastro Angular reescrito segundo as telas: cabeçalho com identidade e ativação, abas Geral/Dados Profissionais/EPIs/Contatos, drawers de cadastro de funcionário/EPIs/contato e modal criar usuário.
7. `FuncionarioReferenciaIntegracaoTest`: valida alterações de schema e fluxo de contatos em PostgreSQL 16/Testcontainers, incluindo acesso entre empresas negado. Workflow `funcionarios-quality.yml`.

### Restrições relevantes

- O campo `login` separado da imagem ainda **não possui autenticação própria** na implementação atual: o backend utiliza e-mail como identificador. A interface bloqueia um login divergente e informa a regra, evitando gravação enganosa.
- A seleção de outra empresa não é permitida, pois a modelagem atual de funcionário tem uma empresa proprietária e isolamento estrito. A ficha mostra o nome e CNPJ reais da empresa autenticada.
- A seleção de múltiplos EPIs funciona mediante gravações individuais; **não** é transação atômica de lote, podendo ocorrer gravação parcial com erro explícito.
- A coluna `tipo` de profissional é texto livre enquanto não houver catálogo de tipos definido; o cargo usa os registros da API existente.
- A remoção física em banco de produção **não foi executada nesta conversa**; o que foi publicado é a migração versionada, que será aplicada pelo pipeline de deploy compatível com Flyway.
- A semelhança visual de 100% ainda depende de screenshots comparativos em 320, 768 e 1440 px e validação de teclado/WCAG. Não alegar equivalência perfeita sem verificação.
- Os campos técnicos `empresa_id`, `usuario_id`, `matricula` e controles de status/auditoria continuam no banco, mesmo sem todos aparecerem na ficha.

Documentação canônica de nomenclatura: `Documentacao/PADRAO_PORTUGUES_BANCO_BACKEND.md`.
