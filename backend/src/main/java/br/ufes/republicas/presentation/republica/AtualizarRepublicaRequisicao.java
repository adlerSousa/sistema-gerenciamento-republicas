package br.ufes.republicas.presentation.republica;

import br.ufes.republicas.application.republica.AtualizarRepublicaEntrada;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * Corpo da requisicao de alteracao dos dados de uma republica.
 */
public record AtualizarRepublicaRequisicao(
        @NotBlank(message = "o nome e obrigatorio")
        String nome,

        @NotBlank(message = "o logradouro e obrigatorio")
        String logradouro,

        @NotBlank(message = "o CEP e obrigatorio")
        String cep,

        @NotBlank(message = "o bairro e obrigatorio")
        String bairro,

        @NotBlank(message = "o ponto de referencia e obrigatorio")
        String pontoReferencia,

        String localizacaoGeografica,

        String codigoEtica,

        @NotBlank(message = "as vantagens sao obrigatorias")
        String vantagens,

        @NotNull(message = "as despesas medias por morador sao obrigatorias")
        @PositiveOrZero(message = "as despesas medias nao podem ser negativas")
        BigDecimal despesasMediasPorMorador,

        @PositiveOrZero(message = "o total de vagas nao pode ser negativo")
        int totalVagas) {

    public AtualizarRepublicaEntrada paraEntrada() {
        return new AtualizarRepublicaEntrada(
                nome, logradouro, cep, bairro, pontoReferencia, localizacaoGeografica,
                codigoEtica, vantagens, despesasMediasPorMorador, totalVagas);
    }
}
