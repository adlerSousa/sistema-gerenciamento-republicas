package br.ufes.republicas.application.morador;

import br.ufes.republicas.domain.morador.Morador;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Dados de um morador devolvidos pelos casos de uso do modulo.
 */
public record MoradorSaida(
        Long id,
        String nome,
        String apelido,
        String cpf,
        String telefone,
        String linkRedeSocial,
        String telefoneResponsavelUm,
        String telefoneResponsavelDois,
        boolean perfilPublico,
        Long republicaId,
        LocalDate dataIngresso,
        BigDecimal percentualRateio,
        boolean semTeto) {

    public static MoradorSaida de(Morador morador) {
        return new MoradorSaida(
                morador.getId(),
                morador.getNome(),
                morador.getApelido(),
                morador.getCpf(),
                morador.getTelefone(),
                morador.getLinkRedeSocial(),
                morador.getTelefoneResponsavelUm(),
                morador.getTelefoneResponsavelDois(),
                morador.isPerfilPublico(),
                morador.getRepublicaId(),
                morador.getDataIngresso(),
                morador.getPercentualRateio(),
                morador.estaSemTeto());
    }
}
