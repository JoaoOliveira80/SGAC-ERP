# 01 — Escopo e contexto do negócio

## Problema simulado

Uma organização precisa organizar solicitações internas de compras realizadas por diferentes departamentos. Sem um fluxo padronizado, os pedidos podem perder rastreabilidade, duplicar registros e dificultar a identificação de responsáveis e valores comprometidos.

## Objetivo do SGAC

Centralizar os cadastros de apoio e a tramitação básica de solicitações de aquisição, permitindo consulta, aprovação/rejeição e registro de histórico.

## Stakeholders simulados

| Papel | Necessidade | Situação no protótipo |
| --- | --- | --- |
| Solicitante | Registrar uma compra com itens e justificativa | Formulário implementado |
| Analista de compras | Consultar e acompanhar solicitações | Listagens e detalhes implementados |
| Aprovador | Registrar uma decisão motivada | Fluxo implementado, **sem identificação autenticada** |
| Gestor | Consultar indicadores | Dashboard implementado |
| Administração de TI | Manter dados mestres | Cadastros e Swagger implementados |

## Dentro do escopo

- Cadastro e manutenção de departamentos, fornecedores e centros de custo;
- Registro de solicitações com itens, preços unitários e valores totais;
- Fluxo `RASCUNHO → PENDENTE → APROVADA ou REJEITADA`;
- Consulta ao histórico e indicadores por status;
- Persistência relacional, validações, documentação técnica e testes unitários.

## Fora do escopo

- Estoque, recebimento, faturamento, pagamento e contabilidade;
- Compras públicas, licitação e normas financeiras específicas;
- Integração com sistemas ERP externos;
- Gestão de usuários, login e controle real de perfis;
- Aprovação automática por alçadas de valor, saldo orçamentário e assinatura eletrônica;
- Operação de produção, multiempresa e escalabilidade comprovada.

**Natureza:** estudo independente baseado em requisitos e perfis simulados. O SGAC não representa um sistema encomendado ou implantado em uma instituição real.
