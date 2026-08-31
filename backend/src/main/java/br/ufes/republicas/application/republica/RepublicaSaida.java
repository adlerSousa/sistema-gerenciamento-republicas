package br.ufes.republicas.application.republica;

import br.ufes.republicas.domain.republica.Republica;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Dados de uma republica devolvidos pelos casos de uso do modulo.
 */
public record RepublicaSaida(
        Long id,
        String nome,
        LocalDate dataFundacao,
        LocalDate dataExtincao,
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
        int vagasDisponiveis) {

    public static RepublicaSaida de(Republica republica) {
        return new RepublicaSaida(
                republica.getId(),
                republica.getNome(),
                republica.getDataFundacao(),
                republica.getDataExtincao(),
                republica.getEndereco().logradouro(),
                republica.getEndereco().cep(),
                republica.getEndereco().bairro(),
                republica.getEndereco().pontoReferencia(),
                republica.getEndereco().localizacaoGeografica(),
                republica.getCodigoEtica(),
                republica.getVantagens(),
                republica.getDespesasMediasPorMorador(),
                republica.getTotalVagas(),
                republica.getVagasOcupadas(),
                republica.vagasDisponiveis());
    }
}
