-- Esquema base de republicas, moradores e representacao.
-- Referencias: BR01 (Republica), BR02 (Morador), BR03 (Representante),
-- BR04 ("Sem teto") e BR07 (Vagas).

CREATE TABLE republica (
    id                            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome                          VARCHAR(150)   NOT NULL,
    data_fundacao                 DATE           NOT NULL,
    data_extincao                 DATE,
    logradouro                    VARCHAR(200)   NOT NULL,
    cep                           VARCHAR(9)     NOT NULL,
    bairro                        VARCHAR(100)   NOT NULL,
    ponto_referencia              VARCHAR(200)   NOT NULL,
    localizacao_geografica        VARCHAR(100),
    codigo_etica                  TEXT,
    vantagens                     TEXT           NOT NULL,
    despesas_medias_por_morador   NUMERIC(12, 2) NOT NULL,
    total_vagas                   INTEGER        NOT NULL,
    vagas_ocupadas                INTEGER        NOT NULL DEFAULT 0,

    CONSTRAINT ck_republica_total_vagas_nao_negativo
        CHECK (total_vagas >= 0),
    CONSTRAINT ck_republica_vagas_ocupadas_nao_negativo
        CHECK (vagas_ocupadas >= 0),
    CONSTRAINT ck_republica_despesas_medias_nao_negativas
        CHECK (despesas_medias_por_morador >= 0)
);

COMMENT ON TABLE republica IS
    'Moradia dividida por estudantes (BR01).';
COMMENT ON COLUMN republica.vagas_ocupadas IS
    'Numero de vagas ocupadas (BR07). As vagas disponiveis sao obtidas por total_vagas - vagas_ocupadas.';

CREATE TABLE morador (
    id                        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome                      VARCHAR(150) NOT NULL,
    apelido                   VARCHAR(80)  NOT NULL,
    cpf                       VARCHAR(11)  NOT NULL,
    telefone                  VARCHAR(20)  NOT NULL,
    link_rede_social          VARCHAR(255),
    telefone_responsavel_um   VARCHAR(20)  NOT NULL,
    telefone_responsavel_dois VARCHAR(20)  NOT NULL,
    perfil_publico            BOOLEAN      NOT NULL DEFAULT TRUE,
    republica_id              BIGINT,
    data_ingresso             DATE,
    percentual_rateio         NUMERIC(5, 2),

    CONSTRAINT uk_morador_cpf UNIQUE (cpf),
    CONSTRAINT fk_morador_republica
        FOREIGN KEY (republica_id) REFERENCES republica (id),
    CONSTRAINT ck_morador_percentual_rateio_valido
        CHECK (percentual_rateio IS NULL
               OR (percentual_rateio >= 0 AND percentual_rateio <= 100))
);

COMMENT ON TABLE morador IS
    'Pessoa cadastrada no sistema (BR02). Quando republica_id e nulo, o morador '
    'encontra-se na condicao de "sem teto" (BR04).';
COMMENT ON COLUMN morador.republica_id IS
    'Republica em que o morador reside atualmente. Um morador nao pode residir '
    'em mais de uma republica ao mesmo tempo (BR02).';

CREATE INDEX ix_morador_republica ON morador (republica_id);

CREATE TABLE representante (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    republica_id BIGINT NOT NULL,
    morador_id   BIGINT NOT NULL,
    data_inicio  DATE   NOT NULL,
    data_fim     DATE,

    CONSTRAINT fk_representante_republica
        FOREIGN KEY (republica_id) REFERENCES republica (id),
    CONSTRAINT fk_representante_morador
        FOREIGN KEY (morador_id) REFERENCES morador (id),
    CONSTRAINT ck_representante_periodo_valido
        CHECK (data_fim IS NULL OR data_fim >= data_inicio)
);

COMMENT ON TABLE representante IS
    'Morador responsavel pela republica durante um periodo determinado (BR03).';

CREATE INDEX ix_representante_republica ON representante (republica_id);
CREATE INDEX ix_representante_morador ON representante (morador_id);

CREATE TABLE usuario (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_usuario  VARCHAR(60)  NOT NULL,
    senha         VARCHAR(255) NOT NULL,
    morador_id    BIGINT       NOT NULL,

    CONSTRAINT uk_usuario_nome_usuario UNIQUE (nome_usuario),
    CONSTRAINT uk_usuario_morador UNIQUE (morador_id),
    CONSTRAINT fk_usuario_morador
        FOREIGN KEY (morador_id) REFERENCES morador (id)
);

COMMENT ON TABLE usuario IS
    'Credenciais de acesso ao sistema (RF03).';
