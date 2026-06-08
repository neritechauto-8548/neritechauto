-- Alter check constraint to allow 'AB' (Aberta) question type
ALTER TABLE it_questionario DROP CONSTRAINT IF EXISTS it_questionario_tp_itquestionario_check;
ALTER TABLE it_questionario ADD CONSTRAINT it_questionario_tp_itquestionario_check CHECK (tp_itquestionario IN ('SN', 'AV', 'AB'));
