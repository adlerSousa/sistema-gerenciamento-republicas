package br.ufes.republicas.domain.financeiro;

/**
 * Periodicidade de um lancamento financeiro (BR06).
 *
 * <p>Lancamentos semanais e mensais sao repetitivos e dao origem a lancamentos
 * derivados numerados.
 */
public enum Periodicidade {
    UNICA,
    SEMANAL,
    MENSAL;

    public boolean ehRepetitiva() {
        return this != UNICA;
    }
}
