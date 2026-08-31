package br.ufes.republicas.application.financeiro;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.financeiro.LancamentoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Consulta as informacoes de um lancamento (UC4).
 */
@Service
public class ConsultarLancamentoCasoDeUso {

    private final LancamentoRepositorio lancamentoRepositorio;

    public ConsultarLancamentoCasoDeUso(LancamentoRepositorio lancamentoRepositorio) {
        this.lancamentoRepositorio = lancamentoRepositorio;
    }

    @Transactional(readOnly = true)
    public LancamentoSaida executar(Long lancamentoId) {
        return lancamentoRepositorio.buscarPorId(lancamentoId)
                .map(LancamentoSaida::de)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Lancamento", lancamentoId));
    }
}
