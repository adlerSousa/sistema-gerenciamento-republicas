package br.ufes.republicas.infrastructure.persistence.financeiro;

import br.ufes.republicas.domain.financeiro.Lancamento;
import br.ufes.republicas.domain.financeiro.LancamentoRepositorio;
import br.ufes.republicas.domain.financeiro.StatusLancamento;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementacao da porta de repositorio de lancamentos sobre JPA.
 */
@Repository
public class LancamentoRepositorioJpa implements LancamentoRepositorio {

    private final LancamentoJpaRepository repositorio;
    private final LancamentoMapper mapeador;

    public LancamentoRepositorioJpa(LancamentoJpaRepository repositorio, LancamentoMapper mapeador) {
        this.repositorio = repositorio;
        this.mapeador = mapeador;
    }

    @Override
    public Lancamento salvar(Lancamento lancamento) {
        LancamentoEntity entidade = lancamento.getId() == null
                ? mapeador.paraEntidade(lancamento)
                : atualizarExistente(lancamento);
        return mapeador.paraDominio(repositorio.save(entidade));
    }

    private LancamentoEntity atualizarExistente(Lancamento lancamento) {
        LancamentoEntity entidade = repositorio.findById(lancamento.getId())
                .orElseGet(LancamentoEntity::new);
        mapeador.aplicar(lancamento, entidade);
        return entidade;
    }

    @Override
    public Optional<Lancamento> buscarPorId(Long id) {
        return repositorio.findById(id).map(mapeador::paraDominio);
    }

    @Override
    public List<Lancamento> listarPorRepublica(Long republicaId) {
        return repositorio.findByRepublicaIdOrderByDataVencimentoAsc(republicaId).stream()
                .map(mapeador::paraDominio)
                .toList();
    }

    @Override
    public List<Lancamento> listarPorMoradorParticipante(Long moradorId) {
        return repositorio.buscarPorMoradorParticipante(moradorId).stream()
                .map(mapeador::paraDominio)
                .toList();
    }

    @Override
    public List<Lancamento> listarPorRepublicaEPeriodo(Long republicaId, LocalDate inicio, LocalDate fim) {
        return repositorio
                .findByRepublicaIdAndDataVencimentoBetweenOrderByDataVencimentoAsc(republicaId, inicio, fim)
                .stream()
                .map(mapeador::paraDominio)
                .toList();
    }

    @Override
    public List<Lancamento> listarPendentesComVencimentoAte(Long republicaId, LocalDate limite) {
        return repositorio
                .findByRepublicaIdAndStatusAndDataVencimentoLessThanEqualOrderByDataVencimentoAsc(
                        republicaId, StatusLancamento.PENDENTE, limite)
                .stream()
                .map(mapeador::paraDominio)
                .toList();
    }
}
