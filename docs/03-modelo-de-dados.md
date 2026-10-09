# 03 — Modelo relacional de dados

Diagrama lógico simplificado das tabelas implementadas nas migrações Flyway V1–V3:

```mermaid
erDiagram
  DEPARTAMENTOS ||--o{ CENTROS_CUSTO : possui
  CENTROS_CUSTO ||--o{ SOLICITACOES : origina
  FORNECEDORES ||--o{ SOLICITACOES : atende
  SOLICITACOES ||--|{ SOLICITACAO_ITENS : contem
  SOLICITACOES ||--o{ SOLICITACAO_HISTORICO : registra

  DEPARTAMENTOS {
    bigint id PK
    varchar codigo UK
    nvarchar nome
    bit ativo
  }
  CENTROS_CUSTO {
    bigint id PK
    varchar codigo UK
    nvarchar nome
    bigint departamento_id FK
    bit ativo
  }
  FORNECEDORES {
    bigint id PK
    varchar cnpj UK
    nvarchar razao_social
    varchar email
    bit ativo
  }
  SOLICITACOES {
    bigint id PK
    bigint centro_custo_id FK
    bigint fornecedor_id FK
    nvarchar solicitante
    nvarchar justificativa
    varchar status
    decimal valor_total
    datetime2 criada_em
    bigint versao
  }
  SOLICITACAO_ITENS {
    bigint id PK
    bigint solicitacao_id FK
    nvarchar descricao
    int quantidade
    decimal valor_unitario
    decimal subtotal
  }
  SOLICITACAO_HISTORICO {
    bigint id PK
    bigint solicitacao_id FK
    varchar status_anterior
    varchar status_novo
    nvarchar responsavel
    nvarchar observacao
    datetime2 registrado_em
  }
```

> Este diagrama descreve as tabelas principais, não replica todas as constraints e os índices. O modelo executável de referência é o conjunto `backend/src/main/resources/db/migration/V*.sql`.

## Decisões de modelagem

- **Foreign keys** asseguram vínculo entre centros, fornecedores e solicitações;
- **`DECIMAL(18,2)`** evita a imprecisão binária típica de ponto flutuante em valores monetários;
- **`versao`** na solicitação apoia o controle de concorrência otimista do Hibernate (`@Version`);
- **Índices** aceleram consultas típicas por status, data e relacionamentos;
- **Não exclusão física** dos cadastros protege a consistência de referências históricas.

## Evolução do schema

- `V1__criar_tabela_departamentos.sql` — departamentos;
- `V2__fornecedores_centros_custo.sql` — fornecedores e centros de custo;
- `V3__solicitacoes_itens_historico.sql` — solicitações, itens, histórico e índices.

**Nunca edite scripts V1–V3 após aplicados.** Para alterações futuras, crie uma nova migração (`V4__...sql`).
