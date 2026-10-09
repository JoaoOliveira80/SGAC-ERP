# 06 — Apresentação técnica do SGAC no portfólio

## Descrição objetiva

> **SGAC — Sistema Integrado de Gestão de Aquisições e Suprimentos** é um protótipo funcional de ERP de aquisições que desenvolvi para exercitar análise de sistemas e fluxo corporativo. O projeto reúne cadastros administrativos, solicitações de aquisição com itens e cálculos monetários, fluxo de aprovação/rejeição e histórico de tramitação. Construído com React, TypeScript, Java, Spring Boot e SQL Server, utiliza Flyway para versionamento do banco e Swagger para documentação da API.

## Como demonstrar em 3 minutos

1. **Problema e atores (30s):** apresente o problema simulado de rastreabilidade de requisições e os papéis de solicitante, compras, aprovador e gestor.
2. **Modelagem (40s):** mostre departamentos, centros de custo, fornecedores e relacionamentos no diagrama ER.
3. **Implementação (60s):** crie uma solicitação com itens e demonstre os valores calculados pelo backend, a mudança `RASCUNHO → PENDENTE → APROVADA` e o histórico.
4. **Qualidade (30s):** aponte validações, regras de negócio no service, migrations Flyway, testes unitários e respostas de erro.
5. **Limites e evolução (20s):** explique que autenticação, autorização, integração com ERP externo e controles de produção são próximos passos, não recursos já presentes.

## Relação com competências de Analista de Sistemas

| Competência | Evidência no SGAC |
| --- | --- |
| Levantamento de requisitos | Documento de stakeholders, RF, RNF e regras de negócio (simulados) |
| Banco SQL | Modelo relacional, foreign keys, índices e `DECIMAL(18,2)` |
| Sistemas ERP | Fluxo demonstrativo de gestão de aquisições |
| Documentação | Diagramas ER, arquitetura, regras e plano de testes |
| Desenvolvimento e manutenção | Backend REST, frontend e validação do fluxo |
| APIs e integração | HTTP REST entre React e Spring Boot; **não** há integração externa real |
| Gerenciamento de processos | Estados, transições, validação e rastreabilidade funcional |
| Git e boas práticas | Histórico de commits, configurações separadas, migrations e testes |

## O que não afirmar

- Não diga que desenvolveu uma integração com **TOTVS RM**;
- Não diga que trabalhou profissionalmente com ERP só por ter construído este projeto;
- Não afirme que possui controle de acesso, login, autenticação ou trilha de auditoria de identidade;
- Não apresente indicadores como resultados de uma organização real: os dados são demonstrativos;
- Não associe este protótipo ao ambiente real do ISD ou de empregadores.

Os pontos acima tornam a apresentação mais transparente e ajudam a diferenciar domínio conceitual de ERP de experiência profissional comprovada.
