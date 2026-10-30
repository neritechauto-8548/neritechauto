# NERITECH AUTO — Matriz de paridade visual e funcional (branch `master`)

## Escopo e regra de evidência
Este documento registra as referências visuais recebidas e a implementação própria da NERITECH AUTO. Fotografias, marcas, textos proprietários e código-fonte de terceiros não são copiados. **Código publicado não equivale a teste executado nem a fidelidade homologada.**

| Referência recebida | Rota do sistema | Estado | Arquivos-base |
| --- | --- | --- | --- |
| `00 - LOGIN.png` (1792 × 859 aprox.) | `/auth/login` | Interface implementada; homologação pendente | `FrontEnd/src/app/routes/sessions/login/login.{html,scss,ts}` |
| `00 - RECUPERAR SENHA.png` (1792 × 859 aprox.) | `/auth/recover` | Interface implementada; homologação pendente | `FrontEnd/src/app/routes/sessions/recover/recover.{html,scss,ts}` |

## AUTH-001 — Login

**Observação visual:** divisão aproximadamente 50/50; fundo grafite no formulário à esquerda; fotografia de oficina à direita com degradê azul na parte superior e verde inferior; formulário compacto, links azuis, botão verde-vivo, rodapé legal.

**Implementação:** layout split-screen responsivo, tipografia compacta, wordmark NERITECH próprio, imagem já presente em `FrontEnd/public/images/login.png`, fundo e sobreposição cromática, formulário funcional com validação, alternar senha, lembrar e-mail, envio antiduplo clique, estados de erro, links para recuperação, teste grátis, ajuda e privacidade.

**Contrato backend real:** `POST /auth/login` com `{email, senha}`. O campo visual "Usuário" explica que o identificador **atualmente é o e-mail**; o cadastro não possui coluna `login` distinta. Não oferecer autenticação por username arbitrário sem alteração específica e validação de isolamento multiempresa.

**A implementar/verificar:** comparação visual por captura de navegador nas mesmas dimensões, imagem e recorte reais do ambiente de deploy, CI Angular, comportamento em 320/768/1440, zoom 200%, teclado, verificação de sessão e tenant na integração.

## AUTH-002 — Recuperação de senha

**Observação visual:** mesmo shell, título e link para área de acesso, campos `E-mail` e `Login`, ajuda para login esquecido, ação solicitar recuperação, central de ajuda, política de privacidade.

**Implementação:** mesmo shell, e-mail validado, submissão ao backend `POST /auth/recover-password`, resposta anti-enumeração, 429/503/offline/timeout, confirmação genérica apenas após resposta real, link de retorno e ajuda interativa que explica o identificador atual.

**Diferença intencional e explícita:** referência possui dois campos separados (`E-mail` e `Login`). A NERITECH atual só possui e-mail como identificador real; portanto não foi criado campo `Login` sem efeito no servidor. Qualquer paridade de dois identificadores exige migração de dados, regras de unicidade por tenant e especificação de API; isso não está comprovado pelas imagens.

**Segurança preservada:** token opaco com hash armazenado e validade de 30 minutos; recuperação com resposta genérica; limitação de requisições; sem sucesso fictício quando o provedor de e-mail está indisponível.

## Checklist de verificação
- [x] Identificar componentes, rotas, backend e testes existentes.
- [x] Atualizar HTML, SCSS e componente de recuperação na `master`.
- [x] Preservar contratos de autenticação e telas de redefinição de senha.
- [x] Adicionar testes unitários focados para links e ajuda.
- [ ] Executar testes Angular e backend (ainda sem execução comprovada nesta alteração).
- [ ] Confirmar deploy e captura visual em produção ou homologação.
- [ ] Medir diferenças de layout/pixels e ajustar imagem, recorte e responsividade.
- [ ] Homologar o fluxo real com envio SMTP de teste, backend e empresa ativa.
- [ ] Decidir se a NERITECH terá username separado do e-mail.

**Branch de trabalho:** `master`. Não efetuar merge automático para `main` ou `feature/develop`.

## Próximas imagens
Registrar uma linha por nova tela, com todos os estados visuais (listagem, edição, modais, erros), regras comprovadas, impactos de API, testes e divergências. Comparar primeiro com implementações existentes e **não** criar duplicatas.

---

## Lote 02 — HOME / Menu global (imagens de 30/10/2026)

