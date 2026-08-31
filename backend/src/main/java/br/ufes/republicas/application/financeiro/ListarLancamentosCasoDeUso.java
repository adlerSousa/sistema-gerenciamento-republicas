package br.ufes.republicas.application.financeiro;

import br.ufes.republicas.domain.financeiro.LancamentoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Consulta os lancamentos de receita e despesa (UC4 e UC10).
 */
@Service
public class ListarLancamentosCasoDeUso {

    private final LancamentoRepositorio lancamentoRepositorio;

    public ListarLancamentosCasoDeUso(LancamentoRepositorio lancamentoRepositorio) {
        this.lancamentoRepositorio = lancamentoRepositorio;
    }

    @Transactional(readOnly = true)
    public List<LancamentoSaida> porRepublica(Long republicaId) {
        return lancamentoRepositorio.listarPorRepublica(republicaId).stream()
                .map(LancamentoSaida::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LancamentoSaida> porRepublicaEPeriodo(Long republicaId, LocalDate inicio, LocalDate fim) {
        return lancamentoRepositorio.listarPorRepublicaEPeriodo(republicaId, inicio, fim).stream()
                .map(LancamentoSaida::de)
                .toList();
    }

    /**
     * Lista as receitas e despesas das quais o morador participa (UC10).
     */
    @Transactional(readOnly = true)
    public List<LancamentoSaida> porMorador(Long moradorId) {
        return lancamentoRepositorio.listarPorMoradorParticipante(moradorId).stream()
                .map(LancamentoSaida::de)
                .toList();
    }
}
