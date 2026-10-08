-- SGAC ERP - V3: solicitacoes, itens e trilha de auditoria.
-- Valores financeiros em DECIMAL(18,2). Datas em UTC (sem offset armazenado).

CREATE TABLE dbo.solicitacoes (
    id BIGINT IDENTITY(1,1) NOT NULL,
    centro_custo_id BIGINT NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    solicitante NVARCHAR(120) NOT NULL,
    justificativa NVARCHAR(500) NOT NULL,
    status VARCHAR(20) NOT NULL,
    valor_total DECIMAL(18,2) NOT NULL,
    criada_em DATETIME2(6) NOT NULL,
    versao BIGINT NOT NULL,
    CONSTRAINT PK_solicitacoes PRIMARY KEY (id),
    CONSTRAINT FK_solicitacoes_centro FOREIGN KEY (centro_custo_id) REFERENCES dbo.centros_custo(id),
    CONSTRAINT FK_solicitacoes_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES dbo.fornecedores(id),
    CONSTRAINT CK_solicitacoes_status CHECK (status IN ('RASCUNHO', 'PENDENTE', 'APROVADA', 'REJEITADA')),
    CONSTRAINT CK_solicitacoes_valor CHECK (valor_total > 0)
);
CREATE INDEX IX_solicitacoes_status_data ON dbo.solicitacoes (status, criada_em);
CREATE INDEX IX_solicitacoes_centro ON dbo.solicitacoes (centro_custo_id);

CREATE TABLE dbo.solicitacao_itens (
    id BIGINT IDENTITY(1,1) NOT NULL,
    solicitacao_id BIGINT NOT NULL,
    descricao NVARCHAR(200) NOT NULL,
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(18,2) NOT NULL,
    subtotal DECIMAL(18,2) NOT NULL,
    CONSTRAINT PK_solicitacao_itens PRIMARY KEY (id),
    CONSTRAINT FK_solicitacao_itens_solicitacao FOREIGN KEY (solicitacao_id)
        REFERENCES dbo.solicitacoes(id),
    CONSTRAINT CK_itens_quantidade CHECK (quantidade > 0),
    CONSTRAINT CK_itens_valores CHECK (valor_unitario > 0 AND subtotal > 0)
);
CREATE INDEX IX_itens_solicitacao ON dbo.solicitacao_itens (solicitacao_id);

CREATE TABLE dbo.solicitacao_historico (
    id BIGINT IDENTITY(1,1) NOT NULL,
    solicitacao_id BIGINT NOT NULL,
    status_anterior VARCHAR(20) NULL,
    status_novo VARCHAR(20) NOT NULL,
    responsavel NVARCHAR(120) NOT NULL,
    observacao NVARCHAR(500) NULL,
    registrado_em DATETIME2(6) NOT NULL,
    CONSTRAINT PK_solicitacao_historico PRIMARY KEY (id),
    CONSTRAINT FK_historico_solicitacao FOREIGN KEY (solicitacao_id)
        REFERENCES dbo.solicitacoes(id),
    CONSTRAINT CK_historico_anterior CHECK (status_anterior IS NULL OR status_anterior IN ('RASCUNHO', 'PENDENTE', 'APROVADA', 'REJEITADA')),
    CONSTRAINT CK_historico_novo CHECK (status_novo IN ('RASCUNHO', 'PENDENTE', 'APROVADA', 'REJEITADA'))
);
CREATE INDEX IX_historico_solicitacao_data
    ON dbo.solicitacao_historico (solicitacao_id, registrado_em, id);
