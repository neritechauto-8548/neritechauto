# 05.01 — Identidade, Autenticação e Sessões

> Espelho consultivo da [documentação oficial no Notion](https://app.notion.com/p/6b001fd65a9c8368bb1c01e95d984771), consultada em 15/10/2026. Não sincroniza automaticamente; prevalece a fonte canônica.
> Para Auth, leia primeiramente o bloco A; referências históricas são secundárias.

## A — CONTRATO EXECUTÁVEL CODEX — AUTH-001/002/003 \| DECISÃO FINAL NERITECH AUTO
**Prioridade:** requisito obrigatório transversal, antes das 64 telas de negócio. Contrato atual prevalece sobre referências visuais antigas onde houver divergência, preservando o design split-screen aprovado.
### AUTH-001 Login — /auth/login
- UX desktop: composição em **duas seções**, ilustração/fotografia de veículo grande e branding original no painel esquerdo; formulário maior e centrado à direita, com hierarquia clean e cores de UI-MASTER. Não copiar login Stripe. Tablet reduz painel ilustrativo, celular prioriza formulário; imagem nunca impede contraste ou teclado.
- Campos: E-mail \[obrigatório, formato email, trim lowercase para pesquisa; não alterar parte local arbitrariamente\], Senha \[obrigatório, tipo password, alternar visibilidade\], Manter meu e-mail preenchido \[boolean off por padrão\]. Links Recuperar acesso e política de privacidade.
- Ação Entrar \[anti-duplo clique, loading, acessível\]; depois de JWT válido confirmar sessão/perfil e empresa autenticada e redirecionar para URL interna segura ou Início. Falha credencial: mensagem genérica sem diferenciar e-mail inválido de senha incorreta; 401 invalida sessão, 403 acesso negado, 429 limites, offline e timeout com retry seguro.
- Política escolhida: senha mínimo de 12 caracteres para nova senha, aceitar frases e caracteres Unicode conforme suporte do backend; evitar regras artificiais de combinação; comparar contra senhas comprometidas se serviço seguro estiver disponível; proteção de brute force/rate limit no servidor. MFA opt-in para empresas e obrigatório para ações sensíveis quando integração suportar, sem bypass de API.
### AUTH-002 Recuperar — /auth/recover
- Campos E-mail \[obrigatório, formato\], botão Enviar instruções. Resposta sempre genérica "Se o e-mail estiver cadastrado, enviaremos instruções" para evitar enumeração.
- Token de recuperação opaco aleatório de uso único; **decisão de TTL 30 min** com armazenamento somente hash no backend, throttling por e-mail/IP e invalidação em novo uso/troca. Nunca registrar token, senha ou URL contendo token nos logs.
- Tela de sucesso e voltar ao login; ausência de provedor de e-mail significa informar indisponibilidade operacional em ambiente real, não afirmar envio.
### AUTH-003 Redefinição — /auth/reset-password
- Campos Nova senha, Confirme nova senha; validação igual, força e proteção de senha, submit idempotente; token expirado/inválido usado → instrução pedir novo link, sem exibir token.
- Trocar senha revoga sessões anteriores/refresh tokens conforme política de backend; tentar refresh antigo retorna 401.
### Sessão e tenant
- **Uma empresa ativa por sessão NERITECH, sem Filiais**; login não disponibiliza seletor de Filiais. Contexto JWT e sessão no servidor; header informado pelo browser não pode substituir identidade autenticada. Nunca usar fallback `empresaId=1`.
- Access token de curta duração; escolher janela padrão **15 minutos**, refresh rotativo com expiração absoluta de sessão **7 dias** salvo política corporativa, revogação por logout/alteração de senha/bloqueio, CSRF/XSS conforme método de armazenamento existente. Se backend atual utiliza outra janela, fazer migração segura de política e testes, não alterar silenciosamente tokens ativos de produção.
- Lock e observabilidade sem credenciais; acesso posterior a dados sempre por capability e tenant, política documentada em 03.01.04 D02.
### Contratos reais e QA
- Reutilizar AuthService/LoginService e rotas `/auth/login`, `/auth/refresh`, `/auth/recover-password`, `/auth/reset-password`, `/auth/logout` já observadas; checar BASE_URL real e testes existentes.
- QA: login correto/incorreto, conta inativa, refresh duplicado/revogação, MFA quando habilitado, rate limit, token reset expirado, link consumido, usuário trocando empresa A/B na mesma estação, 401/403/429/500, offline, tabs, zoom 200%, leitor de tela.
**Gate:** Codex pode implementar UI/backend e testes; autenticação não é homologada sem evidência de revogação e isolamento reais.
<callout icon="🔐" color="blue_bg">
	**PLAT-IAM-001 · Identidade compartilhada** — Status **D4 DOCUMENTAL — identidade, autenticação e sessões especificadas** · 16/08/2026. D5 exige testes reais de auth/revogação, secret scan, políticas de sessão e observabilidade.
</callout>
# B — Escopo e fluxos de autenticação
Login, recuperação de senha, sessão, MFA/step-up quando exigido, rotação/revogação e propagação de contexto autenticado.
# Contexto
Sessão resolve `user_id`, `tenant_id`, unidades autorizadas, roles/capabilities, nível de autenticação, device/session id e correlação. Header informado pelo cliente nunca substitui autorização server-side.
# Sessões
Access token curto + mecanismo seguro de renovação conforme arquitetura adotada; revogação por logout, troca de senha, risco ou administração. Sessões podem ser listadas e encerradas pelo usuário/admin autorizado.
# Recuperação
Token de uso único, expiração curta, armazenado como hash, resposta anti-enumeração e invalidação após uso/troca.
# Regras
1. **RN-PLAT-IAM-001:** autenticação não implica autorização ao tenant/unidade.
2. **RN-PLAT-IAM-002:** refresh/recovery tokens nunca aparecem em logs.
3. **RN-PLAT-IAM-003:** mudança de senha invalida sessões conforme política.
4. **RN-PLAT-IAM-004:** ação P1 pode exigir step-up mesmo com sessão válida.
5. **RN-PLAT-IAM-005:** resposta de login/recuperação não revela se conta inexistente está cadastrada além do necessário.
# Testes
Brute-force/rate limit, enumeração, token expirado/reutilizado, sessão revogada, tenant cruzado, unit scope e step-up.
# C — Registro histórico D4 — Identidade, Autenticação e Sessões (não comprova homologação atual)
**Objetivo:** fornecer identidade confiável e contexto de segurança para todos os módulos sem acoplar autorização a detalhes de UI.
## Contrato de sessão
A sessão/autenticação resolve `userId`, `tenantId`, unidades autorizadas, capabilities efetivas, nível de autenticação, `sessionId/deviceId`, expiração, correlation context e flags de suporte/impersonation. `tenantId`, role ou capability enviados pelo cliente nunca são aceitos como autoridade.
## Fluxos
Login → resolução do tenant → políticas de autenticação → emissão/estabelecimento de sessão → refresh/renewal controlado → logout/revogação. Recuperação de senha e convite usam tokens opacos, single-use, expirados e auditados. Step-up/MFA é exigido apenas para ações classificadas como alto risco conforme policy.
## Regras D4
- **RN-PLAT-ID-D4-001** — credenciais e tokens nunca aparecem em logs, analytics ou audit payload.
- **RN-PLAT-ID-D4-002** — refresh token/session rotation detecta reuse quando modelo adotado suportar.
- **RN-PLAT-ID-D4-003** — desativação de usuário e revogação de grants invalidam sessões/caches conforme janela de risco definida.
- **RN-PLAT-ID-D4-004** — login não revela se e-mail/usuário existe além do necessário.
- **RN-PLAT-ID-D4-005** — recuperação/convite não concede tenant/unidade além do convite registrado server-side.
- **RN-PLAT-ID-D4-006** — suporte/impersonation exige capability, motivo, duração e actor original preservado.
- **RN-PLAT-ID-D4-007** — service accounts não reutilizam identidade humana e possuem owner/escopo/rotação.
- **RN-PLAT-ID-D4-008** — política de sessão diferencia dispositivo confiável apenas como sinal, nunca como bypass de autorização.
## APIs e dados
Endpoints de auth/session/refresh/revoke/recovery/invite devem usar rate-limit e RFC 9457. Persistência proposta: `identity_user`, `identity_session`, `identity_recovery_token`, `identity_invitation`, `identity_auth_event`, com hashes de tokens e timestamps; segredo bruto não persistido recuperável.
## CA/CT
`CA/CT-PLAT-ID-D4-001..012`: enumeração; brute-force/rate-limit; token expiry; single-use; refresh reuse; revoke; disabled user; forged tenant; invitation scope; impersonation audit; secret/log redaction; concurrent sessions.
**Gate D5:** testes auth positivos/negativos, revogação real, secret scan, session policy e observabilidade executados.
# Padrão visual — Autenticação NERITECH AUTO
## Princípio
As telas de entrada devem seguir o padrão **Stripe-first** do NERITECH AUTO: interface limpa, foco na tarefa, poucos elementos, hierarquia clara, feedback imediato e aparência premium de SaaS. A referência é estrutural e não uma cópia visual.
## TELA-AUTO-AUTH-001 — Login
**Rota:** `/auth/login`  
**Objetivo:** autenticar o usuário sem distrações e encaminhá-lo à aplicação.
### Estrutura
1. Marca NERITECH AUTO.
2. Título: **Entrar no NERITECH AUTO**.
3. Texto de apoio curto.
4. Campo **E-mail**.
5. Campo **Senha**.
6. Ação **Esqueci minha senha**.
7. Opção **Manter meu e-mail preenchido**.
8. CTA primário **Entrar**.
9. Estado de carregamento.
10. Mensagens de validação e autenticação.
### Regras
- E-mail obrigatório e validado.
- Senha obrigatória.
- O navegador pode memorizar apenas o e-mail para conveniência.
- **Senha nunca deve ser persistida em localStorage, sessionStorage, cookie acessível por JavaScript ou qualquer armazenamento recuperável pelo frontend.**
- Erros de autenticação não devem revelar informações desnecessárias sobre a existência da conta.
- O contexto de tenant/empresa é resolvido e autorizado no backend; valores enviados pelo cliente nunca são autoridade.
- O botão fica bloqueado durante o processamento.
- A navegação ocorre somente após autenticação válida.
### Estados
`idle` → `validating` → `processing` → `success` / `error`.
## TELA-AUTO-AUTH-002 — Recuperar senha
**Rota:** `/auth/recover`
### Estrutura
1. Marca NERITECH AUTO.
2. Título **Recuperar acesso**.
3. Texto explicativo.
4. Campo **E-mail**.
5. CTA **Enviar link de recuperação**.
6. Link **Voltar para o login**.
### Segurança
- A resposta deve ser anti-enumeração.
- A interface não deve confirmar se o e-mail existe.
- O token deve ser opaco, de uso único, possuir expiração curta e ser armazenado no backend apenas em formato não recuperável quando persistido.
- Tokens nunca aparecem em logs, analytics ou mensagens de erro.
- Deve existir rate limit para solicitações.
- Reenvios devem respeitar política de limitação.
### Estado de sucesso
Exibir confirmação genérica: se o endereço estiver cadastrado, as instruções serão enviadas.
## TELA-AUTO-AUTH-003 — Redefinir senha
**Rota:** `/auth/reset-password?token=...`
### Estrutura
1. Marca NERITECH AUTO.
2. Título **Redefinir senha**.
3. Campo **Nova senha**.
4. Campo **Confirmar nova senha**.
5. Indicador/validação de requisitos da senha.
6. CTA **Atualizar senha**.
7. Link para login.
### Regras
- Token obrigatório, válido, não expirado e não utilizado.
- Token inválido/expirado não permite alteração.
- Nova senha e confirmação devem coincidir.
- A senha deve obedecer à política central de segurança.
- Após alteração, o token é invalidado.
- Sessões existentes devem ser revogadas conforme política de segurança.
- O usuário retorna ao login após sucesso.
## Componentes visuais
- Layout **split-screen desktop-first** em duas áreas.
- Painel visual esquerdo ocupando aproximadamente 42–45% do viewport desktop.
- Painel de autenticação direito ocupando o espaço restante, com conteúdo limitado a aproximadamente 440 px.
- Painel esquerdo com fundo escuro premium, ilustração automotiva tecnológica e elementos gráficos sutis.
- Não utilizar fotografia genérica de banco de imagens nem composição publicitária; o visual deve reforçar o produto SaaS automotivo.
- Painel direito branco, sem card flutuante desnecessário, com bastante respiro e hierarquia tipográfica clara.
- Marca NERITECH AUTO presente no painel visual desktop e reapresentada no mobile.
- CTA único e claramente primário.
- Campos com foco visível.
- Estados de erro, sucesso, loading e disabled explícitos.
- Em tablet/mobile, o painel visual é ocultado e o formulário ocupa a tela integralmente.
- WCAG 2.2 AA.
## Contrato técnico atual validado no repositório
Frontend Angular já possui:
- `/auth/login`
- `/auth/recover`
- `/auth/reset-password`
Serviço atual utiliza:
- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout`
- `POST /auth/recover-password`
- `POST /auth/reset-password`
- `GET /usuarios/me`
Esses contratos representam o estado encontrado no repositório e devem continuar sendo tratados como **contratos técnicos sujeitos à validação do backend**, não como novos endpoints inventados pela documentação.
## Correção de segurança aplicada
O fluxo de login não deve mais salvar a senha no navegador. A opção de lembrança foi redefinida semanticamente para memorizar somente o e-mail.
## Checklist de aceite
- [x] Login redesenhado no padrão visual definido.
- [x] Recuperação de senha redesenhada.
- [x] Fluxo de reset existente identificado.
- [x] Senha removida do armazenamento local do frontend.
- [ ] Validar backend de autenticação e recuperação no repositório.
- [ ] Validar rate limit real.
- [ ] Validar expiração e single-use do token.
- [ ] Validar revogação de sessões após troca de senha.
- [ ] Validar testes automatizados.
- [ ] Validar acessibilidade em navegador.
- [ ] Validar comportamento mobile.
## Aplicação do UI-MASTER-001 — Login
A tela de Login deve seguir integralmente o contrato visual vigente do UI-MASTER-001.
### Ícones
- Família canônica: **Tabler Icons**.
- Estilo: outline.
- Grade: 24×24.
- Stroke: 2.
- Tamanhos usuais: 16/20/24 px.
- Não utilizar PrimeIcons, Material Icons ou outra família na tela.
- O ícone de visibilidade da senha usa o mesmo significado semântico em todo o produto.
- Ícones não devem ser usados como decoração; cada ícone deve comunicar uma ação ou estado.
- O estado de loading utiliza indicador funcional, sem substituir o padrão de ícones por outra biblioteca.
### Tokens aplicados
- Canvas: `#F8FAFC`.
- Panel: `#FFFFFF`.
- Primary 500: `#2563EB`.
- Primary 600: `#1D4ED8`.
- Border: `#E2E8F0`.
- Text primary: `#0F172A`.
- Text secondary: `#475569`.
- Text muted: `#64748B`.
- Raio padrão de controles: 8 px.
- Escala de espaçamento: somente valores do sistema canônico.
### Regra de implementação
O login não deve criar uma identidade visual paralela. Qualquer componente, cor, ícone, espaçamento ou estado novo deve reutilizar o Design System ou ser formalmente incorporado aos tokens antes de ser propagado para outras telas.
## Direção vigente — Login Split-screen
A implementação real do Login foi atualizada no repositório **NERITECH AUTO**, branch `feature/develop`, para a composição split-screen definida como padrão visual da autenticação.
### Direção visual
- Desktop utiliza **duas áreas claramente distintas**: experiência visual/produto à esquerda e autenticação à direita.
- O painel esquerdo ocupa aproximadamente 42–45% do viewport e utiliza fundo escuro premium, grid técnico discreto, iluminação sutil e uma ilustração automotiva vetorial integrada ao produto.
- A mensagem do painel esquerdo reforça o posicionamento: **“Do orçamento à entrega, sua oficina em um só lugar.”**
- O painel direito é branco, limpo e sem card flutuante desnecessário, mantendo o formulário em uma largura confortável de aproximadamente 440 px.
- A estética segue o princípio **Stripe-first**: simplicidade, precisão, respiro, hierarquia e consistência de SaaS premium; não é uma cópia visual do Stripe.
- Não utilizar imagens genéricas de stock nem banners publicitários na autenticação.
- Em telas menores, o painel visual é ocultado e o fluxo retorna para uma composição de autenticação única e responsiva.
### Contratos preservados
- `/auth/login`
- `/auth/recover`
- `/auth/reset-password`
- A lógica de autenticação, validação, loading, recuperação e persistência segura do e-mail permanece inalterada.
- A senha continua sem persistência no frontend.
### Arquivos atualizados no repositório
- `FrontEnd/src/app/routes/sessions/login/login.html`
- `FrontEnd/src/app/routes/sessions/login/login.scss`
### Status
**Implementado no GitHub.** A validação visual final deve ser feita no navegador em desktop (prioridade), tablet e mobile, confirmando também contraste, foco, teclado, leitura por tecnologia assistiva e comportamento em resoluções de 1280 px, 1440 px e 1920 px.
## Atualização — Login e Recuperação de acesso padronizados
A experiência de autenticação agora utiliza o mesmo padrão visual **split-screen desktop-first** em Login e Recuperar acesso.
### Padrão visual
- Painel visual esquerdo maior, ocupando aproximadamente 44–48% do desktop.
- Ilustração automotiva ampliada para ser parte relevante da composição, sem parecer um pequeno detalhe decorativo.
- Painel esquerdo com identidade NERITECH AUTO, grid técnico, iluminação sutil e mensagem contextual da tarefa.
- Painel direito branco, limpo, sem card flutuante desnecessário.
- Formulário limitado a aproximadamente 440 px para preservar legibilidade e respiro.
- Mesmos tokens de cor, tipografia, controles, CTA, estados e espaçamentos entre Login e Recuperação.
- Em tablet/mobile, o painel visual é ocultado e a autenticação ocupa toda a largura disponível.
- A direção é **Stripe-first** como referência de qualidade e estrutura, sem copiar identidade visual de terceiros.
### TELA-AUTO-AUTH-002 — Recuperar senha
A tela de recuperação foi redesenhada para seguir exatamente a mesma linguagem do Login.
- Painel visual contextualizado com **“Recupere seu acesso sem interromper a operação.”**
- Campo de e-mail com altura, foco, erro e tipografia iguais ao Login.
- CTA **Enviar link de recuperação** com o mesmo padrão do CTA de Login.
- Link **Voltar para o login** com o mesmo tratamento visual.
- Estado de sucesso reorganizado para uma leitura mais clara, mantendo a regra anti-enumeração.
- Fluxo funcional, serviço de recuperação, loading, validações e mensagens de segurança foram preservados.
### Arquivos atualizados
- `FrontEnd/src/app/routes/sessions/login/login.html`
- `FrontEnd/src/app/routes/sessions/login/login.scss`
- `FrontEnd/src/app/routes/sessions/recover/recover.html`
- `FrontEnd/src/app/routes/sessions/recover/recover.scss`
### Status
**Implementado no GitHub — branch ****`feature/develop`****.**  
A validação visual final deve considerar desktop 1280/1440/1920 px, tablet e mobile, além de foco por teclado, contraste, estados de erro/loading/sucesso e acessibilidade.
## Refinamento premium — Autenticação Stripe-first
A composição de Login e Recuperação recebeu um refinamento visual final para aproximar a experiência do padrão premium definido para o NERITECH AUTO.
### Ajustes aplicados
- Painel visual desktop ampliado para aproximadamente **48% do viewport**, dando mais protagonismo à identidade do produto.
- Ilustração automotiva ampliada de forma significativa e integrada à composição, deixando de funcionar como elemento decorativo pequeno.
- Redução da intensidade dos brilhos e do grid para uma estética mais sofisticada e menos genérica.
- Painel de autenticação ampliado, com conteúdo de aproximadamente **500 px** de largura máxima.
- Título, campos e CTA receberam escala ligeiramente maior para melhorar presença visual em desktop.
- Inputs e CTA padronizados com **50 px** de altura e raio de 8 px.
- Login e Recuperação utilizam a mesma linguagem visual, tokens e comportamento responsivo.
- A referência permanece **Stripe-first como padrão de qualidade**, sem copiar identidade visual de terceiros.
### Arquivos atualizados
- `FrontEnd/src/app/routes/sessions/login/login.scss`
- `FrontEnd/src/app/routes/sessions/recover/recover.scss`
### Commits
- Login: `fdb128cf0ea24a990e2e0640d462ba176cceb65c`
- Recuperação: `ca81cdd28c3cfed7d5415fa01be8889fb387f467`
### Critério visual vigente
A autenticação deve transmitir **SaaS premium, tecnológico, limpo e confiável**, com grande respiro, hierarquia forte, ilustração automotiva proprietária e formulário claramente dominante no painel direito.
