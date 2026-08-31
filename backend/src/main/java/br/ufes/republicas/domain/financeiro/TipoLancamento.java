package br.ufes.republicas.domain.financeiro;

/**
 * Natureza de um lancamento financeiro (BR06).
 *
 * <p>Um lancamento de despesa e um debito, um valor devido que deve ser pago.
 * Um lancamento de receita e um valor que a republica recebeu.
 */
public enum TipoLancamento {
    RECEITA,
    DESPESA
}
