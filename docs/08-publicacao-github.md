# Publicação segura no GitHub — SGAC

Este checklist pressupõe que a versão local está funcionando, os testes Maven e o build Vite já foram executados, e o repositório Git contém um commit da Etapa 7.

## 1. Conferir arquivos sensíveis e estado do Git

No Git Bash, a partir de `SGAC-erp`:

```bash
git status --short
git ls-files .env
git ls-files | grep -E '(^|/)(node_modules|dist|target)/' || true
git remote -v
```

- `git ls-files .env` deve vir **vazio**;
- as pastas `node_modules`, `dist` e `target` não devem estar rastreadas;
- verifique se imagens e exemplos exibem **somente dados fictícios**;
- examine o histórico se suspeitar que credenciais já tenham sido incluídas: excluir do commit atual **não apaga commits antigos**. Se houve exposição, é necessário **trocar a senha** e limpar o histórico antes de abrir o repositório.

**Não publique** `.env`, backups `.bak`, banco SQL, relatórios contendo segredos ou dados reais de pessoas/organizações. Não rode `docker compose down -v`: isso apaga o volume local do banco.

## 2. Revisar o material novo

Verifique se os arquivos desta atualização foram incluídos:

```text
README.md
docs/07-estudo-de-caso.md
docs/08-publicacao-github.md
docs/09-texto-portfolio.md
docs/screenshots/dashboard.webp
docs/screenshots/solicitacoes.webp
docs/screenshots/aprovacoes.webp
docs/screenshots/centros-de-custo.webp
```

Confira imagens diretamente antes de publicá-las. Por serem screenshots de um ambiente local, imagens e números são **ilustrativos**, não indicadores reais de uma organização.

## 3. Registrar a atualização

```bash
git status --short
git add README.md docs/07-estudo-de-caso.md docs/08-publicacao-github.md docs/09-texto-portfolio.md docs/screenshots/
git diff --cached --stat
git commit -m "docs: add SGAC case study and portfolio screenshots"
```

## 4. Criar repositório remoto (se ainda não existe)

No GitHub, crie um repositório (por exemplo, `sgac-erp`). Deixe **sem README inicial** se vai subir um repositório local existente.

Se `git remote -v` já exibir `origin`, não adicione outro: confira o endereço antes de enviar.

```bash
# Somente se ainda não houver origin:
git remote add origin https://github.com/SEU_USUARIO/sgac-erp.git

git branch -M main
git push -u origin main
```

Substitua `SEU_USUARIO` pelo seu usuário real. Revise o repositório **antes de torná-lo público**; você pode iniciar como privado e depois alterar sua visibilidade.

## 5. Verificações após o push

- README renderizado corretamente, sem imagens quebradas;
- links para cada documento em `docs/` abrindo;
- screenshots legíveis em desktop;
- nenhuma credencial no repositório, inclusive em commits antigos;
- aba **Actions** com workflow executado (a existência de CI não significa que já passou);
- campo **About** com descrição breve e tópicos, por exemplo: `react`, `typescript`, `spring-boot`, `java`, `sql-server`, `erp`, `rest-api`, `flyway`, `portfolio`.

## 6. Portfólio pessoal

Monte uma seção de projeto com título, uma frase de contexto, stack, quatro capacidades e duas chamadas: **Código no GitHub** e **Estudo de caso**. Use [texto pronto](09-texto-portfolio.md) como ponto de partida.

Se optar por uma demonstração online mais adiante, será necessário planejar hospedagem do SQL Server/backend e **implementar segurança antes de abrir endpoints administrativos na internet**. Por ora, **repositório e screenshots são suficientes** para análise de portfólio.
