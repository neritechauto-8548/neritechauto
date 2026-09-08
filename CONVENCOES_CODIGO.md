# NeriTech Auto — Convenções Oficiais de Código

## Idioma oficial do código

A partir de 02/09/2026, todo código de domínio novo ou reconstruído da NeriTech Auto deve utilizar **português brasileiro** para nomes próprios do sistema.

Esta regra é obrigatória para:

- tabelas e colunas do PostgreSQL;
- arquivos de migration Flyway;
- entidades e classes de domínio;
- DTOs, requests e responses próprios da NeriTech;
- serviços e repositórios próprios;
- atributos, variáveis locais, parâmetros e constantes de domínio;
- métodos e funções de negócio;
- enums e valores persistidos de domínio quando não forem contratos externos;
- comentários, JavaDoc, TSDoc e documentação inline;
- nomes de cenários e dados de testes;
- novos contratos HTTP internos quando ainda não houver contrato público consolidado.

## Exemplos

Evitar:

```text
os_comments
author_user_id
author_name_snapshot
content
visibility
comments
invoice
payments
load()
submit()
canView
```

Preferir:

```text
comentarios_ordem_servico
usuario_autor_id
nome_autor_registrado
conteudo
visibilidade
comentarios
fatura
pagamentos
carregar()
registrarComentario()
podeVisualizar
```

## Banco de dados

- tabelas: `snake_case` em português;
- colunas: `snake_case` em português;
- chaves estrangeiras: `<entidade>_id`;
- datas: `data_cadastro`, `data_atualizacao`, `data_envio`, `data_decisao` etc.;
- valores: `valor_total`, `valor_pago`, `valor_pendente`;
- booleanos devem expressar claramente o estado: `ativo`, `aprovado_cliente`, `enviado_email`;
- nenhuma tabela ou coluna existente em produção pode ser renomeada sem migration Flyway explícita.

Exemplo de migration:

```text
V272__criar_comentarios_ordem_servico.sql
```

## Backend Java

Preferir:

```java
public record ComentarioOrdemServicoResposta(
        Long id,
        Long ordemServicoId,
        Long usuarioAutorId,
        String nomeAutor,
        String conteudo,
        String visibilidade,
        LocalDateTime dataCadastro) {
}
```

Métodos de domínio devem usar verbos em português, por exemplo:

```text
listar
buscarPorId
criar
atualizar
excluir
validar
exigirOrdemDaEmpresa
obterUsuarioAutenticado
normalizarConteudo
```

## Frontend Angular / TypeScript

Preferir:

```typescript
interface ComentarioOrdemServico {
  id: number;
  ordemServicoId: number;
  usuarioAutorId: number;
  nomeAutor: string;
  conteudo: string;
  dataCadastro: string;
}

carregar(): void { ... }
registrarComentario(): void { ... }
```

Estados internos próprios também devem usar português quando não forem definidos por biblioteca externa:

```typescript
type EstadoCarregamento = 'ocioso' | 'carregando' | 'pronto' | 'proibido' | 'erro';
```

## Exceções técnicas obrigatórias

Não traduzir identificadores impostos pela plataforma, linguagem, protocolo ou fornecedor quando isso quebrar compatibilidade.

Exemplos permitidos:

- palavras-chave de Java/TypeScript como `class`, `interface`, `public`, `private`, `return`, `const`, `let`;
- APIs de framework como `HttpClient`, `Observable`, `ResponseEntity`, `Pageable`, `OnInit`, `OnChanges`;
- annotations/decorators oficiais de Spring e Angular;
- HTTP, REST, JWT, OAuth2, OpenAPI, RFC, UUID, URL, ETag e `If-Match`;
- nomes oficiais de headers e claims;
- payloads de Stripe, Stone, provedores fiscais e outras integrações quando o contrato externo exige determinado campo;
- hooks obrigatórios de framework, como `ngOnInit` e `ngOnChanges`;
- códigos de permissão já persistidos, como `OS_COMENTARIOS` e `FIN_LISTAR_CONTAS`, até existir migration formal aprovada.

## Migração do legado

Não fazer tradução global e cega do repositório.

A migração é progressiva por slice:

1. todo código novo nasce em português;
2. ao reconstruir um módulo, converter identificadores próprios daquele slice quando seguro;
3. contratos públicos existentes exigem compatibilidade ou versionamento;
4. tabelas/colunas persistidas exigem Flyway;
5. não quebrar consumidores apenas para traduzir nomes;
6. nomes em inglês mantidos por compatibilidade devem ser tratados como legado e não copiados para código novo.

## Revisão de PR

Um PR deve ser bloqueado quando introduzir novo identificador de domínio em inglês sem justificativa técnica.

A justificativa válida deve ser uma destas:

- requisito do framework;
- requisito de protocolo/padrão;
- contrato externo;
- compatibilidade temporária documentada com legado.

Fora dessas situações, usar português brasileiro.
