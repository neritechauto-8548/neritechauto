# NERITECH AUTO — Componentes compartilhados da Etapa 00

Fonte visual: Notion UI-MASTER-001. Preservar SVGs vetoriais consistentes (traço Tabler) e identidade NERITECH AUTO.

- `NeriTechIcon`: ícones SVG inline, tamanho controlado, alternativa textual apenas quando semântica. Importar de `@shared/components`.
- `DataViewState`: um componente acessível para `loading`, `empty`, `filtered-empty`, `error`, `offline` e `forbidden`. O botão de nova tentativa é exibido **somente** quando o componente pai liga `retryAvailable` e trata `(retry)`; nunca simular dados ou sucesso.
- `permissionGuard`: guard reutilizável de UX configurável por `data.requiredPermissions` e `data.permissionMode = 'any' | 'all'`. Em preview local usa bypass controlado; em produção nega rota para `/403` sem permissão. **O backend deve validar cada ação e não confiar neste guard como segurança definitiva.** A aplicação da guarda em rotas requer inspeção prévia dos contratos de permissão do módulo.

Esses componentes são infraestrutura compartilhada: sua inclusão no repositório não comprova integração em todas as telas nem homologação visual. Validar uso e semântica por tela.
