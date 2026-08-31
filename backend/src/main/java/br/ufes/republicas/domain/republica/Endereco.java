package br.ufes.republicas.domain.republica;

import java.util.Objects;

/**
 * Endereco de uma republica (BR01).
 *
 * <p>A localizacao geografica e opcional; os demais dados sao obrigatorios.
 */
public record Endereco(
        String logradouro,
        String cep,
        String bairro,
        String pontoReferencia,
        String localizacaoGeografica) {

    public Endereco {
        Objects.requireNonNull(logradouro, "O logradouro e obrigatorio.");
        Objects.requireNonNull(cep, "O CEP e obrigatorio.");
        Objects.requireNonNull(bairro, "O bairro e obrigatorio.");
        Objects.requireNonNull(pontoReferencia, "O ponto de referencia e obrigatorio.");
    }
}
