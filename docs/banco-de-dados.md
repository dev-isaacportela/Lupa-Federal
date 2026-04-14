# Banco de Dados

## Modelo de dados

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

## Descrição das entidades

**orgaos** — órgãos do governo federal (ex: Ministério da Saúde).
Identificado pelo `codigo_siafi`, código oficial no sistema SIAFI do Tesouro Nacional.

**agentes_politicos** — unidades gestoras que executam os gastos. Cada agente pertence a um órgão.
Representa a Unidade Gestora (UG) responsável pelo documento de despesa.

**fornecedores** — pessoas jurídicas ou físicas que receberam os pagamentos.
`tipo_pessoa`: `J` = CNPJ (pessoa jurídica), `F` = CPF (pessoa física).

**categorias_despesa** — classificação orçamentária do gasto (elemento de despesa).
Exemplos: `Outros Serviços de Terceiros - Pessoa Jurídica`, `Diárias - Civil`.

**despesas** — registro central de cada pagamento.
`id_despesa_api` é gerado via `SHA-256(numero_documento + cnpj_fornecedor)`, garantindo idempotência na reingestão a mesma despesa nunca é duplicada.

## Migrações

Gerenciadas pelo Flyway em `services/core-api/src/main/resources/db/migration/`.
O Hibernate tem `ddl-auto=none` — não toca no schema.

| Versão | Descrição                                                                                                |
| ------- | ---------------------------------------------------------------------------------------------------------- |
| V1      | Schema inicial                                                                                             |
| V2      | Adição de IDs externos (CREATE TABLE IF NOT EXISTS — ineficaz)                                          |
| V3      | Renomeação de colunas (CREATE TABLE IF NOT EXISTS — ineficaz)                                           |
| V4      | Correção via ALTER TABLE — adiciona colunas faltantes e renomeia `id_portal_api` → `id_agente_api` |

## Resetar o banco

```bash
docker exec -it lupa-postgres psql -U lupa -d lupa_federal \
  -c "SET search_path TO lupa; TRUNCATE despesas, agentes_politicos, fornecedores, categorias_despesa, orgaos RESTART IDENTITY CASCADE;"
```
