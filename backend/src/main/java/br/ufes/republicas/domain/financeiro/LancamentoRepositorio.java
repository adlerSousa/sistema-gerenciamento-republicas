package br.ufes.republicas.domain.financeiro;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Porta de acesso ao repositorio de lancamentos financeiros.
 */
public interface LancamentoRepositorio {

    Lancamento salvar(Lancamento lancamento);

    Optional<Lancamento> buscarPorId(Long id);

    List<Lancamento> listarPorRepublica(Long republicaId);

    List<Lancamento> listarPorMoradorParticipante(Long moradorId);

    List<Lancamento> listarPorRepublicaEPeriodo(Long republicaId, LocalDate inicio, LocalDate fim);

    List<Lancamento> listarPendentesComVencimentoAte(Long republicaId, LocalDate limite);
}
