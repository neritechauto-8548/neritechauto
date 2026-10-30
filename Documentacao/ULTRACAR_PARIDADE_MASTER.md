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
