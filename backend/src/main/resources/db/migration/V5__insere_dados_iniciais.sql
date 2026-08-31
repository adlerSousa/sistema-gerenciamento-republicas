-- Massa de dados inicial do repositorio-base.
--
-- Os identificadores sao fixados explicitamente para que o estado inicial seja
-- deterministico e identico em todas as execucoes do experimento. As sequencias
-- sao reposicionadas ao final para que os novos registros continuem a partir do
-- ultimo identificador utilizado.
--
-- Os lancamentos pendentes usam datas relativas a data corrente, de modo que o
-- comportamento de aviso de vencimento proximo (RF01) permaneca observavel
-- independentemente do dia em que o sistema for executado.

-- ------------------------------------------------------------------ republicas

INSERT INTO republica OVERRIDING SYSTEM VALUE VALUES
(1, 'Republica Vila Velha', DATE '2019-03-10', NULL,
 'Rua das Palmeiras, 120', '29100-000', 'Centro', 'Ao lado da praca central',
 '-20.3297,-40.2925', 'Silencio apos as 22 horas.',
 'Internet de alta velocidade, area de estudos e churrasqueira.',
 450.00, 6, 4),
(2, 'Republica Alto Alegre', DATE '2020-08-01', NULL,
 'Avenida Brasil, 987', '29500-000', 'Sao Silvano', 'Em frente ao mercado municipal',
 '-20.7539,-41.5330', NULL,
 'Garagem coberta, lavanderia compartilhada e proximidade do campus.',
 380.00, 4, 2),
(3, 'Republica Monte Belo', DATE '2021-01-15', NULL,
 'Rua dos Ipes, 45', '29700-000', 'Centro', 'Proximo ao terminal rodoviario',
 NULL, 'Divisao igualitaria das despesas.',
 'Quartos individuais e cozinha ampla.',
 520.00, 5, 5);

-- -------------------------------------------------------------------- moradores

INSERT INTO morador OVERRIDING SYSTEM VALUE VALUES
(1, 'Ana Beatriz Moreira', 'Ana', '11122233344', '27999110001',
 'https://redes.exemplo/anabeatriz', '27999220001', '27999330001', TRUE, 1, DATE '2019-03-10', 25.00),
(2, 'Bruno Carvalho Lima', 'Bruno', '22233344455', '27999110002',
 NULL, '27999220002', '27999330002', TRUE, 1, DATE '2019-06-01', 25.00),
(3, 'Carla Nogueira Dias', 'Carla', '33344455566', '27999110003',
 'https://redes.exemplo/carlan', '27999220003', '27999330003', TRUE, 1, DATE '2020-02-17', 25.00),
(4, 'Diego Fontes Ribeiro', 'Diego', '44455566677', '27999110004',
 NULL, '27999220004', '27999330004', FALSE, 1, DATE '2021-09-05', 25.00),
(5, 'Elisa Prado Martins', 'Elisa', '55566677788', '27999110005',
 NULL, '27999220005', '27999330005', TRUE, 2, DATE '2020-08-01', 50.00),
(6, 'Felipe Andrade Souza', 'Felipe', '66677788899', '27999110006',
 'https://redes.exemplo/felipeas', '27999220006', '27999330006', TRUE, 2, DATE '2022-03-12', 50.00),
(7, 'Gabriela Teixeira Alves', 'Gabi', '77788899900', '27999110007',
 NULL, '27999220007', '27999330007', TRUE, 3, DATE '2021-01-15', 20.00),
(8, 'Henrique Barros Costa', 'Henrique', '88899900011', '27999110008',
 NULL, '27999220008', '27999330008', TRUE, NULL, NULL, NULL),
(9, 'Isabela Rocha Freitas', 'Isa', '99900011122', '27999110009',
 'https://redes.exemplo/isabelarf', '27999220009', '27999330009', TRUE, NULL, NULL, NULL),
(10, 'Joao Pedro Nunes', 'Joao', '10011122233', '27999110010',
 NULL, '27999220010', '27999330010', FALSE, NULL, NULL, NULL);

-- ---------------------------------------------------------------- representantes

INSERT INTO representante OVERRIDING SYSTEM VALUE VALUES
(1, 1, 1, DATE '2019-03-10', NULL),
(2, 2, 5, DATE '2020-08-01', NULL),
(3, 3, 7, DATE '2021-01-15', NULL);

