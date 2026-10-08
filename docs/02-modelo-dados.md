# Modelo relacional - SGAC (etapa atual)

```text
DEPARTAMENTOS
  id (PK)
  codigo (UQ)
  nome
  ativo
      1
      |
      | N
CENTROS_CUSTO
  id (PK)
  codigo (UQ)
  nome
  departamento_id (FK -> departamentos.id)
  ativo

FORNECEDORES
  id (PK)
  cnpj (UQ)
  razao_social
  email (opcional)
  ativo
```

## Proximas entidades previstas
- solicitacoes_compra -> departamento, centro_custo, fornecedor e status
- itens_solicitacao -> solicitacao, descricao, quantidade, valor_unitario
- eventos_aprovacao -> solicitacao, decisao, data/hora, autor

Toda mudanca de esquema sera criada como nova migration Flyway; nunca editar uma migration ja executada.
