CREATE SCHEMA IF NOT EXISTS lupa;

CREATE TABLE IF NOT EXISTS lupa.__schema_version (
  id integer PRIMARY KEY,
  applied_at timestamptz NOT NULL DEFAULT now(),
  description text NOT NULL
);

INSERT INTO lupa.__schema_version (id, description)
VALUES (1, 'bootstrap')
ON CONFLICT (id) DO NOTHING;

