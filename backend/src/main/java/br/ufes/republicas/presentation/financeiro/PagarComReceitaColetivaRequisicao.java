package br.ufes.republicas.presentation.financeiro;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Corpo da requisicao de pagamento de despesa com o saldo da receita coletiva
 * (BR06).
 */
public record PagarComReceitaColetivaRequisicao(
        @NotNull(message = "o valor utilizado e obrigatorio")
        @Positive(message = "o valor utilizado deve ser positivo")
        BigDecimal valorUtilizado) {
}
