package br.ufes.republicas.presentation.comum;

import java.time.Instant;

/**
 * Corpo padronizado das respostas de erro da API.
 */
public record RespostaErro(
        Instant timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho) {

    public static RespostaErro de(int status, String erro, String mensagem, String caminho) {
        return new RespostaErro(Instant.now(), status, erro, mensagem, caminho);
    }
}
