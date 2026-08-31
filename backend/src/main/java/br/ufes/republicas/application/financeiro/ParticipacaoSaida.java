package br.ufes.republicas.application.financeiro;

import br.ufes.republicas.domain.financeiro.ParticipacaoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Participacao de um morador no rateio de um lancamento.
 */
public record ParticipacaoSaida(
        Long id,
        Long moradorId,
        BigDecimal percentual,
        BigDecimal valorFixo,
        BigDecimal valorDevido,
        boolean pago,
        LocalDate dataPagamento) {

    public static ParticipacaoSaida de(ParticipacaoLancamento participacao) {
        return new ParticipacaoSaida(
                participacao.getId(),
                participacao.getMoradorId(),
                participacao.getPercentual(),
                participacao.getValorFixo(),
                participacao.getValorDevido(),
                participacao.isPago(),
                participacao.getDataPagamento());
    }
}
