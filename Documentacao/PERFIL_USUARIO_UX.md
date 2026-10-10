# NERITECH AUTO — Menu de perfil do usuário

O componente FrontEnd/src/app/theme/widgets/user-button.ts utiliza estilo próprio em user-button.scss, com ícones SVG de tamanho e traços definidos. O layout corresponde à referência aprovada: avatar na barra superior, cartão branco compacto, retrato circular, dados reais quando disponíveis, opções Meu perfil, Configurações, Ajuda e Sair.

Origem da foto: a propriedade avatar/avatarUrl de /usuarios/me tem prioridade. Na homologação sem backend, o ativo nt-profile-demo.webp é ilustrativo e sinalizado explicitamente no cartão. Em produção, quando não há foto configurada, é usado nt-profile-default.svg, sem iniciais ou identidade inventada. Caso a foto falhe, usa fallback neutro.

Meu perfil abre /profile/overview; Configurações abre /profile/settings; Ajuda utiliza a URL de suporte; Sair utiliza o serviço de autenticação. Não cria novas rotas ou endpoints.

Critérios pendentes: verificar tamanho dos ícones (18 px), popup menor que viewport, teclado/Escape, foto real e fallback, abertura e fechamento, navegação de rotas, build e homologação visual real.
