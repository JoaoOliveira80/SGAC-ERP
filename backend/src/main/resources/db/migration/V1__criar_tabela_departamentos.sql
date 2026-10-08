CREATE TABLE dbo.departamentos (
    id BIGINT IDENTITY(1,1) NOT NULL,

    codigo VARCHAR(20) NOT NULL,

    nome NVARCHAR(120) NOT NULL,

    ativo BIT NOT NULL
        CONSTRAINT DF_departamentos_ativo
        DEFAULT (1),

    CONSTRAINT PK_departamentos
        PRIMARY KEY (id),

    CONSTRAINT UQ_departamentos_codigo
        UNIQUE (codigo)
);
