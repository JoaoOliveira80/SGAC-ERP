# 04 — Arquitetura e fluxos funcionais

## Arquitetura de alto nível

```mermaid
flowchart LR
  U[Usuario no navegador] --> F[React + TypeScript / Vite]
  F -->|/api REST JSON| C[Spring Boot Controllers]
  C --> S[Services e regras]
  S --> R[Spring Data Repositories]
  R --> D[(SQL Server 2022)]
  M[Flyway V1-V3] --> D
  C --> V[Bean Validation]
  C --> W[Swagger OpenAPI]
```

Na execução local, o Vite encaminha `/api` para o backend na porta 8080. O backend se conecta ao SQL Server pelo driver JDBC em `localhost:14333`.

## Fluxo da solicitação

```mermaid
flowchart TD
  A[Selecionar centro de custo e fornecedor] --> B{Cadastros ativos?}
  B -->|Nao| X[Rejeitar operacao: HTTP 422]
  B -->|Sim| C[Informar solicitante, justificativa e itens]
  C --> D[Calcular subtotais e valor total]
  D --> E[Salvar como RASCUNHO]
  E --> H[Registrar evento de criacao]
  E --> F[Enviar para aprovacao]
  F --> G{Ainda validos e ativos?}
  G -->|Nao| X
  G -->|Sim| I[PENDENTE]
  I --> J{Decisao}
  J -->|Aprovar| K[APROVADA]
  J -->|Rejeitar com motivo| L[REJEITADA]
  K --> N[Registrar decisao no historico]
  L --> N
```

## Contrato principal da API

| Método | Endpoint | Finalidade |
| --- | --- | --- |
| GET | `/api/departamentos` | Lista departamentos |
| GET | `/api/fornecedores` | Lista fornecedores |
| GET | `/api/centros-custo` | Lista centros de custo |
| GET | `/api/solicitacoes` | Lista resumos |
| POST | `/api/solicitacoes` | Cadastra nova solicitação |
| GET | `/api/solicitacoes/{id}` | Detalhe com itens |
| POST | `/api/solicitacoes/{id}/enviar` | Muda rascunho para pendente |
| POST | `/api/solicitacoes/{id}/aprovar` | Aprova pendente |
| POST | `/api/solicitacoes/{id}/rejeitar` | Rejeita pendente |
| GET | `/api/solicitacoes/{id}/historico` | Consultar trilha funcional |

Os endpoints de cadastro também implementam `POST`, `PUT`, `GET/{id}` e `PATCH/{id}/status`. O Swagger detalha os contratos e payloads.

## Decisões de projeto

- A lógica de aprovação fica nos Services/Entidades, não na interface;
- Valores são calculados no backend, não aceitos como totais enviados pelo navegador;
- As transações mantêm mudança de status e registro de histórico como uma operação;
- Respostas padronizadas diferenciam erros de validação, ausência de recurso e conflitos.
