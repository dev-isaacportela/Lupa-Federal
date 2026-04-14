SET search_path TO lupa;

ALTER TABLE orgaos
    ADD COLUMN IF NOT EXISTS id_orgao_api BIGINT;
UPDATE orgaos SET id_orgao_api = CAST(codigo_siafi AS BIGINT) WHERE id_orgao_api IS NULL;
ALTER TABLE orgaos ALTER COLUMN id_orgao_api SET NOT NULL;
ALTER TABLE orgaos DROP CONSTRAINT IF EXISTS orgaos_id_orgao_api_key;
ALTER TABLE orgaos ADD CONSTRAINT orgaos_id_orgao_api_key UNIQUE (id_orgao_api);

ALTER TABLE categorias_despesa
    ADD COLUMN IF NOT EXISTS id_categoria_despesa_api BIGINT;
UPDATE categorias_despesa SET id_categoria_despesa_api = id WHERE id_categoria_despesa_api IS NULL;
ALTER TABLE categorias_despesa ALTER COLUMN id_categoria_despesa_api SET NOT NULL;
ALTER TABLE categorias_despesa DROP CONSTRAINT IF EXISTS categorias_despesa_id_categoria_despesa_api_key;
ALTER TABLE categorias_despesa ADD CONSTRAINT categorias_despesa_id_categoria_despesa_api_key UNIQUE (id_categoria_despesa_api);

ALTER TABLE fornecedores
    ADD COLUMN IF NOT EXISTS id_fornecedor_api BIGINT;
UPDATE fornecedores SET id_fornecedor_api = id WHERE id_fornecedor_api IS NULL;
ALTER TABLE fornecedores ALTER COLUMN id_fornecedor_api SET NOT NULL;
ALTER TABLE fornecedores DROP CONSTRAINT IF EXISTS fornecedores_id_fornecedor_api_key;
ALTER TABLE fornecedores ADD CONSTRAINT fornecedores_id_fornecedor_api_key UNIQUE (id_fornecedor_api);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = 'lupa' AND table_name = 'agentes_politicos' AND column_name = 'id_portal_api'
    ) THEN
        ALTER TABLE agentes_politicos RENAME COLUMN id_portal_api TO id_agente_api;
    ELSIF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = 'lupa' AND table_name = 'agentes_politicos' AND column_name = 'id_agente_api'
    ) THEN
        ALTER TABLE agentes_politicos ADD COLUMN id_agente_api BIGINT;
    END IF;
END $$;

ALTER TABLE agentes_politicos DROP CONSTRAINT IF EXISTS agentes_politicos_id_agente_api_key;
ALTER TABLE agentes_politicos ADD CONSTRAINT agentes_politicos_id_agente_api_key UNIQUE (id_agente_api);

ALTER TABLE despesas
    ADD COLUMN IF NOT EXISTS id_despesa_api BIGINT;
UPDATE despesas SET id_despesa_api = id WHERE id_despesa_api IS NULL;
ALTER TABLE despesas ALTER COLUMN id_despesa_api SET NOT NULL;
ALTER TABLE despesas DROP CONSTRAINT IF EXISTS despesas_id_despesa_api_key;
ALTER TABLE despesas ADD CONSTRAINT despesas_id_despesa_api_key UNIQUE (id_despesa_api);
