package br.ufes.republicas.application.republica;

import java.math.BigDecimal;

/**
 * Dados alteraveis de uma republica ja cadastrada (UC1).
 */
public record AtualizarRepublicaEntrada(
        String nome,
        String logradouro,
        String cep,
        String bairro,
        String pontoReferencia,
        String localizacaoGeografica,
        String codigoEtica,
        String vantagens,
        BigDecimal despesasMediasPorMorador,
        int totalVagas) {
}
