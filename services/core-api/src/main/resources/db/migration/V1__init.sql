CREATE SCHEMA IF NOT EXISTS lupa;
SET search_path TO lupa;

CREATE TABLE IF NOT EXISTS orgaos (
    id SERIAL PRIMARY KEY,
    codigo_siafi VARCHAR(10) UNIQUE NOT NULL,
    nome VARCHAR(255) NOT NULL,
    sigla VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS categorias_despesa (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS fornecedores (
    id SERIAL PRIMARY KEY,
    cnpj_cpf VARCHAR(20) UNIQUE NOT NULL,
    razao_social VARCHAR(255) NOT NULL,
    tipo_pessoa CHAR(1) CHECK (tipo_pessoa IN ('J', 'F'))
);

CREATE TABLE IF NOT EXISTS agentes_politicos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf_mascarado VARCHAR(20),
    id_orgao INTEGER REFERENCES orgaos(id)
);

CREATE TABLE IF NOT EXISTS despesas (
    id SERIAL PRIMARY KEY,
    id_agente INTEGER REFERENCES agentes_politicos(id),
    id_fornecedor INTEGER REFERENCES fornecedores(id),
    id_categoria INTEGER REFERENCES categorias_despesa(id),
    data_emissao DATE NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    numero_documento VARCHAR(50)
);

CREATE INDEX IF NOT EXISTS idx_agentes_politicos_id_orgao ON agentes_politicos(id_orgao);
CREATE INDEX IF NOT EXISTS idx_despesas_id_agente ON despesas(id_agente);
CREATE INDEX IF NOT EXISTS idx_despesas_id_fornecedor ON despesas(id_fornecedor);
CREATE INDEX IF NOT EXISTS idx_despesas_id_categoria ON despesas(id_categoria);
CREATE INDEX IF NOT EXISTS idx_despesas_data_emissao ON despesas(data_emissao);
