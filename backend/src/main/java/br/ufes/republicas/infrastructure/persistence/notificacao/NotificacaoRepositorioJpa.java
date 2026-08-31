package br.ufes.republicas.infrastructure.persistence.notificacao;

import br.ufes.republicas.domain.notificacao.Notificacao;
import br.ufes.republicas.domain.notificacao.NotificacaoRepositorio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementacao da porta de repositorio de notificacoes sobre JPA.
 */
@Repository
public class NotificacaoRepositorioJpa implements NotificacaoRepositorio {

    private final NotificacaoJpaRepository repositorio;

    public NotificacaoRepositorioJpa(NotificacaoJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Notificacao salvar(Notificacao notificacao) {
        NotificacaoEntity entidade = notificacao.getId() == null
                ? new NotificacaoEntity()
                : repositorio.findById(notificacao.getId()).orElseGet(NotificacaoEntity::new);

        entidade.setId(notificacao.getId());
        entidade.setMoradorId(notificacao.getMoradorId());
        entidade.setRepublicaId(notificacao.getRepublicaId());
        entidade.setTipo(notificacao.getTipo());
        entidade.setMensagem(notificacao.getMensagem());
        entidade.setLida(notificacao.isLida());
        entidade.setDataCriacao(notificacao.getDataCriacao());
        entidade.setLancamentoId(notificacao.getLancamentoId());

        return paraDominio(repositorio.save(entidade));
    }

    @Override
    public Optional<Notificacao> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    public List<Notificacao> listarPorMorador(Long moradorId) {
        return repositorio.findByMoradorIdOrderByDataCriacaoDesc(moradorId).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<Notificacao> listarNaoLidasPorMorador(Long moradorId) {
        return repositorio.findByMoradorIdAndLidaFalseOrderByDataCriacaoDesc(moradorId).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public boolean existeParaMoradorELancamento(Long moradorId, Long lancamentoId) {
        return repositorio.existsByMoradorIdAndLancamentoId(moradorId, lancamentoId);
    }

    private Notificacao paraDominio(NotificacaoEntity entidade) {
        Notificacao notificacao = new Notificacao(
                entidade.getMoradorId(),
                entidade.getRepublicaId(),
                entidade.getTipo(),
                entidade.getMensagem(),
                entidade.getDataCriacao());
        notificacao.definirId(entidade.getId());
        notificacao.definirLida(entidade.isLida());
        notificacao.vincularLancamento(entidade.getLancamentoId());
        return notificacao;
    }
}
