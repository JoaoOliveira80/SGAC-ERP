# SGAC — Sistema Integrado de Gestão de Aquisições e Suprimentos

Projeto demonstrativo em **Java 21 + Spring Boot 4.1.1 + SQL Server 2022 + Flyway + Swagger UI**, reconstruído até a etapa de **cadastros administrativos**.

## Situação dos módulos

| Módulo | Estado |
| --- | --- |
| SQL Server no Docker | Compose configurado |
| Spring Boot e `/api/health` | Implementado |
| Departamentos (GET, GET/id, POST, PUT, PATCH/status) | Implementado |
| Fornecedores (GET, GET/id, POST, PUT, PATCH/status) | Implementado |
| Centros de custo (GET, GET/id, POST, PUT, PATCH/status) | Implementado |
| Migrações V1 e V2 | Incluídas |
| Solicitações, aprovações, frontend | **Ainda não implementados** |

## Restaurar no Windows (Git Bash)

**IMPORTANTE:** Não execute `docker compose down -v`: isso apaga o volume persistente do banco.

1. Extraia o ZIP. A raiz do projeto precisa conter `compose.yaml`, `.env.example` e `backend/`.
2. Coloque na raiz um arquivo `.env` **com sua senha original**. Se o seu ZIP de resgate `SGAC-erp.zip` ainda estiver disponível, ele possui o `.env` anterior. Não publique esse arquivo.
   - Em um projeto realmente novo, copie `.env.example` para `.env` e crie sua senha forte antes da primeira inicialização.
   - Alterar `.env` **não redefine** a senha de um SQL Server já existente.
3. Abra a raiz do projeto e execute:

```bash
docker compose up -d
# Verificar quais bancos existem, sem apagar dados:
docker compose exec -T sqlserver bash -c '/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -Q "SELECT name FROM sys.databases"'
# SOMENTE se SGAC_DB nao aparecer na lista, crie uma vez:
docker compose exec -T sqlserver bash -c '/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -Q "CREATE DATABASE SGAC_DB"'
cd backend
set -a
source ../.env
set +a
./mvnw clean test
./mvnw spring-boot:run
```

4. Confirme: `http://localhost:8080/api/health` e `http://localhost:8080/swagger-ui/index.html`.
5. Primeiro crie um departamento ativo, depois um centro de custo com `departamentoId` do departamento.

### Exemplos de payloads

`POST /api/departamentos`
```json
{"codigo":"TI","nome":"Tecnologia da Informacao"}
```

`POST /api/fornecedores`
```json
{"cnpj":"11222333000181","razaoSocial":"Fornecedor Demonstrativo","email":"contato@exemplo.com"}
```

`POST /api/centros-custo`
```json
{"codigo":"CC-TI","nome":"Tecnologia e Sistemas","departamentoId":1}
```

## Atenção sobre migrações Flyway

As migrações foram reconstituídas conforme o código discutido até esta etapa. Se o volume antigo ainda contiver migrações e ocorrer erro de `checksum mismatch`, **não apague o banco e não execute `flyway repair` automaticamente**. Compare o arquivo de migração original com o reconstruído e avalie a alteração antes de seguir.

## Restrições de segurança e escopo

- O arquivo `.env` não está neste ZIP para evitar expor credenciais.
- SQL Server `sa` e `trustServerCertificate=true` são **apenas para desenvolvimento local**.
- O backend ainda não tem autenticação/autorização; não exponha a API publicamente.
- A verificação de CNPJ atual cobre somente formato, não dígitos verificadores.
- Não existem integrações oficiais com TOTVS RM neste projeto.

## Estrutura

```text
SGAC-erp/
  compose.yaml
  .env.example
  .gitignore
  README.md
  backend/
    pom.xml
    mvnw
    mvnw.cmd
    .mvn/wrapper/
    src/main/java/br/com/sgac/
      BackendApplication.java
      api/HealthController.java
      departamentos/
      fornecedores/
      centroscusto/
    src/main/resources/
      application.yml
      db/migration/V1__criar_tabela_departamentos.sql
      db/migration/V2__fornecedores_centros_custo.sql
  docs/
    01-escopo.md
    02-modelo-dados.md
```

Documentação técnica adicional em `docs/`.
