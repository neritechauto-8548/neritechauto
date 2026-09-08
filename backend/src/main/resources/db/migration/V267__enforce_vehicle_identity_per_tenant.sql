-- D5 vehicle identity hardening.
--
-- This migration deliberately refuses to choose a winner when legacy rows
-- collide after canonical normalization. Production duplicates must be
-- remediated explicitly with business/audit context before this migration is
-- retried; no vehicle/history is deleted or merged here.

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
          FROM veiculos
         WHERE placa IS NULL
            OR regexp_replace(btrim(placa), '[^A-Za-z0-9]', '', 'g') = ''
    ) THEN
        RAISE EXCEPTION 'V267 blocked: veiculos contains a null/blank plate that cannot be normalized safely';
    END IF;

    IF EXISTS (
        SELECT 1
          FROM (
                SELECT empresa_id,
                       upper(regexp_replace(btrim(placa), '[^A-Za-z0-9]', '', 'g')) AS placa_normalizada
                  FROM veiculos
                 GROUP BY empresa_id,
                          upper(regexp_replace(btrim(placa), '[^A-Za-z0-9]', '', 'g'))
                HAVING count(*) > 1
          ) duplicadas
    ) THEN
        RAISE EXCEPTION 'V267 blocked: duplicate normalized vehicle plates exist inside the same tenant; remediate explicitly before retrying';
    END IF;

    IF EXISTS (
        SELECT 1
          FROM (
                SELECT empresa_id,
                       upper(regexp_replace(btrim(chassi), '\s+', '', 'g')) AS chassi_normalizado
                  FROM veiculos
                 WHERE chassi IS NOT NULL
                   AND btrim(chassi) <> ''
                 GROUP BY empresa_id,
                          upper(regexp_replace(btrim(chassi), '\s+', '', 'g'))
                HAVING count(*) > 1
          ) duplicados
    ) THEN
        RAISE EXCEPTION 'V267 blocked: duplicate normalized vehicle chassis/VIN values exist inside the same tenant; remediate explicitly before retrying';
    END IF;
END $$;

-- Canonicalize only after the preflight proves the transformation will not
-- collapse two records in the same tenant.
UPDATE veiculos
   SET placa = upper(regexp_replace(btrim(placa), '[^A-Za-z0-9]', '', 'g'))
 WHERE placa <> upper(regexp_replace(btrim(placa), '[^A-Za-z0-9]', '', 'g'));

UPDATE veiculos
   SET chassi = NULLIF(upper(regexp_replace(btrim(chassi), '\s+', '', 'g')), '')
 WHERE chassi IS NOT NULL
   AND chassi IS DISTINCT FROM NULLIF(upper(regexp_replace(btrim(chassi), '\s+', '', 'g')), '');

-- Database constraints are the final authority under concurrent requests.
-- Multiple NULL chassis remain valid, while every non-null chassis is unique
-- within its tenant. The same plate/chassis may still exist in another tenant.
CREATE UNIQUE INDEX IF NOT EXISTS ux_d5_veiculos_empresa_placa
    ON veiculos (empresa_id, placa);

CREATE UNIQUE INDEX IF NOT EXISTS ux_d5_veiculos_empresa_chassi
    ON veiculos (empresa_id, chassi)
    WHERE chassi IS NOT NULL;
