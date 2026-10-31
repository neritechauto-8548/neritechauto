# ADM-004 — Inventário de referência visual e desenvolvimento de Funcionários

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
- Botão EPIs na listagem deliberadamente desabilitado: não há fluxo de EPI comprovado e não deve apontar para um recurso falso.

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

1. Criar relacionamento e catálogo persistidos de **EPIs por funcionário**, entregas/validade e ações de registro.
2. Criar **contatos múltiplos por funcionário**, WhatsApp e seleção de contato principal com modelo autorizado, persistência e testes.
3. Resolver **empresas vinculadas** com modelo de autorização e regras de multiempresa sem acesso cruzado indevido.
4. Harmonizar separação entre cadastro básico e dados profissionais sem burlar regras RH/legais; revisar os campos obrigatórios em conjunto com ADM-004.
5. Integrar **criação de usuário em fluxo separado** à experiência do perfil, respeitando política de senha/RBAC, sem duplicar usuários.
6. Melhorar fidelidade visual do formulário profissional, abas Geral/Dados Profissionais/EPIs/Contatos, relatórios completos, busca no modal colunas e suporte mobile/teclado.
7. Executar testes ponta a ponta, comparar screenshots 320/768/1440 e WCAG 2.2 AA; os testes de compilação e contrato HTTP/tenant do workflow focado já passaram.

Situação geral: **implementação parcial, integração principal de listagem e CRUD reaproveitada, homologação pendente**.
