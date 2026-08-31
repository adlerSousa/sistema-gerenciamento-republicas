package br.ufes.republicas.application.notificacao;

import br.ufes.republicas.domain.notificacao.Notificacao;
import br.ufes.republicas.domain.notificacao.TipoNotificacao;

import java.time.LocalDate;

/**
 * Dados de uma notificacao devolvidos pelos casos de uso do modulo.
 */
public record NotificacaoSaida(
        Long id,
        Long moradorId,
        Long republicaId,
        TipoNotificacao tipo,
        String mensagem,
        boolean lida,
        LocalDate dataCriacao,
        Long lancamentoId) {

    public static NotificacaoSaida de(Notificacao notificacao) {
        return new NotificacaoSaida(
                notificacao.getId(),
                notificacao.getMoradorId(),
                notificacao.getRepublicaId(),
                notificacao.getTipo(),
                notificacao.getMensagem(),
                notificacao.isLida(),
                notificacao.getDataCriacao(),
                notificacao.getLancamentoId());
    }
}
