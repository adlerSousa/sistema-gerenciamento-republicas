-- Esquema financeiro: lancamentos de receita e despesa, rateio entre moradores
-- e receita coletiva.
-- Referencias: BR05 (Receita coletiva) e BR06 (Receitas e despesas).

CREATE TABLE lancamento (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    republica_id          BIGINT         NOT NULL,
    tipo                  VARCHAR(20)    NOT NULL,
    descricao             VARCHAR(200)   NOT NULL,
    valor                 NUMERIC(12, 2) NOT NULL,
    data_vencimento       DATE           NOT NULL,
    data_cadastro         DATE           NOT NULL,
    periodicidade         VARCHAR(20)    NOT NULL,
    forma_rateio          VARCHAR(20)    NOT NULL,
    numero_parcela        INTEGER,
    total_parcelas        INTEGER,
    lancamento_origem_id  BIGINT,
    status                VARCHAR(20)    NOT NULL,
    justificativa_estorno TEXT,

    CONSTRAINT fk_lancamento_republica
        FOREIGN KEY (republica_id) REFERENCES republica (id),
    CONSTRAINT fk_lancamento_origem
        FOREIGN KEY (lancamento_origem_id) REFERENCES lancamento (id),
    CONSTRAINT ck_lancamento_tipo
        CHECK (tipo IN ('RECEITA', 'DESPESA')),
    CONSTRAINT ck_lancamento_periodicidade
        CHECK (periodicidade IN ('UNICA', 'SEMANAL', 'MENSAL')),
    CONSTRAINT ck_lancamento_forma_rateio
        CHECK (forma_rateio IN ('PERCENTUAL', 'VALOR_FIXO')),
    CONSTRAINT ck_lancamento_status
        CHECK (status IN ('PENDENTE', 'PAGO', 'ESTORNADO')),
    CONSTRAINT ck_lancamento_valor_positivo
        CHECK (valor > 0),
    CONSTRAINT ck_lancamento_parcela_valida
        CHECK (
            (numero_parcela IS NULL AND total_parcelas IS NULL)
            OR (numero_parcela >= 1 AND total_parcelas >= 1
                AND numero_parcela <= total_parcelas)
        )
);

COMMENT ON TABLE lancamento IS
    'Lancamento de receita ou despesa da republica (BR06). Lancamentos '
    'periodicos geram lancamentos derivados numerados, vinculados ao lancamento '
    'de origem por lancamento_origem_id.';
COMMENT ON COLUMN lancamento.forma_rateio IS
    'Define se o rateio entre os moradores participantes e feito por percentual '
    'ou por valor fixo em reais (BR06).';

CREATE INDEX ix_lancamento_republica ON lancamento (republica_id);
CREATE INDEX ix_lancamento_data_vencimento ON lancamento (data_vencimento);
CREATE INDEX ix_lancamento_origem ON lancamento (lancamento_origem_id);

CREATE TABLE participacao_lancamento (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lancamento_id   BIGINT         NOT NULL,
    morador_id      BIGINT         NOT NULL,
    percentual      NUMERIC(5, 2),
    valor_fixo      NUMERIC(12, 2),
    valor_devido    NUMERIC(12, 2) NOT NULL,
    pago            BOOLEAN        NOT NULL DEFAULT FALSE,
    data_pagamento  DATE,

    CONSTRAINT fk_participacao_lancamento
        FOREIGN KEY (lancamento_id) REFERENCES lancamento (id),
    CONSTRAINT fk_participacao_morador
        FOREIGN KEY (morador_id) REFERENCES morador (id),
    CONSTRAINT uk_participacao_lancamento_morador
        UNIQUE (lancamento_id, morador_id),
    CONSTRAINT ck_participacao_percentual_valido
        CHECK (percentual IS NULL OR (percentual >= 0 AND percentual <= 100)),
    CONSTRAINT ck_participacao_valor_devido_nao_negativo
        CHECK (valor_devido >= 0)
);

COMMENT ON TABLE participacao_lancamento IS
    'Participacao de cada morador no rateio de um lancamento (BR06). O valor '
    'devido corresponde a parcela do morador, calculada por percentual ou por '
    'valor fixo.';

CREATE INDEX ix_participacao_lancamento ON participacao_lancamento (lancamento_id);
CREATE INDEX ix_participacao_morador ON participacao_lancamento (morador_id);

CREATE TABLE receita_coletiva (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    republica_id BIGINT         NOT NULL,
    saldo        NUMERIC(12, 2) NOT NULL DEFAULT 0,

    CONSTRAINT uk_receita_coletiva_republica UNIQUE (republica_id),
    CONSTRAINT fk_receita_coletiva_republica
        FOREIGN KEY (republica_id) REFERENCES republica (id),
    CONSTRAINT ck_receita_coletiva_saldo_nao_negativo
        CHECK (saldo >= 0)
);

COMMENT ON TABLE receita_coletiva IS
    'Saldo acumulado de receitas coletivas da republica, proveniente de doacoes '
    'e de eventos (BR05). O saldo pode ser utilizado para pagar lancamentos de '
    'despesa, sendo debitado no momento do uso (BR06).';

CREATE TABLE movimentacao_receita_coletiva (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    receita_coletiva_id BIGINT         NOT NULL,
    lancamento_id       BIGINT,
    tipo                VARCHAR(20)    NOT NULL,
    valor               NUMERIC(12, 2) NOT NULL,
    data_movimentacao   DATE           NOT NULL,
    descricao           VARCHAR(200)   NOT NULL,

    CONSTRAINT fk_movimentacao_receita_coletiva
        FOREIGN KEY (receita_coletiva_id) REFERENCES receita_coletiva (id),
    CONSTRAINT fk_movimentacao_lancamento
        FOREIGN KEY (lancamento_id) REFERENCES lancamento (id),
    CONSTRAINT ck_movimentacao_tipo
        CHECK (tipo IN ('CREDITO', 'DEBITO')),
    CONSTRAINT ck_movimentacao_valor_positivo
        CHECK (valor > 0)
);

COMMENT ON TABLE movimentacao_receita_coletiva IS
    'Historico de creditos e debitos do saldo da receita coletiva (BR05, BR06).';

CREATE INDEX ix_movimentacao_receita_coletiva
    ON movimentacao_receita_coletiva (receita_coletiva_id);
