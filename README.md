# SGAC — Sistema Integrado de Gestão de Aquisições e Suprimentos

**Protótipo funcional de ERP**, desenvolvido como projeto de portfólio para demonstrar análise de requisitos, modelagem relacional, regras de negócio, desenvolvimento Full Stack e documentação técnica/funcional.

> **Escopo:** demonstração local. Não é um produto pronto para produção, não implementa autenticação/autorização e **não possui integração oficial com TOTVS RM**.

## Funcionalidades implementadas

| Módulo | Funcionalidades |
| --- | --- |
| Dashboard | Indicadores de solicitações, aprovações e valores aprovados, obtidos pela API |
| Departamentos | Cadastrar, listar, consultar, editar e ativar/desativar |
| Fornecedores | Cadastrar, listar, consultar, editar e ativar/desativar; verificação de formato do CNPJ |
| Centros de custo | CRUD com desativação lógica, vinculados a departamento ativo |
| Solicitações | Criar com vários itens, calcular subtotal/total, consultar e listar |
| Aprovações | Enviar rascunho, aprovar ou rejeitar solicitação pendente |
| Histórico | Registrar criação, envio e decisão com data, responsável informado e observação |
| API | Endpoints REST documentados via Swagger UI |

### Arquitetura

- **Frontend:** React 19, TypeScript, Vite 7, Tailwind CSS 4, TanStack Query e React Router.
- **Backend:** Java 21, Spring Boot 4.1.1, Spring Web MVC, Spring Data JPA, Hibernate, Bean Validation.
- **Banco:** Microsoft SQL Server 2022 Developer em Docker Compose.
- **Versionamento do banco:** Flyway (`V1`, `V2`, `V3`).
- **Testes:** JUnit 5, Mockito e Bean Validation. As verificações automatizadas existentes não substituem testes de integração com SQL Server.

Os componentes de interface foram elaborados especialmente para o projeto; o uso das tecnologias acima não implica que haja integração com produtos corporativos proprietários.

## Pré-requisitos

Java 21+, Node.js 22+, Docker Desktop com Docker Compose e Git. Os comandos a seguir foram planejados para Git Bash no Windows.

## Execução local

**1. Configurar credenciais**

Na raiz do projeto, copie `.env.example` para `.env` e configure uma senha forte em `MSSQL_SA_PASSWORD`. Se você já possui um volume persistido do SQL Server, mantenha a senha que foi definida quando esse volume foi criado.

**Nunca faça commit do `.env` ou compartilhe esse arquivo.** A aplicação utiliza `sa` somente no ambiente demonstrativo local.

**2. Iniciar SQL Server**

```bash
docker compose up -d
docker compose ps
```

O SQL Server fica disponível exclusivamente em `127.0.0.1:14333` no computador host.

**3. Criar o banco uma única vez**

Confira os bancos existentes:

```bash
docker compose exec -T sqlserver bash -c '/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -Q "SELECT name FROM sys.databases"'
```

Se `SGAC_DB` ainda **não existir**, crie-o:

```bash
docker compose exec -T sqlserver bash -c '/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -b -Q "CREATE DATABASE SGAC_DB"'
```

**4. Iniciar backend**

```bash
cd backend
set -a
source ../.env
set +a
./mvnw spring-boot:run
```

A API estará em `http://localhost:8080` e o Swagger em `http://localhost:8080/swagger-ui/index.html`.

**5. Iniciar frontend** (em outro terminal):

```bash
cd frontend
npm ci
npm run dev
```

A interface estará em `http://localhost:5173`. O Vite encaminha `/api` para a API local. Para um build de produção do frontend, use `npm run build`; os arquivos ficam em `frontend/dist/`.

## Testes e qualidade

No backend (com o `.env` carregado se necessário):

```bash
cd backend
./mvnw test
```

No frontend:

```bash
cd frontend
npm ci
npm run build
```

Os testes de backend atuais são principalmente **unitários**, sem necessidade de banco. Consulte [a matriz de testes](docs/05-testes-e-limitacoes.md) para o que está coberto e o que continua manual.

## Demonstração sugerida

1. Crie um departamento (ex.: `TI`).
2. Cadastre um fornecedor fictício e um centro de custo ligado ao departamento.
3. Crie uma solicitação com mais de um item e confira o valor calculado pelo backend.
4. Envie a solicitação para aprovação.
5. Aprove ou rejeite com justificativa e confira o histórico.
6. Confira os indicadores no dashboard.

Regra central: somente solicitações `PENDENTE` podem ser aprovadas ou rejeitadas. Cadastros inativos não podem ser usados na criação ou envio de novas solicitações. **O nome do responsável é digitado pelo operador e não constitui autenticação.**

## Documentação

- [01 — Escopo e stakeholders](docs/01-escopo.md)
- [02 — Requisitos e regras de negócio](docs/02-requisitos-e-regras.md)
- [03 — Modelo de dados](docs/03-modelo-de-dados.md)
- [04 — Arquitetura e fluxos](docs/04-arquitetura-e-fluxos.md)
- [05 — Testes, segurança e limitações](docs/05-testes-e-limitacoes.md)
- [06 — Apresentação técnica para portfólio](docs/06-apresentacao-portifolio.md)

## Cuidados importantes

- Não execute `docker compose down -v` em uma instância com dados que deseja preservar; esse comando **remove o volume**.
- Não altere migrations V1–V3 que já tenham sido executadas. Use `V4__...sql` para futuras mudanças de banco.
- O projeto não possui permissões de usuário, trilha de identidade verificada, controle de orçamento, pedidos de compra formais ou integração com sistemas externos.
- Para produção seriam necessários autenticação, autorização por perfil, usuário de banco com privilégios mínimos, certificados TLS válidos, testes de integração, gestão de segredos, logging/auditoria de segurança, backup e recuperação, observabilidade e revisão de regras de negócio.
- Não publique dados reais de clientes ou empregadores em exemplos, screenshots ou histórico de commits.

Projeto demonstrativo para fins educacionais e de portfólio. Nenhuma licença de uso de terceiros é concedida implicitamente por este repositório.
