package br.ufes.republicas.application.republica;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Dados necessarios para fundar uma republica (UC1).
 */
public record CadastrarRepublicaEntrada(
        String nome,
        LocalDate dataFundacao,
        String logradouro,
        String cep,
        String bairro,
        String pontoReferencia,
        String localizacaoGeografica,
        String codigoEtica,
        String vantagens,
        BigDecimal despesasMediasPorMorador,
        int totalVagas,
        int vagasOcupadas,
        Long moradorFundadorId) {
}
