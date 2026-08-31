package br.ufes.republicas.presentation.morador;

import br.ufes.republicas.domain.morador.OrigemSolicitacao;
import jakarta.validation.constraints.NotNull;

/**
 * Corpo da requisicao de envio de convite ou de solicitacao de moradia.
 */
public record EnviarSolicitacaoRequisicao(
        @NotNull(message = "a republica e obrigatoria")
        Long republicaId,

        @NotNull(message = "o morador e obrigatorio")
        Long moradorId,

        @NotNull(message = "a origem da solicitacao e obrigatoria")
        OrigemSolicitacao origem) {
}
