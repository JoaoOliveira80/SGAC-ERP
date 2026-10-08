# SGAC - Etapa 5: Solicitações de Aquisição e Aprovações

## Objetivo
Permitir abrir uma solicitação de compra com itens e valores reais, encaminhá-la para aprovação e registrar a decisão em trilha de auditoria.

## Requisitos funcionais
- RF01: Criar solicitação vinculada a centro de custo ativo (cujo departamento esteja ativo) e fornecedor ativo.
- RF02: Inserir entre 1 e 30 itens, cada um com descrição, quantidade e valor unitário.
- RF03: Calcular subtotais e total no backend usando BigDecimal.
- RF04: Criar em RASCUNHO, enviar para PENDENTE e então APROVADA ou REJEITADA.
- RF05: Exigir responsável em aprovação/rejeição e justificativa para rejeição.
- RF06: Consultar solicitações, seus itens e histórico de transições.
- RF07: Bloquear transições inválidas com HTTP 409 e referências inativas com HTTP 422.

## Fluxo
RASCUNHO -> PENDENTE -> APROVADA
                     -> REJEITADA
O protótipo não possui reabertura/edição pós-criação; isto é intencional para o MVP.

## Modelo de dados
- solicitacoes: centro_custo_id, fornecedor_id, solicitante, justificativa, status, valor_total, criada_em, versao.
- solicitacao_itens: solicitacao_id, descricao, quantidade, valor_unitario, subtotal.
- solicitacao_historico: solicitacao_id, status_anterior, status_novo, responsavel, observacao, registrado_em.
- V3 é aplicada pelo Flyway; não altera V1/V2.

## Rotas REST
- GET /api/solicitacoes
- POST /api/solicitacoes
- GET /api/solicitacoes/{id}
- POST /api/solicitacoes/{id}/enviar
- POST /api/solicitacoes/{id}/aprovar
- POST /api/solicitacoes/{id}/rejeitar
- GET /api/solicitacoes/{id}/historico

## Limitações e próximos passos
Projeto demonstrativo para portfólio, não ERP pronto para produção. Ainda sem autenticação e autorização reais, logo o nome do aprovador informado no JSON **não é identidade verificada**. Para produção: Spring Security/JWT ou login institucional, perfis e segregação de funções, proteção contra escrita concorrente e paginação, monitoramento e logs, validação completa de CNPJ, tratamento de erros padronizado e testes de integração com SQL Server.
A API opera com datas em UTC armazenadas em DATETIME2; a UI deve formatá-las no fuso desejado.
