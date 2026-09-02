# NeriTech Auto — Codex Instructions

Este arquivo é o contexto operacional principal para agentes de código.
Objetivo: implementar com qualidade usando o mínimo de contexto e tokens possível.

## 1. Regra principal

Implemente somente a tarefa solicitada. Não transforme uma tarefa pequena em refatoração ampla.

Ordem de prioridade:
1. pedido atual do usuário;
2. este `AGENTS.md`;
3. documentação específica da funcionalidade em `Documentacao/`;
4. `regras_de_negocio.md`;
5. comportamento e padrões já existentes no código.

Se houver conflito real entre documentação e código, preserve o comportamento existente quando a tarefa não exigir mudança e registre a divergência no resumo final.

## 2. Protocolo de economia de tokens

- Não leia o repositório inteiro por padrão.
- Não leia toda a pasta `Documentacao/` por padrão.
- Comece pelo arquivo desta instrução e pelos arquivos diretamente relacionados à tarefa.
- Antes de implementar, inspecione primeiro de 3 a 5 arquivos relevantes. Amplie somente se necessário.
- Pesquise por componente, rota, endpoint, entidade ou regra pelo nome antes de abrir arquivos grandes.
- Não releia arquivos que já foram compreendidos na mesma tarefa.
- Não explique raciocínio longo; execute.
- Não gere documentação extensa ao final.
- Não rode todos os testes do projeto se testes focados forem suficientes.
- Não atualize dependências, versões ou arquitetura sem solicitação explícita.
- Não crie abstrações para uso futuro sem necessidade atual.

## 3. Mapa rápido do repositório

- `FrontEnd/`: aplicação web principal.
- `backend/`: backend principal.
- `fiscal-service/`: domínio/serviço fiscal.
- `PortalCliente/`: portal externo do cliente.
- `landpage/`: landing page.
- `Database/`: artefatos de banco de dados.
- `Documentacao/`: documentação funcional/técnica.
- `regras_de_negocio.md`: regras de negócio consolidadas.

Leia apenas a área necessária para a tarefa atual.

## 4. Stack atual verificada

### Frontend
- Angular 20.3.x.
- PrimeNG 20.3.x para novos componentes de negócio.
- Tailwind disponível para layout e utilitários.
- Angular Material/Matero existe no legado; não ampliar seu uso em telas novas sem necessidade.
- RxJS e formulários Angular conforme padrões existentes.

### Backend
- Java 21.
- Spring Boot 3.2.3.
- Spring Web, Validation, Security e Data JPA.
- PostgreSQL.
- Flyway.
- JWT.
- MapStruct.
- OpenAPI/Swagger.
- JasperReports onde já aplicável.

## 5. Regras de implementação

- Reutilize componentes, serviços, DTOs, helpers, estilos e padrões existentes antes de criar novos.
- Preserve compatibilidade com APIs existentes salvo quando a tarefa exigir alteração de contrato.
- Validação deve existir na camada apropriada; não dependa apenas do frontend.
- Não exponha dados de outro tenant/empresa.
- Respeite permissões e autenticação existentes.
- Nunca inclua segredos, tokens ou credenciais no código.
- Para mudanças de banco, use migração Flyway; não altere histórico de migração já aplicado.
- Evite lógica de negócio importante dentro de componentes Angular ou controllers Spring.
- Prefira alterações pequenas, coesas e testáveis.

## 6. Frontend — padrão para novas telas

- Use PrimeNG + Tailwind como padrão principal.
- Siga `DESIGN.md`.
- Reaproveite shell, sidebar, topbar, breadcrumbs, cards, tabelas, formulários e dialogs existentes.
- Não crie uma identidade visual diferente por módulo.
- Use estados de loading, vazio, erro, sucesso e disabled quando aplicáveis.
- Formulários devem ter labels claras, validações visíveis e mensagens objetivas.
- Layout deve funcionar em desktop e permanecer utilizável em tablet/mobile.
- Ícones desejados: Tabler Icons. Se Tabler ainda não estiver instalado, não adicione dependência durante uma tarefa sem relação com isso; mantenha o padrão existente e registre a pendência.

## 7. Backend — padrão

Fluxo preferencial quando compatível com o código existente:
`Controller -> Service -> Repository`.

- Controllers finos.
- Regras de negócio em services/domínio.
- DTOs nas fronteiras da API.
- Bean Validation para entrada.
- MapStruct quando o módulo já o utiliza.
- Erros devem seguir o tratamento central existente.
- Toda consulta multiempresa deve respeitar o escopo do tenant/empresa.

## 8. Testes e validação

Para cada tarefa:
1. rode o teste mais específico possível;
2. rode lint/build do módulo alterado apenas quando necessário;
3. amplie a validação se a mudança tiver impacto transversal.

Não corrija erros antigos e não relacionados sem que bloqueiem a tarefa. Se bloquearem, faça a menor correção segura e informe no resumo.

## 9. Escopo proibido sem pedido explícito

Não fazer automaticamente:
- refatoração global;
- migração de framework;
- upgrade de versão;
- troca de biblioteca de UI;
- alteração de autenticação;
- alteração de estratégia multi-tenant;
- reestruturação de banco;
- criação de novo microserviço;
- mudança de contratos públicos de API;
- reescrita visual completa de telas fora do escopo.

## 10. Resposta final do Codex

Seja curto. Informe somente:
1. `Implementado`: 1 a 4 bullets;
2. `Arquivos alterados`: caminhos;
3. `Validação`: testes/build executados;
4. `Pendências`: somente se existirem.

Não repita o código completo nem a documentação na resposta final.
