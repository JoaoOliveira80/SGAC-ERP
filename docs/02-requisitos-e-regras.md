# 02 — Requisitos funcionais, não funcionais e regras de negócio

## Requisitos funcionais — implementados

| ID | Requisito | Critério de aceite |
| --- | --- | --- |
| RF-01 | Manter departamentos | Criar, consultar, atualizar e ativar/desativar |
| RF-02 | Manter fornecedores | Impedir CNPJ de 14 dígitos duplicado; permitir ativação/desativação |
| RF-03 | Manter centros de custo | Associar centro a um departamento existente e ativo |
| RF-04 | Criar solicitações | Informar solicitante, justificativa, centro, fornecedor e de 1 a 30 itens |
| RF-05 | Calcular valores | Total igual à soma dos subtotais, com `BigDecimal` e duas casas |
| RF-06 | Enviar solicitação | Somente rascunho com itens, centro/departamento/fornecedor ativos |
| RF-07 | Registrar decisão | Aprovar ou rejeitar somente solicitação pendente |
| RF-08 | Histórico de tramitação | Registrar criação, envio e decisão |
| RF-09 | Consultar indicadores | Totais, valores aprovados e contagem por status exibidos na interface |
| RF-10 | Navegar pelo portal | Interface responsiva com páginas de cadastros, pedidos e aprovações |

## Requisitos não funcionais — adotados / limites

| ID | Requisito | Tratamento |
| --- | --- | --- |
| RNF-01 | Integridade relacional | Foreign keys e restrições no SQL Server |
| RNF-02 | Evolução de esquema | Migrações versionadas por Flyway |
| RNF-03 | Precisão monetária | `DECIMAL(18,2)` no banco e `BigDecimal` no Java |
| RNF-04 | Separação de responsabilidades | DTO / Controller / Service / Repository / Entity |
| RNF-05 | Feedback sobre falhas | HTTP 400, 404, 409 e 422, com detalhes padronizados |
| RNF-06 | Rastreabilidade básica | Histórico de mudança de status e observação |
| RNF-07 | Segurança de produção | **Não atendido**: sem autenticação/autorização e ainda usando `sa` local |
| RNF-08 | Desempenho/carga | **Não medido**: sem testes de carga ou SLA |

## Regras de negócio verificáveis

| ID | Regra |
| --- | --- |
| RB-01 | Códigos de departamento e centro de custo devem ser únicos |
| RB-02 | O CNPJ do fornecedor deve ter 14 dígitos e ser único (não valida dígitos verificadores) |
| RB-03 | Não é permitido associar ou reativar centro de custo vinculado a departamento inativo |
| RB-04 | Não é permitido criar solicitação para fornecedor/centro/departamento inativo |
| RB-05 | Uma solicitação tem no mínimo 1 e no máximo 30 itens, exigidos pela validação da API |
| RB-06 | Quantidade de cada item é positiva e limitada pelo DTO; preço unitário deve ser maior ou igual a R$ 0,01 |
| RB-07 | Uma solicitação nova inicia em `RASCUNHO` |
| RB-08 | Envio somente de `RASCUNHO` para `PENDENTE` |
| RB-09 | Decisão somente de `PENDENTE` para `APROVADA` ou `REJEITADA` |
| RB-10 | Rejeição exige justificativa de ao menos 10 caracteres |
| RB-11 | Cada mudança de status gera registro de histórico na mesma transação da alteração |
| RB-12 | Desativação lógica de cadastros preserva referências históricas |

## Limites de confiança

O campo `responsavel` das decisões é fornecido no corpo HTTP pelo cliente: é **autodeclarado** e pode ser falsificado. O SGAC, portanto, registra uma trilha funcional de eventos, mas ainda **não oferece auditoria de identidade confiável**. A transição para produção exige autenticação e autorização por perfil de usuário.
