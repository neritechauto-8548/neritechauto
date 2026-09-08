-- Compatibilidade temporária para endpoints legados de composição de orçamento.
-- O catálogo canônico usa OS_EDITAR. Alguns endpoints ainda referenciam OS_ALTERAR;
-- este alias herda somente das funções que já possuem OS_EDITAR e, portanto,
-- não amplia autoridade. Remover após normalizar os @PreAuthorize legados.

INSERT INTO permissoes (
    chave,
    valor,
    descricao,
    versao,
    data_cadastro,
    data_atualizacao)
VALUES (
    'Permissões da ordem de serviço',
    'OS_ALTERAR',
    'Compatibilidade temporária para edição de composição de orçamento',
    0,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP)
ON CONFLICT (valor) DO NOTHING;

INSERT INTO funcoes_permissoes (funcao_id, permissao_id, empresa_id, created_at)
SELECT DISTINCT
    fp.funcao_id,
    target.id,
    fp.empresa_id,
    CURRENT_TIMESTAMP
FROM funcoes_permissoes fp
JOIN permissoes source ON source.id = fp.permissao_id
JOIN permissoes target ON target.valor = 'OS_ALTERAR'
WHERE source.valor = 'OS_EDITAR'
ON CONFLICT (funcao_id, permissao_id) DO NOTHING;
