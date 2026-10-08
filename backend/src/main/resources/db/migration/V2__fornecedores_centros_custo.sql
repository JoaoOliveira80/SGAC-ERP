-- SGAC ERP - Migracao V2
-- Cadastro de fornecedores e centros de custo vinculados a departamentos.

CREATE TABLE dbo.fornecedores (
    id BIGINT IDENTITY(1,1) NOT NULL,
    cnpj VARCHAR(14) NOT NULL,
    razao_social NVARCHAR(160) NOT NULL,
    email VARCHAR(160) NULL,
    ativo BIT NOT NULL CONSTRAINT DF_fornecedores_ativo DEFAULT (1),
    CONSTRAINT PK_fornecedores PRIMARY KEY (id),
    CONSTRAINT UQ_fornecedores_cnpj UNIQUE (cnpj),
    CONSTRAINT CK_fornecedores_cnpj_formato CHECK (
        LEN(cnpj) = 14 AND cnpj NOT LIKE '%[^0-9]%'
    )
);

CREATE TABLE dbo.centros_custo (
    id BIGINT IDENTITY(1,1) NOT NULL,
    codigo VARCHAR(20) NOT NULL,
    nome NVARCHAR(120) NOT NULL,
    departamento_id BIGINT NOT NULL,
    ativo BIT NOT NULL CONSTRAINT DF_centros_custo_ativo DEFAULT (1),
    CONSTRAINT PK_centros_custo PRIMARY KEY (id),
    CONSTRAINT UQ_centros_custo_codigo UNIQUE (codigo),
    CONSTRAINT FK_centros_custo_departamento FOREIGN KEY (departamento_id)
        REFERENCES dbo.departamentos (id)
);

CREATE INDEX IX_centros_custo_departamento_id
    ON dbo.centros_custo (departamento_id);