-- --------------------------------------------------------------------- usuarios

INSERT INTO usuario OVERRIDING SYSTEM VALUE VALUES
(1, 'ana',      '$2a$10$5xJmM0dGZ0hVhXqYQyqCue0mQyqCue0mQyqCue0mQyqCue0mQyqCu', 1),
(2, 'bruno',    '$2a$10$5xJmM0dGZ0hVhXqYQyqCue0mQyqCue0mQyqCue0mQyqCue0mQyqCu', 2),
(3, 'carla',    '$2a$10$5xJmM0dGZ0hVhXqYQyqCue0mQyqCue0mQyqCue0mQyqCue0mQyqCu', 3),
(4, 'diego',    '$2a$10$5xJmM0dGZ0hVhXqYQyqCue0mQyqCue0mQyqCue0mQyqCue0mQyqCu', 4),
(5, 'elisa',    '$2a$10$5xJmM0dGZ0hVhXqYQyqCue0mQyqCue0mQyqCue0mQyqCue0mQyqCu', 5),
(6, 'felipe',   '$2a$10$5xJmM0dGZ0hVhXqYQyqCue0mQyqCue0mQyqCue0mQyqCue0mQyqCu', 6),
(7, 'gabriela', '$2a$10$5xJmM0dGZ0hVhXqYQyqCue0mQyqCue0mQyqCue0mQyqCue0mQyqCu', 7),
(8, 'henrique', '$2a$10$5xJmM0dGZ0hVhXqYQyqCue0mQyqCue0mQyqCue0mQyqCue0mQyqCu', 8),
(9, 'isabela',  '$2a$10$5xJmM0dGZ0hVhXqYQyqCue0mQyqCue0mQyqCue0mQyqCue0mQyqCu', 9),
(10, 'joaop',   '$2a$10$5xJmM0dGZ0hVhXqYQyqCue0mQyqCue0mQyqCue0mQyqCue0mQyqCu', 10);

-- ------------------------------------------------------------- receita coletiva

INSERT INTO receita_coletiva OVERRIDING SYSTEM VALUE VALUES
(1, 1, 800.00),
(2, 2, 150.00),
(3, 3, 0.00);

-- ------------------------------------------------------------------ lancamentos

-- Lancamentos historicos ja quitados, base para o indice de compromisso com
-- pagamentos (BR02.2.3).
INSERT INTO lancamento OVERRIDING SYSTEM VALUE VALUES
(1, 1, 'DESPESA', 'Conta de energia eletrica de maio', 320.00,
 DATE '2026-05-10', DATE '2026-05-01', 'UNICA', 'PERCENTUAL', NULL, NULL, NULL, 'PAGO', NULL),
(2, 1, 'DESPESA', 'Conta de agua de maio', 160.00,
 DATE '2026-05-15', DATE '2026-05-01', 'UNICA', 'PERCENTUAL', NULL, NULL, NULL, 'PAGO', NULL),
(3, 1, 'RECEITA', 'Doacao de ex-morador', 500.00,
 DATE '2026-05-20', DATE '2026-05-18', 'UNICA', 'PERCENTUAL', NULL, NULL, NULL, 'PAGO', NULL),
(4, 2, 'DESPESA', 'Internet de maio', 120.00,
 DATE '2026-05-12', DATE '2026-05-02', 'UNICA', 'VALOR_FIXO', NULL, NULL, NULL, 'PAGO', NULL);

-- Lancamentos pendentes, com vencimento proximo a data corrente, usados pelo
-- aviso de vencimento (RF01).
INSERT INTO lancamento OVERRIDING SYSTEM VALUE VALUES
(5, 1, 'DESPESA', 'Conta de energia eletrica do mes corrente', 340.00,
 CURRENT_DATE + 3, CURRENT_DATE - 5, 'MENSAL', 'PERCENTUAL', 1, 3, NULL, 'PENDENTE', NULL),
(6, 1, 'DESPESA', 'Compra da mesa da cozinha 1/3', 210.00,
 CURRENT_DATE + 10, CURRENT_DATE - 2, 'MENSAL', 'PERCENTUAL', 1, 3, NULL, 'PENDENTE', NULL),