**Arquivos de referência recebidos:** `00 - INICIO.png`, `00.1 - INICIO.png`, `00.2 - INICIO.png`, `01 - Menu.png`, `01.1 - Menu.png`, `01.1 - Menud.png`, `01.1 - Menup.png`, `01.2 - Menu.png`, `01.2 - Menu(1).png`, `01.12 - Menu.png`, `012 - Menu.png`, `013 - Menu.png`, `014 - Menu.png`, `01.3 - Menu superior.png`, `01.3 - Menu superior(1).png`, `01.4 - Menu superior.png`, `01.4 - Menu superior(1).png`.

**Observações comprovadas nas imagens:**
- Topbar branca acima da sidebar, navegação horizontal Aplicativos, Empresas, Usuários, Integrações, Informativos, Master, busca por atalho Ctrl+K, acesso a peças, ajuda, notificações e conta. Não assumir que menus superiores implicam novas APIs.
- Sidebar expandida ~270 px, recolhida ~72 px, check-in/out, abas ativas azul-claro, ícones lineares, grupos com acordeão, rodapé Recolher.
- Submenus observados: Orçamentos & Serviços (Checklist, Diagnósticos, Orçamentos, Ordens de serviço); Clientes (Gerenciar clientes, Histórico de veículos); Gestão de recursos (Cadastros, Seguradoras, Fornecedores, Transportadoras); Peças & Estoque (Produtos, Consulta de peças, Kits, Cotações, Compras, Entregas & Devoluções, Inventários, Correções de estoque, Transferências); Financeiro (Contas, Pagar & Receber, Movimentação, Boletos, Conciliação); Fiscal (Nota fiscal, Manifestação, Sintegra, NCM, Inutilização).
- Home: saudação, aviso, dois widgets Financeiro e Ordens de serviço; botões de atualizar, informações, personalizar cartões e ícones de ajuda. Um exemplo do UltraCar mostra ticket mensal em R$ 3.037,96 — esse valor **não deve** ser copiado para a API nem usado como dado real.

**Código implementado na branch master:**
- `FrontEnd/public/data/menu.json`: árvore reorganizada para refletir as capturas, com rotas existentes preservadas; funções não implementadas permanecem visualmente indisponíveis.
- `FrontEnd/src/app/theme/header/*`, `sidebar/*`, `sidemenu/*`, `admin-layout/*`: topbar global, menu expandido/recolhido, link real para suporte, estilos e ícones ajustados. Botões de check-in/out **indisponíveis** até existir contrato de marcação de presença e servidor, para não criar registros falsos.
- `FrontEnd/src/app/routes/dashboard/dashboard.{html,scss,ts}`: layout com dois widgets brancos, indicadores reais do endpoint `GET /api/dashboard`, atualização real, saudação por perfil autenticado e seleção visual de componentes (estado local em memória); as quatro visões anteriores continuam acessíveis.
- `FrontEnd/src/app/routes/dashboard/dashboard.spec.ts`: testes adicionados de renderização com dados zero, atualização, personalização e preservação das quatro visões. **Ainda não executados neste ambiente.**

**Mapeamento de dados:** contas a receber pendentes, contas a pagar pendentes, títulos a receber vencidos, receitas/despesas do período, ticket médio geral, veículos atrasados e clientes ativos já existem no `DashboardDTO`. O endpoint **não fornece** ticket médio mensal separado, recebíveis do dia, vencimentos a pagar específicos ou a mesma semântica de "entregas atrasadas" da referência. São lacunas, não zeros verificados.

**Diferenças e pendências obrigatórias de homologação:**
- [ ] Executar `yarn test --watch=false --browsers=ChromeHeadless` com testes focados e `yarn build`.
- [ ] Validar visualmente screenshots em 1920 px, 1440 px, 768 px e 320 px, além de zoom 200%.
- [ ] Comparar teclado, leitor de tela, rolagem e comportamento do menu recolhido/expandido.
- [ ] Definir contrato e testes de check-in/out para colaboradores; ações permanecem desabilitadas.
- [ ] Criar backend para indicadores ainda inexistentes (somente após definir semântica, filtros e multiempresa).
- [ ] Projetar consulta de peças externa, importações, boletos e conciliação sem links falsos.
- [ ] Personalização dos widgets hoje é apenas local à página, sem persistência entre sessões.
- [ ] A busca visualmente chamada "Busca Inteligente" hoje pesquisa **somente páginas e módulos permitidos**, não tem busca semântica por registros.
- [ ] Os screenshots originais ainda não estão armazenados como assets no repositório; são referências recebidas no chat.

**Status:** código de layout/integração enviado para o GitHub; equivalência visual, testes e operações indisponíveis ainda não homologados.
