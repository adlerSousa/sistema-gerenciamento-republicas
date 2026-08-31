package br.ufes.republicas.application.financeiro;

import br.ufes.republicas.domain.financeiro.FormaRateio;
import br.ufes.republicas.domain.financeiro.Lancamento;
import br.ufes.republicas.domain.financeiro.Periodicidade;
import br.ufes.republicas.domain.financeiro.StatusLancamento;
import br.ufes.republicas.domain.financeiro.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Dados de um lancamento devolvidos pelos casos de uso do modulo financeiro.
 */
public record LancamentoSaida(
        Long id,
        Long republicaId,
        TipoLancamento tipo,
        String descricao,
        BigDecimal valor,
        LocalDate dataVencimento,
        LocalDate dataCadastro,
        Periodicidade periodicidade,
        FormaRateio formaRateio,
        Integer numeroParcela,
        Integer totalParcelas,
        StatusLancamento status,
        List<ParticipacaoSaida> participacoes) {

    public static LancamentoSaida de(Lancamento lancamento) {
        return new LancamentoSaida(
                lancamento.getId(),
                lancamento.getRepublicaId(),
                lancamento.getTipo(),
                lancamento.getDescricao(),
                lancamento.getValor(),
                lancamento.getDataVencimento(),
                lancamento.getDataCadastro(),
                lancamento.getPeriodicidade(),
                lancamento.getFormaRateio(),
                lancamento.getNumeroParcela(),
                lancamento.getTotalParcelas(),
                lancamento.getStatus(),
                lancamento.getParticipacoes().stream().map(ParticipacaoSaida::de).toList());
    }
}
