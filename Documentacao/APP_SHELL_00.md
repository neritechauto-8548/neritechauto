# NERITECH AUTO — Application Shell (Etapa 00)

## Escopo do shell ativo

- Navegação principal: `theme/admin-layout`, `theme/sidebar/sidebar.ts`, `theme/sidemenu` e `theme/header`.
- Branding, menu do usuário e breadcrumb: `theme/widgets`.
- Busca mobile e desktop com atalhos e responsividade; autenticação e assinatura apenas nos ambientes de produção (prévia UX claramente marcada).
- Serviços, formulários, filtros, tabelas e telas de domínio preservam a arquitetura congelada no Notion.

## Rotas de produto

O arquivo `app.routes.ts` registra apenas o Application Shell, autenticação, perfil/conta, estados de erro e os módulos existentes da NERITECH AUTO. Rotas de vitrine de componentes herdadas do template (`design`, `material`, `media`, `forms`, `tables`, `permissions`, `utilities`) **não integram a navegação do produto**. O código de demonstração pode permanecer no repositório enquanto for útil para desenvolvimento, mas não deve gerar telas extras acessíveis na aplicação.

Não recriar a sidebar antiga `SidebarMenuComponent`: usar exclusivamente `theme/sidebar/sidebar.ts` e respeitar o menu oficial do Notion. Não criar atalho de rota inexistente para simular módulo ainda não implementado.

## Gate de homologação

- `yarn build` deve passar no pipeline Angular;
- `mvn -B clean test -DskipTests=false` deve passar com banco PostgreSQL de testes;
- CI verde **não substitui QA visual** de 320, 375, 768, 1024 e 1440 px nem navegação por teclado;
- confirmar perfil, permissões, navegação e comportamento sem backend apenas em UX Preview;
- manter pendências explícitas até a validação real no navegador.