(7, 1, 'DESPESA', 'Manutencao do portao', 180.00,
 CURRENT_DATE - 4, CURRENT_DATE - 20, 'UNICA', 'VALOR_FIXO', NULL, NULL, NULL, 'PENDENTE', NULL),
(8, 2, 'DESPESA', 'Gas de cozinha', 130.00,
 CURRENT_DATE + 2, CURRENT_DATE - 3, 'UNICA', 'VALOR_FIXO', NULL, NULL, NULL, 'PENDENTE', NULL);

-- --------------------------------------------------- participacoes no rateio

INSERT INTO participacao_lancamento OVERRIDING SYSTEM VALUE VALUES
-- Lancamento 1, quitado dentro do prazo pela maioria dos moradores.
(1,  1, 1, 25.00, NULL, 80.00, TRUE,  DATE '2026-05-08'),
(2,  1, 2, 25.00, NULL, 80.00, TRUE,  DATE '2026-05-09'),
(3,  1, 3, 25.00, NULL, 80.00, TRUE,  DATE '2026-05-10'),
(4,  1, 4, 25.00, NULL, 80.00, TRUE,  DATE '2026-05-18'),
-- Lancamento 2.
(5,  2, 1, 25.00, NULL, 40.00, TRUE,  DATE '2026-05-13'),
(6,  2, 2, 25.00, NULL, 40.00, TRUE,  DATE '2026-05-15'),
(7,  2, 3, 25.00, NULL, 40.00, TRUE,  DATE '2026-05-14'),
(8,  2, 4, 25.00, NULL, 40.00, TRUE,  DATE '2026-05-22'),
-- Lancamento 3, receita rateada igualmente.
(9,  3, 1, 25.00, NULL, 125.00, TRUE, DATE '2026-05-20'),
(10, 3, 2, 25.00, NULL, 125.00, TRUE, DATE '2026-05-20'),
(11, 3, 3, 25.00, NULL, 125.00, TRUE, DATE '2026-05-20'),
(12, 3, 4, 25.00, NULL, 125.00, TRUE, DATE '2026-05-20'),
-- Lancamento 4, rateio por valor fixo.
(13, 4, 5, NULL, 60.00, 60.00, TRUE, DATE '2026-05-11'),
(14, 4, 6, NULL, 60.00, 60.00, TRUE, DATE '2026-05-12'),
-- Lancamentos pendentes.
(15, 5, 1, 25.00, NULL, 85.00, FALSE, NULL),
(16, 5, 2, 25.00, NULL, 85.00, FALSE, NULL),
(17, 5, 3, 25.00, NULL, 85.00, FALSE, NULL),
(18, 5, 4, 25.00, NULL, 85.00, FALSE, NULL),
(19, 6, 1, 25.00, NULL, 52.50, FALSE, NULL),
(20, 6, 2, 25.00, NULL, 52.50, FALSE, NULL),
(21, 6, 3, 25.00, NULL, 52.50, FALSE, NULL),
(22, 6, 4, 25.00, NULL, 52.50, FALSE, NULL),
(23, 7, 1, NULL, 90.00, 90.00, FALSE, NULL),
(24, 7, 2, NULL, 90.00, 90.00, FALSE, NULL),
(25, 8, 5, NULL, 65.00, 65.00, FALSE, NULL),
(26, 8, 6, NULL, 65.00, 65.00, FALSE, NULL);

-- ------------------------------- movimentacoes do saldo da receita coletiva

INSERT INTO movimentacao_receita_coletiva OVERRIDING SYSTEM VALUE VALUES
(1, 1, 3, 'CREDITO', 500.00, DATE '2026-05-20', 'Doacao de ex-morador'),
(2, 1, NULL, 'CREDITO', 300.00, DATE '2026-06-02', 'Renda da festa junina'),
(3, 2, NULL, 'CREDITO', 150.00, DATE '2026-06-10', 'Rifa beneficente');

-- ---------------------------------------------------------------------- tarefas

-- Tarefas concluidas no prazo e fora do prazo, base para o indice de realizacao
-- de tarefas (BR02.2.2).
INSERT INTO tarefa OVERRIDING SYSTEM VALUE VALUES
(1, 1, 'Limpeza da area comum', DATE '2026-05-02', DATE '2026-05-09', TRUE,
 DATE '2026-05-08', 'Area varrida e organizada.'),
