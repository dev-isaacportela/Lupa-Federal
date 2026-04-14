# Como usar

## Pré-requisitos

- Docker e Docker Compose
- Python 3.11+
- Java 17+ e Maven
- Chave de API do Portal da Transparência ([obter aqui](https://portaldatransparencia.gov.br/api-de-dados/cadastrar-email))

## Setup local

**1. Configure o `.env`:**
```bash
cp .env.example .env
```

Preencha:
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

**3. Inicie o data-ingestion:**
```bash
cd services/data-ingestion
pip install -r requirements.txt
```

Crie `services/data-ingestion/.env`:
```env
PORTAL_API_KEY=sua_chave_aqui
CORE_API_URL=http://localhost:8080
```

```bash
python main.py
```

**4. Inicie o frontend:**
```bash
cd web
npm install
npm run dev
```

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

| Campo | Descrição | Padrão |
|---|---|---|
| `gestao` | Código de gestão do órgão (`00001` = Tesouro Nacional) | obrigatório |
| `fase` | `1` = Empenho, `2` = Liquidação, `3` = Pagamento | `3` |
| `max_paginas` | Limite de páginas por dia (100 itens/página) | `10` |

## Endpoints

### data-ingestion (`localhost:8000`)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/health` | Health check |
| POST | `/ingest/despesas` | Ingestão por dia |
| POST | `/ingest/despesas/mes` | Ingestão por mês |
| POST | `/ingest/despesas/ano` | Ingestão por ano |

Documentação interativa: `http://localhost:8000/docs`

### core-api (`localhost:8080`)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/v1/orgaos` | Listar órgãos |
| GET | `/api/v1/fornecedores` | Listar fornecedores |
| GET | `/api/v1/categorias-despesas` | Listar categorias |
| GET | `/api/v1/agentes` | Listar agentes |
| GET | `/api/v1/despesas` | Listar despesas com filtros |
| POST | `/api/v1/ingest/despesas` | Receber lote do data-ingestion |

Documentação interativa: `http://localhost:8080/swagger-ui.html`

## Deploy (produção)

| Serviço | Plataforma | Variáveis necessárias |
|---|---|---|
| `core-api` | Railway | `POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` |
| `data-ingestion` | Railway | `CORE_API_URL`, `PORTAL_API_KEY` |
| `web` | Vercel | `VITE_API_URL` |

Veja o guia completo em [arquitetura.md](./arquitetura.md).
