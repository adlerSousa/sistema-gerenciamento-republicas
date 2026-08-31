package br.ufes.republicas.presentation.financeiro;

import jakarta.validation.constraints.NotNull;

/**
 * Corpo da requisicao de registro de pagamento da parcela de um morador (UC9).
 */
public record RegistrarPagamentoRequisicao(
        @NotNull(message = "o morador e obrigatorio")
        Long moradorId) {
}
