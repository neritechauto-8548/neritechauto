# Gestão de pessoas — confronto das referências visuais (31/10/2026)

**Repositório:** `neritechauto-8548/neritechauto`  
**Branch:** `master`  
**Imagens:** 02 e 02.1 (Cargos), 03, 03.1 e 03.2 (EPIs), 04 a 04.5 (Comissões).  
**Referência:** interface UltraCar, utilizada como orientação visual/operacional; não copiar marca de terceiros.

## Inventário observado e implementado

| Módulo | Referência visual | Implementação atual |
| --- | --- | --- |
| Cargos | Breadcrumb, abas, título, busca, tabela Descrição/Código/Tipo, modal apenas Descrição do cargo e Código CBO, ações Cancelar e Cadastrar cargo | Listagem e modal Angular reescritos, buscas paginadas e CRUD conectado; coluna `tipo` real e opcional no banco (V297); não se atribui um tipo fictício; inativação pela API preexistente ainda existe fora da listagem |
| EPIs | Listagem Descrição/Certificado de aprovação, busca, cadastro lateral com nome/CA/categoria/fabricante/tamanho/especificações/descrição | Layout simplificado; campos de formulário, CRUD e inativação lógica conectados ao catálogo persistente do módulo RH |
| Comissões | Listagem por Funcionário, OS, Placa, Empresa, Cliente, Autorização, Fechamento, Valor Líquido, Valor Base, Comissão e Situação; seleção individual/em lote, relatório, filtro avançado, quitação e estorno | Layout atualizado; API busca por número real da OS, cliente e placa, com consulta vinculada à empresa; contexto real obtido de OS/veículos/clientes; quitação individual ou lote em transação, dados persistidos e auditados; relatório mantém serviço PDF existente |
| Geração financeira | Checkbox gerar conta a pagar; plano de contas e tipo de documento; aviso de documento em aberto | `V298__vinculo_comissao_conta_pagar.sql`, título real em `fin_contas_pagar`, plano validado na empresa; chave única de vínculo com comissão; título com situação `ABERTO` |
| Estorno | Dialog de confirmação e mudança de status | Estorno auditado com bloqueio pessimista; se título ainda está aberto e não movimentado, ele é cancelado na mesma transação; se já existe pagamento/movimentação, estorno recusado para reconciliação real |

## Arquivos principais

- `FrontEnd/src/app/routes/configuracoes/cargos/cargos.ts`
- `FrontEnd/src/app/routes/configuracoes/cargos/cargo.service.ts`
- `FrontEnd/src/app/routes/configuracoes/epis/epis.html` e `epis.ts`
- `FrontEnd/src/app/routes/financeiro/comissoes/comissoes.html`, `comissoes.ts` e `comissao-financeira.service.ts`
- `backend/src/main/java/com/neritech/saas/rh/domain/Cargo.java` e `dto/CargoResponse.java`
- `backend/src/main/java/com/neritech/saas/financeiro/controller/ComissaoFuncionarioController.java`
- `backend/src/main/java/com/neritech/saas/financeiro/service/ComissaoFuncionarioService.java`
- `backend/src/main/java/com/neritech/saas/financeiro/dto/ComissaoFuncionarioResponse.java` e `mapper/ComissaoFuncionarioMapper.java`
- `backend/src/main/resources/db/migration/V297__tipo_cargos_referencia.sql`
- `backend/src/main/resources/db/migration/V298__vinculo_comissao_conta_pagar.sql`
- `backend/src/test/java/com/neritech/saas/rh/CargoTenantIntegrationTest.java`
- `backend/src/test/java/com/neritech/saas/financeiro/ComissaoQuitacaoTenantIntegrationTest.java`

## Validação verificada

- **Código testado:** `dae0f2cc25fbc8095ab732853ae37842d87808d5`.
- **Workflow:** https://github.com/neritechauto-8548/neritechauto/actions/runs/41208899216
- Jobs de frontend e backend concluídos com sucesso.
- Inclui testes existentes de Cargos e EPIs, além de testes de geração de conta pendente, estorno, lote transacional, permissões e acesso entre empresas.
- Build/deploy backend anterior concluído com sucesso para o código do módulo backend em https://github.com/neritechauto-8548/neritechauto/actions/runs/41208765048.
- Sem comprovação de deploy do frontend nem comparação visual por screenshots em navegador.

## Limites declarados sem simular funcionalidade

1. **Não há comprovação de paridade visual 100%.** A comparação pixel a pixel, tipografia real, breakpoints 320/768/1440, navegação por teclado e WCAG 2.2 AA ainda demandam homologação em navegador.
2. **Liquidação automática de contas a pagar não foi habilitada.** Sem conta bancária/caixa, comprovante e reconciliação, marcar conta como paga produziria um pagamento falso; backend rejeita com 422 e frontend explicita a restrição.
3. **Data de Fechamento de OS não foi inferida da data de entrega.** A coluna permanece vazia quando não há informação de fechamento confiável no contrato.
4. **Tipo de cargo:** a referência exibe a coluna, mas não define seu preenchimento no modal; coluna criada opcional, nenhum valor artificial.
5. **Limpeza completa do schema legado de Cargos e Comissões:** não concluída. Os campos extras foram removidos das telas alvo, mas atributos de domínio herdados com possíveis dependências de folha/financeiro continuam até revisão de consumidores. Não executar `DROP COLUMN CASCADE` sem mapeamento de dependências.
6. **Quitação administrativa não significa transferência bancária.** A criação da conta a pagar representa um título em aberto; somente o módulo financeiro poderá liquidá-lo.
7. **Valores líquidos/OS:** onde o relacionamento de OS existe, o valor exibido é `ordens_servico.valor_total`; registros sem OS podem mostrar informação ausente. Não usar dados fictícios.

**Status:** principais fluxos das imagens conectados e testados, fidelidade visual ainda não homologada, integrações bancárias não simuladas.
