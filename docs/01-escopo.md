# SGAC - Sistema Integrado de Gestao de Aquisicoes e Suprimentos

## Objetivo
Demonstrar a modelagem de processos e o desenvolvimento de um mini ERP administrativo: da solicitacao interna de compra a aprovacao e historico da decisao.

## Atores previstos
- Solicitante: solicita a aquisicao vinculada a seu departamento e centro de custo.
- Aprovador: aceita ou rejeita solicitacoes segundo regras de negocio.
- Administrador: mantem cadastros organizacionais.

## Cadastros ja disponiveis nesta etapa
- Departamentos: codigo, nome, status ativo.
- Centros de custo: codigo, nome, departamento responsavel, status ativo.
- Fornecedores: CNPJ, razao social, email, status ativo.

## Regras desta etapa
- Codigos de departamentos e centros de custo nao podem se repetir.
- Fornecedores nao podem repetir o CNPJ.
- Centros de custo devem estar ligados a departamentos existentes e ativos no momento do cadastro/edicao.
- Exclusao logica (ativacao/desativacao) preserva historico e chaves estrangeiras.
- Campos de entrada tem validacao na API e constraints no SQL Server.

## Fora do escopo desta etapa
- Autenticacao e perfis, solicitacoes, itens, aprovacoes, trilha de auditoria.
- CNPJ: verifica-se formato de 14 digitos; digitos verificadores nao sao validados ainda.
- Integracao real com TOTVS RM: este ERP e demonstrativo e independente.
