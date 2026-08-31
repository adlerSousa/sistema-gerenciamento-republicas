-- Esquema de tarefas domesticas, reclamacoes e sugestoes.
-- Referencias: BR08 (Tarefas) e BR09 (Reclamacoes/Sugestoes).

CREATE TABLE tarefa (
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    republica_id         BIGINT       NOT NULL,
    descricao            VARCHAR(500) NOT NULL,
    data_agendamento     DATE         NOT NULL,
    data_termino         DATE         NOT NULL,
    finalizada           BOOLEAN      NOT NULL DEFAULT FALSE,
    data_conclusao       DATE,
    descricao_conclusao  VARCHAR(500),

    CONSTRAINT fk_tarefa_republica
        FOREIGN KEY (republica_id) REFERENCES republica (id),
    CONSTRAINT ck_tarefa_periodo_valido
        CHECK (data_termino >= data_agendamento)
);

COMMENT ON TABLE tarefa IS
    'Tarefa domestica atribuida a um ou mais moradores da republica (BR08).';
COMMENT ON COLUMN tarefa.data_conclusao IS
    'Data em que a tarefa foi efetivamente concluida. Uma tarefa concluida ate a '
    'data_termino e considerada realizada no prazo estipulado (BR02.2.2).';

CREATE INDEX ix_tarefa_republica ON tarefa (republica_id);

CREATE TABLE tarefa_responsavel (
    tarefa_id  BIGINT NOT NULL,
    morador_id BIGINT NOT NULL,

    CONSTRAINT pk_tarefa_responsavel PRIMARY KEY (tarefa_id, morador_id),
    CONSTRAINT fk_tarefa_responsavel_tarefa
        FOREIGN KEY (tarefa_id) REFERENCES tarefa (id),
    CONSTRAINT fk_tarefa_responsavel_morador
        FOREIGN KEY (morador_id) REFERENCES morador (id)
);

COMMENT ON TABLE tarefa_responsavel IS
    'Moradores responsaveis por uma tarefa (BR08).';

CREATE TABLE reclamacao_sugestao (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    republica_id        BIGINT       NOT NULL,
    autor_id            BIGINT       NOT NULL,
    tipo                VARCHAR(20)  NOT NULL,
    descricao           VARCHAR(1000) NOT NULL,
    data_registro       DATE         NOT NULL,
    data_solucao        DATE,
    resolvida           BOOLEAN      NOT NULL DEFAULT FALSE,
    solucao_confirmada  BOOLEAN      NOT NULL DEFAULT FALSE,
    excluida            BOOLEAN      NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_reclamacao_republica
        FOREIGN KEY (republica_id) REFERENCES republica (id),
    CONSTRAINT fk_reclamacao_autor
        FOREIGN KEY (autor_id) REFERENCES morador (id),
    CONSTRAINT ck_reclamacao_tipo
        CHECK (tipo IN ('RECLAMACAO', 'SUGESTAO'))
);

COMMENT ON TABLE reclamacao_sugestao IS
    'Reclamacao ou sugestao registrada por um morador (BR09). A idade em dias e '
    'obtida pela diferenca entre data_solucao e data_registro.';
COMMENT ON COLUMN reclamacao_sugestao.excluida IS
    'Marcacao de exclusao logica realizada pelo representante (BR09).';

CREATE INDEX ix_reclamacao_republica ON reclamacao_sugestao (republica_id);
CREATE INDEX ix_reclamacao_autor ON reclamacao_sugestao (autor_id);

CREATE TABLE reclamacao_envolvido (
    reclamacao_id BIGINT NOT NULL,
    morador_id    BIGINT NOT NULL,

    CONSTRAINT pk_reclamacao_envolvido PRIMARY KEY (reclamacao_id, morador_id),
    CONSTRAINT fk_reclamacao_envolvido_reclamacao
        FOREIGN KEY (reclamacao_id) REFERENCES reclamacao_sugestao (id),
    CONSTRAINT fk_reclamacao_envolvido_morador
        FOREIGN KEY (morador_id) REFERENCES morador (id)
);

COMMENT ON TABLE reclamacao_envolvido IS
    'Moradores envolvidos em uma reclamacao ou sugestao (BR09). Uma reclamacao '
    'resolvida por um morador envolvido conta para o seu indice de solucao de '
    'reclamacoes (BR02.2.1).';
