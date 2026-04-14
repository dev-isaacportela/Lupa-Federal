# Análise de Requisitos

## Problema

Dados de gastos públicos federais são públicos, mas dispersos e difíceis de interpretar. O Portal da Transparência expõe uma API rica, porém sem visualizações analíticas — é necessário consultar manualmente por órgão, período e fase de execução.

## Objetivo

Automatizar a coleta, normalização e apresentação desses dados em um painel que permita responder perguntas como:

- Quais fornecedores receberam mais recursos em um determinado período?
- Como os gastos de um órgão evoluíram ao longo do ano?
- Quais categorias de despesa concentram mais pagamentos?

## Requisitos funcionais

| ID | Requisito |
|---|---|
| RF01 | Coletar despesas do Portal da Transparência por dia, mês ou ano |
| RF02 | Normalizar e persistir os dados (órgão, fornecedor, categoria, agente, despesa) |
| RF03 | Evitar duplicação de registros em reingestões |
| RF04 | Expor despesas com filtros por período, órgão e fornecedor |
| RF05 | Apresentar agregações (top fornecedores, evolução temporal) no dashboard |

## Requisitos não funcionais

| ID | Requisito |
|---|---|
| RNF01 | Ingestão tolerante a falhas: erro em um dia não interrompe o processamento do mês |
| RNF02 | IDs de despesa determinísticos: reingerir a mesma despesa não gera duplicata |
| RNF03 | Stack executável localmente com um único comando (`docker compose up`) |
| RNF04 | Separação clara entre coleta de dados (Python) e regras de negócio (Java) |
