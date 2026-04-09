# Lupa Federal (MVP)

Painel para entender gastos públicos federais a partir de dados do Portal da Transparência.

## Infra local (M1) — Postgres via Docker Compose

1) Criar o `.env`:
- PowerShell: `Copy-Item .env.example .env`

2) Subir o banco:
- `docker compose -f infrastructure/docker-compose.yml up -d`

3) Verificar saúde:
- `docker compose -f infrastructure/docker-compose.yml ps`

### Conexão

- Apps rodando na sua máquina: use `POSTGRES_HOST=localhost`
- Apps rodando via Compose: use host `postgres` (nome do service)

### Migrações / inicialização do schema

Por enquanto, a inicialização do schema roda via scripts SQL em `infrastructure/db/init` (montados em `/docker-entrypoint-initdb.d`).
