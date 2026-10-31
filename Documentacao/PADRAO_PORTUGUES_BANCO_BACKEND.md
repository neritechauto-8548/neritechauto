# Padrão obrigatório — backend e banco de dados em português

Projeto: NERITECH AUTO. Branch de trabalho: `master`.

## Convenção oficial

- Nome de tabelas e colunas físicas em português brasileiro, `snake_case`, sem acentos: `funcionarios`, `rh_funcionario_contatos`, `rh_funcionario_contato_meios`, `rh_epis`, `rh_epi_entregas`; `nome_completo`, `cpf`, `registro_profissional`, `comissao_pecas`, `comissao_servicos`, `data_admissao`, `empresa_id`.
- Classes, campos, DTOs, métodos de serviço e regras de domínio escritos em português (`FuncionarioService`, `FuncionarioRequest`, `ContatoFuncionario`, `dataEntrega`, `dataNascimento`). Termos de frameworks e HTTP mantêm sua nomenclatura necessária (`Controller`, `Request`, `Response`, `Pageable`, `GET`, `POST`).
- Tabelas com FK de empresa têm `empresa_id`, relacionamentos restritos à empresa autenticada; nunca aceitar `empresa_id` arbitrário para acessar outra empresa.
- Migrações Flyway versionadas e aditivas por padrão; remoção física somente quando explicitamente autorizada e após revisão de dependências.
- Entidades não devem declarar atributos persistentes que não existam no schema vigente. Campos internos indispensáveis à segurança, auditoria, relacionamento e integridade são permitidos mesmo quando não visíveis na UI.
- O DTO de uma tela deve representar somente os campos presentes no contrato de domínio dessa tela, e não toda a história do schema.
- Credenciais e senhas permanecem em entidades separadas de `funcionarios`, com hash e autorização por papel. EPIs são vinculados via entregas auditadas.

## Contrato observado nas imagens (Funcionários)

**Dados gerais:** foto, CPF, nome completo, RG, data de nascimento, gênero, observações.

**Endereço:** CEP, logradouro, número, bairro, UF, cidade e complemento.

**Empresas vinculadas:** empresa selecionada e devidamente autorizada; no modelo atual, o vínculo é de uma empresa por funcionário. A expansão multiempresa precisa garantir que leitura e escrita sigam cada tenant.

**Dados profissionais:** perfil (orçamentista/vendedor ou outras funções), registro profissional, tipo, área, cargo, nível acadêmico, admissão, carteira de trabalho, PIS, salário-base, comissões em peças e serviços, linhas de transporte, valor de passagem, observações.

**EPIs:** catálogo separado; histórico por funcionário com EPI, empresa, data de entrega e validade.

**Contatos:** múltiplos contatos por funcionário, principal único, nome, gênero, nascimento e meios de comunicação (e-mail, WhatsApp, celular, telefone).

**Usuário vinculado:** login e credenciais são do módulo autenticação, nunca colunas da tabela funcionários.

## Migrações executáveis

- `V294__funcionarios_referencia_profissional_contatos.sql`: reduz obrigatoriedade da data de admissão, cria campos profissionais e contatos.
- `V295__perfil_profissional_funcionarios.sql`: adiciona coluna de perfil profissional.
- `V296__remover_campos_legados_funcionarios.sql`: exclusão intencional de 26 colunas de funcionário fora das telas e sem necessidade operacional identificada.
- `FuncionarioReferenciaIntegracaoTest`: verifica a migração aplicada em PostgreSQL 16, ausência das colunas legadas, existência das novas colunas e isolamento/CRUD de contatos.

## Restrições e pendências

- **O padrão é incremental:** não declarar todo o repositório refatorado; outros módulos possuem código herdado e precisam de migrações próprias. Esta entrega padroniza a área de Funcionários.
- O login da autenticação atual é o e-mail: o campo separado `login` da imagem ainda não possui identidade autônoma. Até modificar a autenticação, o fluxo exige que o login corresponda ao e-mail; jamais gravar um login fictício.
- A relação com uma segunda empresa não pode ser adicionada por simples alteração de interface — exige novo vínculo e autorização por empresa.
- A data de admissão é opcional no cadastro básico e nunca gerada artificialmente. Matrícula interna `AUTO-` é identificador técnico não solicitado ao usuário.
- Homologação visual 100% exige capturas em navegador, comparação de dimensões, estados, navegação, responsividade e WCAG 2.2 AA.
