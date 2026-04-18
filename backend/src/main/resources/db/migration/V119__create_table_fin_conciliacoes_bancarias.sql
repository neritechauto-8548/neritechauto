CREATE TABLE fin_conciliacoes_bancarias (
    id BIGSERIAL PRIMARY KEY,
    conta_bancaria_id BIGINT,
    periodo_inicio DATE NOT NULL,
    periodo_fim DATE NOT NULL,
    saldo_inicial_sistema DECIMAL(15,2) NOT NULL,
    saldo_final_sistema DECIMAL(15,2) NOT NULL,
    saldo_inicial_extrato DECIMAL(15,2) NOT NULL,
    saldo_final_extrato DECIMAL(15,2) NOT NULL,
    diferenca_encontrada DECIMAL(15,2),
    total_movimentacoes_sistema INT,
    total_movimentacoes_extrato INT,
    movimentacoes_nao_conciliadas INT DEFAULT 0,
    status VARCHAR(30),
    observacoes TEXT,
    arquivo_extrato_url VARCHAR(500),
    responsavel_conciliacao BIGINT NOT NULL,
    data_inicio_conciliacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_fim_conciliacao TIMESTAMP,
    aprovada_por BIGINT,
    data_aprovacao TIMESTAMP,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    CONSTRAINT fk_fin_conciliacoes_conta FOREIGN KEY (conta_bancaria_id) REFERENCES fin_contas_bancarias(id)
);

CREATE INDEX idx_fin_conciliacoes_conta ON fin_conciliacoes_bancarias(conta_bancaria_id);
