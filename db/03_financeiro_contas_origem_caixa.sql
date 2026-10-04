ALTER TABLE conta
    ADD COLUMN status VARCHAR(20) NULL AFTER observacao,
    ADD COLUMN recorrente BOOLEAN NOT NULL DEFAULT FALSE AFTER status;

UPDATE conta
SET status = CASE
    WHEN dtpagamento IS NOT NULL THEN 'BAIXADA'
    ELSE 'PENDENTE'
END
WHERE codigo > 0
  AND status IS NULL;

ALTER TABLE conta
    MODIFY COLUMN status VARCHAR(20) NOT NULL;

ALTER TABLE caixa_historico
    ADD COLUMN origem VARCHAR(30) NOT NULL DEFAULT 'MANUAL' AFTER tipo,
    ADD COLUMN origem_codigo INT NULL AFTER origem;

UPDATE caixa_historico
SET origem = 'PROCEDIMENTO'
WHERE codigo > 0
  AND UPPER(TRIM(descricao)) = 'FINALIZAÇÃO DE PROCEDIMENTO';

CREATE INDEX idx_conta_vencimento
    ON conta (dtvencimento);

CREATE INDEX idx_conta_tipo_status
    ON conta (tipo, status);

CREATE INDEX idx_caixa_historico_origem
    ON caixa_historico (origem, origem_codigo);

CREATE UNIQUE INDEX uk_caixa_historico_origem_codigo
    ON caixa_historico (origem, origem_codigo);
