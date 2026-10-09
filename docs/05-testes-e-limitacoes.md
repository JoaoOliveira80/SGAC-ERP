# 05 — Plano de testes, segurança e limitações

## Testes automatizados incluídos

| Conjunto | Cenários principais | Dependência externa |
| --- | --- | --- |
| `DepartamentoTest` | Criação, alteração, desativação | Nenhuma |
| `DepartamentoServiceTest` | Código duplicado, normalização, edição, inexistente | Mockito (sem banco) |
| `FornecedorRequestValidationTest` | Formato de CNPJ, e-mail e campos válidos | Bean Validation (sem banco) |
| `SolicitacaoTest` | Cálculo de valores e transições de estado | Nenhuma |
| `SolicitacaoServiceTest` | Centro inexistente e transições proibidas | Mockito (sem banco) |

Execute `cd backend && ./mvnw test`. A ausência de banco nos testes unitários é intencional. **O CI não demonstra que a integração JDBC, o SQL Server ou as migrations funcionem**.

## Matriz de verificações manuais de integração

1. Verificar `GET /api/health` e Swagger;
2. Cadastrar e listar departamento, centro de custo e fornecedor;
3. Tentar códigos ou CNPJ repetidos e verificar HTTP 409;
4. Desativar fornecedor/centro e verificar que não são usados em nova solicitação;
5. Criar solicitação com 2 itens: `2 x 650,00 + 3 x 80,50 = 1.541,50`;
6. Enviar, aprovar/rejeitar e consultar histórico;
7. Tentar aprovar duas vezes e verificar HTTP 409;
8. Verificar dashboard, busca e responsividade da interface;
9. Conferir erros de campos obrigatórios no Swagger;
10. Executar `npm run build` no frontend.

## Segurança e controles ainda pendentes

**Não publicar esta API como aplicação aberta na internet.** O SGAC tem uma interface de administração, mas não exige login nem verifica se a pessoa que solicita a aprovação é um aprovador legítimo. Os campos de responsável são autodeclarados.

- Conta `sa` do banco é usada somente em ambiente local; migrar para usuário com menos privilégios;
- Certificado SQL Server é confiado apenas no desenvolvimento (`trustServerCertificate=true`);
- Credenciais devem ficar fora do Git, fora dos ZIPs de compartilhamento e fora de capturas de tela;
- É necessário implementar autenticação, autorização e identidade auditável antes de considerar produção;
- CNPJ é validado apenas como **14 dígitos**, sem dígitos verificadores;
- Não foram realizados testes de carga, acessibilidade automatizada ou teste de invasão;
- Conflitos de concorrência são parcialmente tratados pelo versionamento otimista, mas carecem de testes de integração concorrente;
- Operações administrativas não possuem registro de quem alterou cadastro;
- E-mails e nomes usados na demonstração devem ser fictícios.

## Tratamento de erros

A API retorna `ProblemDetail` (formato JSON com `status`, `detail`, `path`, `timestamp`) para erros de requisição e conflito. Na validação dos DTOs, a propriedade `campos` indica quais campos falharam. Erros de banco internos não são incluídos na resposta ao cliente.

## Critério de conclusão do protótipo

Projeto com build funcionando em ambiente local, fluxo principal testado manualmente, documentação coerente com o código, ausência de segredos compartilhados e limitações honestamente informadas. **Isso não equivale a pronto para produção.**
