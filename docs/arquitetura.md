# Arquitetura

## Visão geral

```
Portal da Transparência
        │
        ▼
┌─────────────────────┐
│   data-ingestion    │  Python · FastAPI
│  (coleta e transforma)│
└──────────┬──────────┘
           │ POST /api/v1/ingest/despesas
           ▼
┌─────────────────────┐
│      core-api       │  Java · Spring Boot
│  (regras + persistência)│
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│     PostgreSQL      │  schema: lupa
└─────────────────────┘
           │
           ▼
┌─────────────────────┐
│        web          │  React · Vite
└─────────────────────┘
```

## Serviços

| Serviço | Tecnologia | Responsabilidade |
|---|---|---|
| `data-ingestion` | Python · FastAPI | Coleta e transforma dados do Portal da Transparência |
| `core-api` | Java · Spring Boot | Regras de negócio, persistência e endpoints REST |
| `web` | React · Vite | Dashboard com filtros e visualizações |
| `postgres` | PostgreSQL 16 | Persistência no schema `lupa` |

## Estrutura do repositório

```
lupa-federal/
├── docs/                        # Documentação
├── infrastructure/
│   └── docker-compose.yml
├── services/
│   ├── core-api/                # Java · Spring Boot
│   │   └── src/main/resources/
│   │       └── db/migration/    # Flyway migrations
│   └── data-ingestion/          # Python · FastAPI
│       └── app/
│           ├── adapters/        # HTTP clients (Portal + core-api)
│           ├── api/             # Rotas FastAPI
│           ├── core/            # Config, logging, exceptions
│           ├── domain/          # Use cases e models
│           └── ports/           # Interfaces (contratos)
└── web/                         # React · Vite
```

## Padrões adotados

**data-ingestion** — Clean Architecture (Ports & Adapters). A lógica de negócio (use cases) não depende de frameworks ou bibliotecas externas. Adapters HTTP implementam as interfaces definidas nos ports.

**core-api** — Arquitetura em camadas (Controller → Service → Repository). Migrações de schema gerenciadas pelo Flyway, sem DDL automático do Hibernate.

## Deploy

| Serviço | Plataforma |
|---|---|
| `core-api` + `data-ingestion` + PostgreSQL | Railway |
| `web` | Vercel |
