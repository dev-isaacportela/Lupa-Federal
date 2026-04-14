# Lupa Federal

Painel de transparência de gastos públicos federais. Consome dados do [Portal da Transparência](https://portaldatransparencia.gov.br/) e os apresenta em um dashboard com filtros e visualizações.

## Arquitetura

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
│        web          │  Dashboard
└─────────────────────┘
```

## Pré-requisitos

- Docker e Docker Compose
- Python 3.11+
- Java 17+ e Maven
- Chave de API do Portal da Transparência ([obter aqui](https://portaldatransparencia.gov.br/api-de-dados/cadastrar-email))

## Configuração

**1. Clone e configure o `.env`:**

```bash
cp .env.example .env
```

Edite o `.env` e preencha:

```env
POSTGRES_DB=lupa_federal
POSTGRES_USER=lupa
POSTGRES_PASSWORD=lupa
PORTAL_API_KEY=sua_chave_aqui
```

**2. Suba o banco e o core-api:**

```bash
docker compose -f infrastructure/docker-compose.yml up -d
```

**3. Verifique se está tudo de pé:**

```bash
docker compose -f infrastructure/docker-compose.yml ps
curl http://localhost:8080/api/v1/orgaos
```

**4. Configure e inicie o data-ingestion:**

```bash
cd services/data-ingestion
pip install -r requirements.txt
```

Crie um `.env` dentro de `services/data-ingestion/`:

```env
PORTAL_API_KEY=sua_chave_aqui
CORE_API_URL=http://localhost:8080
```

```bash
python main.py
```

O serviço sobe em `http://localhost:8000`.

## Ingestão de dados

### Por dia

```bash
curl -X POST http://localhost:8000/ingest/despesas \
  -H "Content-Type: application/json" \
  -d '{"data_emissao": "10/01/2024", "gestao": "00001", "fase": 3}'
```

### Por mês

```bash
curl -X POST http://localhost:8000/ingest/despesas/mes \
  -H "Content-Type: application/json" \
  -d '{"ano": 2024, "mes": 1, "gestao": "00001", "fase": 3}'
```

### Por ano

```bash
curl -X POST http://localhost:8000/ingest/despesas/ano \
  -H "Content-Type: application/json" \
  -d '{"ano": 2024, "gestao": "00001", "fase": 3}'
```

**Parâmetros:**

| Campo           | Descrição                                                     | Padrão      |
| --------------- | --------------------------------------------------------------- | ------------ |
| `gestao`      | Código de gestão do órgão (ex:`00001` = Tesouro Nacional) | obrigatório |
| `fase`        | `1` = Empenho, `2` = Liquidação, `3` = Pagamento        | `3`        |
| `max_paginas` | Limite de páginas por dia (100 itens/página)                  | `10`       |

## Endpoints

### data-ingestion (`localhost:8000`)

| Método | Rota                     | Descrição        |
| ------- | ------------------------ | ------------------ |
| GET     | `/health`              | Health check       |
| POST    | `/ingest/despesas`     | Ingestão por dia  |
| POST    | `/ingest/despesas/mes` | Ingestão por mês |
| POST    | `/ingest/despesas/ano` | Ingestão por ano  |

Documentação interativa: `http://localhost:8000/docs`

### core-api (`localhost:8080`)

| Método | Rota                            | Descrição                    |
| ------- | ------------------------------- | ------------------------------ |
| GET     | `/api/v1/orgaos`              | Listar órgãos                |
| GET     | `/api/v1/fornecedores`        | Listar fornecedores            |
| GET     | `/api/v1/categorias-despesas` | Listar categorias              |
| GET     | `/api/v1/agentes`             | Listar agentes                 |
| GET     | `/api/v1/despesas`            | Listar despesas com filtros    |
| POST    | `/api/v1/ingest/despesas`     | Receber lote do data-ingestion |

Documentação interativa: `http://localhost:8080/swagger-ui.html`

## Estrutura do repositório

```
lupa-federal/
├── infrastructure/
│   └── docker-compose.yml
├── services/
│   ├── core-api/               # Java · Spring Boot
│   │   └── src/main/resources/
│   │       └── db/migration/   # Flyway migrations
│   └── data-ingestion/         # Python · FastAPI
│       └── app/
│           ├── adapters/       # HTTP clients (Portal + core-api)
│           ├── api/            # Rotas FastAPI
│           ├── core/           # Config, logging, exceptions
│           ├── domain/         # Use cases e models
│           └── ports/          # Interfaces (contratos)
└── web/                        # Dashboard
```

## Análise de Requisitos

### Problema

Dados de gastos públicos federais são públicos, mas dispersos e difíceis de interpretar. O Portal da Transparência expõe uma API rica, porém sem visualizações analíticas é necessário consultar manualmente por órgão, período e fase de execução.

### Objetivo

Automatizar a coleta, normalização e apresentação desses dados em um painel que permita responder perguntas como:

- Quais fornecedores receberam mais recursos em um determinado período?
- Como os gastos de um órgão evoluíram ao longo do ano?
- Quais categorias de despesa concentram mais pagamentos?

### Requisitos funcionais

| ID   | Requisito                                                                         |
| ---- | --------------------------------------------------------------------------------- |
| RF01 | Coletar despesas do Portal da Transparência por dia, mês ou ano                 |
| RF02 | Normalizar e persistir os dados (órgão, fornecedor, categoria, agente, despesa) |
| RF03 | Evitar duplicação de registros em reingestões                                  |
| RF04 | Expor despesas com filtros por período, órgão e fornecedor                     |
| RF05 | Apresentar agregações (top fornecedores, evolução temporal) no dashboard      |

### Requisitos não funcionais

| ID    | Requisito                                                                            |
| ----- | ------------------------------------------------------------------------------------ |
| RNF01 | Ingestão tolerante a falhas: erro em um dia não interrompe o processamento do mês |
| RNF02 | IDs de despesa determinísticos: reingerir a mesma despesa não gera duplicata       |
| RNF03 | Stack executável localmente com um único comando (`docker compose up`)           |
| RNF04 | Separação clara entre coleta de dados (Python) e regras de negócio (Java)         |

---

## Modelo de Dados

### Entidades e relacionamentos

```
orgaos
──────────────────────────────
id               SERIAL PK
id_orgao_api     BIGINT UNIQUE    ← código do Portal
codigo_siafi     VARCHAR(10)
nome             VARCHAR(255)
sigla            VARCHAR(10)
        │
        │ 1 ──── N
        ▼
agentes_politicos                     fornecedores
─────────────────────────────         ──────────────────────────────
id               SERIAL PK           id                SERIAL PK
id_agente_api    BIGINT UNIQUE        id_fornecedor_api BIGINT UNIQUE
nome             VARCHAR(255)         cnpj_cpf          VARCHAR(20)
cpf_mascarado    VARCHAR(20)          razao_social      VARCHAR(255)
id_orgao         INTEGER FK           tipo_pessoa       CHAR(1) J/F
        │                                     │
        │                                     │
        └──────────────┬───────────────────────┘
                       │
                       ▼
                    despesas
        ───────────────────────────────────────
        id               SERIAL PK
        id_despesa_api   BIGINT UNIQUE          ← hash(documento + cnpj)
        id_agente        INTEGER FK → agentes_politicos
        id_fornecedor    INTEGER FK → fornecedores
        id_categoria     INTEGER FK → categorias_despesa
        data_emissao     DATE
        valor            DECIMAL(15,2)
        numero_documento VARCHAR(50)
                       ▲
                       │
        categorias_despesa
        ──────────────────────────────
        id                      SERIAL PK
        id_categoria_despesa_api BIGINT UNIQUE
        descricao               VARCHAR(100)
```

### Descrição das entidades

**orgaos** — órgãos do governo federal (ex: Ministério da Saúde).
Identificado pelo `codigo_siafi`, que é o código oficial no sistema SIAFI do Tesouro Nacional.

**agentes_politicos** — unidades gestoras que executam os gastos. Cada agente pertence a um órgão.
No contexto da ingestão, representa a Unidade Gestora (UG) responsável pelo documento de despesa.

**fornecedores** — pessoas jurídicas ou físicas que receberam os pagamentos.
`tipo_pessoa`: `J` = CNPJ (pessoa jurídica), `F` = CPF (pessoa física).

**categorias_despesa** — classificação orçamentária do gasto (elemento de despesa).
Exemplos: `Outros Serviços de Terceiros - Pessoa Jurídica`, `Diárias - Civil`.

**despesas** — registro central de cada pagamento realizado.
`id_despesa_api` é gerado via `SHA-256(numero_documento + cnpj_fornecedor)`, garantindo idempotência na reingestão.

---

## Resetar o banco

Para limpar todos os dados e reiniciar do zero:

```bash
docker exec -it lupa-postgres psql -U lupa -d lupa_federal \
  -c "SET search_path TO lupa; TRUNCATE despesas, agentes_politicos, fornecedores, categorias_despesa, orgaos RESTART IDENTITY CASCADE;"
```
