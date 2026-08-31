package br.ufes.republicas.domain.financeiro;

/**
 * Forma de divisao de um lancamento entre os moradores participantes (BR06).
 *
 * <p>O rateio deve ser feito por percentual para cada morador ou por valor fixo
 * em reais.
 */
public enum FormaRateio {
    PERCENTUAL,
    VALOR_FIXO
}
