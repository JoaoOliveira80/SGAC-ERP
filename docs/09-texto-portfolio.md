# Conteúdo para o portfólio — SGAC

## Título do projeto

**SGAC — Sistema Integrado de Gestão de Aquisições e Suprimentos**

## Tagline

ERP demonstrativo de aquisições: cadastros administrativos, solicitações, aprovação e indicadores conectados ao SQL Server.

## Descrição para card (curta)

Protótipo funcional de ERP para gestão de aquisições, criado como estudo independente de análise de sistemas. Desenvolvido com React, TypeScript, Java, Spring Boot e SQL Server, com fluxos de aprovação, histórico, modelagem relacional e documentação técnica.

## Texto para página do projeto (médio)

O SGAC nasceu de um desafio simulado: centralizar solicitações internas de aquisição e tornar seus dados e decisões mais rastreáveis. Planejei um recorte funcional com departamentos, fornecedores, centros de custo e solicitações com múltiplos itens. Modelei as entidades e seus relacionamentos no SQL Server e construí uma API Spring Boot responsável por validações, cálculo monetário, transições de status e histórico de tramitação. No frontend, criei um painel React/TypeScript com formulários, consultas e indicadores baseados nos dados reais do banco local. A aplicação inclui migrações Flyway, Swagger/OpenAPI, testes unitários e documentação de requisitos, regras de negócio e arquitetura.

**Resultado do estudo:** fluxo demonstrativo completo de solicitação, envio para análise e aprovação/rejeição, com registros consultáveis pelo sistema. O cenário e os dados são fictícios; não há integração com TOTVS RM nem implantação corporativa.

## Competências para destacar

- Análise de requisitos e documentação funcional de um processo simulado;
- Modelagem relacional e consultas em Microsoft SQL Server;
- API REST em Java/Spring Boot com regras e validações;
- Interface React/TypeScript integrada ao backend;
- Testes JUnit, Flyway e versionamento Git.

## Tecnologias visíveis no card

`React` · `TypeScript` · `Spring Boot` · `Java 21` · `SQL Server` · `Flyway` · `Docker` · `REST`

## Evidências e links

- Preview: imagem do `dashboard.webp`;
- Galeria: `solicitacoes.webp`, `aprovacoes.webp`, `centros-de-custo.webp`;
- Link **Repositório**: preencher após criar o repositório no GitHub;
- Link **Estudo de caso**: apontar para `docs/07-estudo-de-caso.md` no mesmo repositório.

## Apresentação rápida em entrevista (aprox. 30 segundos)

> Desenvolvi o SGAC como projeto independente para aprofundar análise de sistemas além do CRUD. Ele simula um processo de compras: cadastros de apoio, solicitações com vários itens, cálculo dos valores no backend e aprovação com histórico. Usei React e TypeScript no frontend, Java com Spring Boot na API e SQL Server com Flyway no banco. Documentei requisitos, fluxos e modelo de dados, além de escrever testes unitários. É um protótipo de estudo, ainda sem login e integrações externas.

## Transparência

Evite apresentar o SGAC como ERP contratado ou implementado para uma organização. As capturas e valores são de ambiente demonstrativo. O projeto **não substitui experiência profissional com TOTVS RM**.
