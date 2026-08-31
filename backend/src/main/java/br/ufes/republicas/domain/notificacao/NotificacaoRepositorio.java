package br.ufes.republicas.domain.notificacao;

import java.util.List;
import java.util.Optional;

/**
 * Porta de acesso ao repositorio de notificacoes.
 */
public interface NotificacaoRepositorio {

    Notificacao salvar(Notificacao notificacao);

    Optional<Notificacao> buscarPorId(Long id);

    List<Notificacao> listarPorMorador(Long moradorId);

    List<Notificacao> listarNaoLidasPorMorador(Long moradorId);

    boolean existeParaMoradorELancamento(Long moradorId, Long lancamentoId);
}
