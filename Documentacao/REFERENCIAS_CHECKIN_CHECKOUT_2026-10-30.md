# Referência visual — Entrada e saída do veículo (30/10/2026)

**Fonte:** duas capturas fornecidas pelo usuário nesta conversa, denominadas “01 - check-in.png” e “01 - check-out.png”. **Status:** análise de referência, NÃO implementação/homologação. **Branch examinada:** master.

## Observado nas capturas
- **Check-in / Entrada do veículo:** diálogo branco sobre backdrop escuro, topo pequeno “Check-in”, título “Entrada do veículo”, instrução cinza, fechamento no canto superior direito, seis cartões de ação em coluna: **Gerar diagnóstico**, **Gerar orçamento**, **Abrir OS**, **Gerar atividade**, **Gerar checklist**, **Gerar agendamento** (badge “Em breve”). Cada cartão possui ícone em fundo cinza-claro, título preto e descrição cinza. Na captura, **Abrir OS** apresenta borda azul e fundo azul muito claro (estado visual selecionado/foco não determinável).
- **Check-out / Saída do veículo:** diálogo branco com topo pequeno “Check-out”, título “Saída do veículo”, instrução cinza e fechamento; dois cartões: **Gerar financeiro** (“Lance o recebimento no contas a receber e conclua o ciclo da ordem.”) e **Gerar nota fiscal** (“Emita a nota fiscal de serviço ou venda vinculada à ordem de saída.”).
- Layout: cantos levemente arredondados, divisórias finas, espaçamento vertical uniforme, fundo modal escurecido, sem campos de formulário, tabelas ou botões de confirmação apresentados. O comportamento após clicar em cada cartão **não está demonstrado**.

## Referências de código efetivamente encontradas na master
- `FrontEnd/src/app/routes/os/os.routes.ts`: `/os/cadastro`, `/os/cadastro/:id`, `/os/visualizar-os/:numero`; listagem `ordem-servico.ts` usa `OS_INCLUIR` e serviço `OrdemServicoService`.
- `FrontEnd/src/app/routes/orcamento/orcamento.routes.ts`: rota interna `cadastro-orcamento`; não assumir que seu contrato aceita veículo pré-preenchido.
- `FrontEnd/src/app/routes/agendamento/agendamento.routes.ts`: `cadastro`; não assumir criação persistida ao clicar.
- `FrontEnd/src/app/routes/financeiro/financeiro.routes.ts`: `receber`; não é evidência de geração automática de título.
- `FrontEnd/src/app/routes/fiscal/fiscal.routes.ts`: `nfse`, `nfe`; rota de listagem não equivale a emissão.
- `FrontEnd/src/app/routes/patio/patio.service.ts` e `Documentacao/notion/telas/gestao-patio/PATIO-001.md`: contratos do pátio. Ações do modal devem usar a mesma OS, veículo e empresa, sem inventar um segundo cadastro ou um módulo independente.

## Contrato de implementação proposto (pendente de prova pelo código)
1. Criar **um único componente de seleção contextual**, parametrizado por modo `entrada|saida` e identificação do veículo/OS; não duplicar layout.
2. Abrir o componente somente a partir de contexto com vínculo conhecido; exigir contexto válido na ação e permissão no backend. Fechar via botão, Esc e interação acessível; restaurar foco.
3. A seleção apenas **inicia o fluxo existente**: não persistir check-in, OS, financeiro ou emissão fiscal pelo clique no cartão. Se houver ação real de entrada/saída, confirmar a mutação no servidor, registrar ator, empresa, horários e idempotência, só então atualizar tela.
4. Conferir se diagnóstico, checklist e atividade possuem criadores e contratos reais antes de habilitar. Agendamento marcado “Em breve” na referência deve ficar inequivocamente indisponível se a integração contextual não estiver pronta, mesmo havendo cadastro de agenda genérico.
5. Check-out deve validar estado da OS, pendências e autorização antes de **gerar financeiro** ou **gerar nota fiscal**. Emissão e liquidação só podem confirmar sucesso após resposta real do backend/provedor.
6. Integrar com os componentes existentes e testar isolamentos A/B, 401/403/409/422, repetição de clique, timeout, reload, teclado, responsividade 320/768/1440 e regressão de OS/agenda/pátio.
7. Medidas dos elementos devem ser verificadas em captura de navegador à mesma escala antes de declarar paridade; a referência não prova fontes instaladas nem comportamentos ocultos.

## Inventário e evidências
| Referência | Módulo | Estado | Evidência |
| --- | --- | --- | --- |
| Check-in | Oficina / Recepção / Pátio | Captura analisada; implementação pendente | Imagem fornecida, seis cartões observados |
| Check-out | Oficina / Entrega / Financeiro / Fiscal | Captura analisada; implementação pendente | Imagem fornecida, dois cartões observados |

**Validação efetiva neste registro:** inspeção estática pontual dos arquivos citados na master. **Não executados:** build Angular, testes Maven, E2E, inspeção visual da aplicação ou operação de API. **Fidelidade:** apenas requisitos observáveis documentados; nenhuma equivalência funcional afirmada.
