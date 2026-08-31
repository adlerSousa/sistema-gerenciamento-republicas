package br.ufes.republicas.presentation.republica;

import br.ufes.republicas.application.republica.CadastrarRepublicaEntrada;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Corpo da requisicao de fundacao de uma republica.
 */
public record CadastrarRepublicaRequisicao(
        @NotBlank(message = "o nome e obrigatorio")
        String nome,

        @NotNull(message = "a data de fundacao e obrigatoria")
        LocalDate dataFundacao,

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
        int totalVagas,

        @PositiveOrZero(message = "as vagas ocupadas nao podem ser negativas")
        int vagasOcupadas,

        @NotNull(message = "o morador fundador e obrigatorio")
        Long moradorFundadorId) {

    public CadastrarRepublicaEntrada paraEntrada() {
        return new CadastrarRepublicaEntrada(
                nome, dataFundacao, logradouro, cep, bairro, pontoReferencia,
                localizacaoGeografica, codigoEtica, vantagens,
                despesasMediasPorMorador, totalVagas, vagasOcupadas, moradorFundadorId);
    }
}
