-- Esquema de convites, solicitacoes de moradia e notificacoes.
-- Referencias: RF01 (Notificar vencimento proximo), RF02 (Notificacao de
-- solicitacao), UC5, UC14 e UC17.

CREATE TABLE solicitacao_moradia (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    republica_id  BIGINT      NOT NULL,
    morador_id    BIGINT      NOT NULL,
    origem        VARCHAR(20) NOT NULL,
    status        VARCHAR(20) NOT NULL,
    data_registro DATE        NOT NULL,
    data_resposta DATE,

    CONSTRAINT fk_solicitacao_republica
        FOREIGN KEY (republica_id) REFERENCES republica (id),
    CONSTRAINT fk_solicitacao_morador
        FOREIGN KEY (morador_id) REFERENCES morador (id),
    CONSTRAINT ck_solicitacao_origem
        CHECK (origem IN ('CONVITE', 'SOLICITACAO')),
    CONSTRAINT ck_solicitacao_status
        CHECK (status IN ('PENDENTE', 'ACEITA', 'RECUSADA'))
);

COMMENT ON TABLE solicitacao_moradia IS
    'Convite enviado pelo representante ou solicitacao enviada por um morador '
    'sem teto para ingressar em uma republica (UC5, UC14, UC17).';
COMMENT ON COLUMN solicitacao_moradia.origem IS
    'CONVITE quando parte do representante da republica; SOLICITACAO quando '
    'parte do proprio morador.';

CREATE INDEX ix_solicitacao_republica ON solicitacao_moradia (republica_id);
CREATE INDEX ix_solicitacao_morador ON solicitacao_moradia (morador_id);

CREATE TABLE notificacao (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    morador_id     BIGINT       NOT NULL,
    republica_id   BIGINT,
    tipo           VARCHAR(40)  NOT NULL,
    mensagem       VARCHAR(500) NOT NULL,
    lida           BOOLEAN      NOT NULL DEFAULT FALSE,
    data_criacao   DATE         NOT NULL,
    lancamento_id  BIGINT,

    CONSTRAINT fk_notificacao_morador
        FOREIGN KEY (morador_id) REFERENCES morador (id),
    CONSTRAINT fk_notificacao_republica
        FOREIGN KEY (republica_id) REFERENCES republica (id),
    CONSTRAINT fk_notificacao_lancamento
        FOREIGN KEY (lancamento_id) REFERENCES lancamento (id),
    CONSTRAINT ck_notificacao_tipo
        CHECK (tipo IN ('VENCIMENTO_PROXIMO', 'SOLICITACAO_MORADIA', 'CONVITE_RECEBIDO'))
);

COMMENT ON TABLE notificacao IS
    'Aviso apresentado ao morador no acesso ao sistema (RF01, RF02).';
COMMENT ON COLUMN notificacao.tipo IS
    'VENCIMENTO_PROXIMO corresponde ao aviso de lancamentos a vencer, emitido '
    'conforme o prazo definido em BR01.1.';

CREATE INDEX ix_notificacao_morador ON notificacao (morador_id);
