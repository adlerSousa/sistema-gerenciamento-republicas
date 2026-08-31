package br.ufes.republicas.infrastructure.persistence.reclamacao;

import br.ufes.republicas.domain.reclamacao.ReclamacaoSugestao;
import br.ufes.republicas.domain.reclamacao.ReclamacaoSugestaoRepositorio;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

/**
 * Implementacao da porta de repositorio de reclamacoes e sugestoes sobre JPA.
 */
@Repository
public class ReclamacaoSugestaoRepositorioJpa implements ReclamacaoSugestaoRepositorio {

    private final ReclamacaoSugestaoJpaRepository repositorio;

    public ReclamacaoSugestaoRepositorioJpa(ReclamacaoSugestaoJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public ReclamacaoSugestao salvar(ReclamacaoSugestao reclamacaoSugestao) {
        ReclamacaoSugestaoEntity entidade = reclamacaoSugestao.getId() == null
                ? new ReclamacaoSugestaoEntity()
                : repositorio.findById(reclamacaoSugestao.getId())
                        .orElseGet(ReclamacaoSugestaoEntity::new);

        entidade.setId(reclamacaoSugestao.getId());
        entidade.setRepublicaId(reclamacaoSugestao.getRepublicaId());
        entidade.setAutorId(reclamacaoSugestao.getAutorId());
        entidade.setTipo(reclamacaoSugestao.getTipo());
        entidade.setDescricao(reclamacaoSugestao.getDescricao());
        entidade.setDataRegistro(reclamacaoSugestao.getDataRegistro());
        entidade.setDataSolucao(reclamacaoSugestao.getDataSolucao());
        entidade.setResolvida(reclamacaoSugestao.isResolvida());
        entidade.setSolucaoConfirmada(reclamacaoSugestao.isSolucaoConfirmada());
        entidade.setExcluida(reclamacaoSugestao.isExcluida());
        entidade.setEnvolvidos(new LinkedHashSet<>(reclamacaoSugestao.getEnvolvidos()));

        return paraDominio(repositorio.save(entidade));
    }

    @Override
    public Optional<ReclamacaoSugestao> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    public List<ReclamacaoSugestao> listarPorRepublica(Long republicaId) {
        return repositorio.findByRepublicaIdOrderByDataRegistroDesc(republicaId).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<ReclamacaoSugestao> listarPorAutor(Long autorId) {
        return repositorio.findByAutorIdOrderByDataRegistroDesc(autorId).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<ReclamacaoSugestao> listarPorEnvolvidoEPeriodo(Long moradorId,
                                                               LocalDate inicio,
                                                               LocalDate fim) {
        return repositorio.buscarPorEnvolvidoEPeriodo(moradorId, inicio, fim).stream()
                .map(this::paraDominio)
                .toList();
    }

    private ReclamacaoSugestao paraDominio(ReclamacaoSugestaoEntity entidade) {
        ReclamacaoSugestao reclamacao = new ReclamacaoSugestao(
                entidade.getRepublicaId(),
                entidade.getAutorId(),
                entidade.getTipo(),
                entidade.getDescricao(),
                entidade.getDataRegistro(),
                List.copyOf(entidade.getEnvolvidos()));

        reclamacao.definirId(entidade.getId());
        reclamacao.restaurarSituacao(
                entidade.isResolvida(),
                entidade.isSolucaoConfirmada(),
                entidade.isExcluida(),
                entidade.getDataSolucao());
        return reclamacao;
    }
}