(2, 1, 'Organizacao da despensa', DATE '2026-05-05', DATE '2026-05-12', TRUE,
 DATE '2026-05-15', 'Concluida com atraso por viagem.'),
(3, 1, 'Manutencao do jardim', DATE '2026-05-10', DATE '2026-05-20', TRUE,
 DATE '2026-05-19', 'Grama aparada e plantas regadas.'),
(4, 1, 'Revisao da lista de compras', DATE '2026-06-01', CURRENT_DATE + 7, FALSE,
 NULL, NULL),
(5, 2, 'Limpeza da lavanderia', DATE '2026-05-03', DATE '2026-05-10', TRUE,
 DATE '2026-05-09', 'Lavanderia higienizada.'),
(6, 2, 'Descarte de reciclaveis', DATE '2026-06-05', CURRENT_DATE + 4, FALSE,
 NULL, NULL);

INSERT INTO tarefa_responsavel VALUES
(1, 1), (1, 2),
(2, 3),
(3, 2), (3, 4),
(4, 1),
(5, 5),
(6, 6);

-- --------------------------------------------------- reclamacoes e sugestoes

-- Registros resolvidos e pendentes, base para o indice de solucao de
-- reclamacoes (BR02.2.1).
INSERT INTO reclamacao_sugestao OVERRIDING SYSTEM VALUE VALUES
(1, 1, 2, 'RECLAMACAO', 'Louca acumulada na pia durante a semana.',
 DATE '2026-05-04', DATE '2026-05-07', TRUE, TRUE, FALSE),
(2, 1, 1, 'RECLAMACAO', 'Musica alta apos o horario de silencio.',
 DATE '2026-05-11', DATE '2026-05-16', TRUE, TRUE, FALSE),
(3, 1, 3, 'RECLAMACAO', 'Lixo nao retirado no dia da coleta.',
 DATE '2026-05-18', NULL, FALSE, FALSE, FALSE),
(4, 1, 4, 'SUGESTAO', 'Criar uma escala fixa de limpeza do banheiro.',
 DATE '2026-05-21', NULL, FALSE, FALSE, FALSE),
(5, 2, 6, 'RECLAMACAO', 'Portao deixado aberto durante a noite.',
 DATE '2026-05-14', DATE '2026-05-17', TRUE, FALSE, FALSE);

INSERT INTO reclamacao_envolvido VALUES
(1, 3),
(2, 4),
(3, 2), (3, 4),
(4, 1), (4, 2), (4, 3), (4, 4),
(5, 5);

-- ---------------------------------------------- solicitacoes de moradia

INSERT INTO solicitacao_moradia OVERRIDING SYSTEM VALUE VALUES
(1, 1, 8, 'SOLICITACAO', 'PENDENTE', CURRENT_DATE - 2, NULL),
(2, 2, 9, 'CONVITE', 'PENDENTE', CURRENT_DATE - 1, NULL);

-- ------------------------------------------- reposicionamento das sequencias

SELECT setval(pg_get_serial_sequence('republica', 'id'),
              (SELECT max(id) FROM republica));
SELECT setval(pg_get_serial_sequence('morador', 'id'),
              (SELECT max(id) FROM morador));
SELECT setval(pg_get_serial_sequence('representante', 'id'),
              (SELECT max(id) FROM representante));
SELECT setval(pg_get_serial_sequence('usuario', 'id'),
              (SELECT max(id) FROM usuario));
SELECT setval(pg_get_serial_sequence('receita_coletiva', 'id'),
              (SELECT max(id) FROM receita_coletiva));
SELECT setval(pg_get_serial_sequence('lancamento', 'id'),
              (SELECT max(id) FROM lancamento));
SELECT setval(pg_get_serial_sequence('participacao_lancamento', 'id'),
              (SELECT max(id) FROM participacao_lancamento));
SELECT setval(pg_get_serial_sequence('movimentacao_receita_coletiva', 'id'),
              (SELECT max(id) FROM movimentacao_receita_coletiva));
SELECT setval(pg_get_serial_sequence('tarefa', 'id'),
              (SELECT max(id) FROM tarefa));
SELECT setval(pg_get_serial_sequence('reclamacao_sugestao', 'id'),
              (SELECT max(id) FROM reclamacao_sugestao));
SELECT setval(pg_get_serial_sequence('solicitacao_moradia', 'id'),
              (SELECT max(id) FROM solicitacao_moradia));
