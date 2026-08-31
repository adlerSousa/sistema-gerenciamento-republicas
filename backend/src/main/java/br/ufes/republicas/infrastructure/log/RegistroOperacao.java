package br.ufes.republicas.infrastructure.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Dados de uma ocorrencia registrada no arquivo de log (RF09).
 */
public record RegistroOperacao(
        String operacao,
        String nome,
        String usuario,
        LocalDateTime momento,
        String mensagemDaFalha) {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    public boolean ehFalha() {
        return mensagemDaFalha != null;
    }

    public String data() {
        return momento.format(FORMATO_DATA);
    }

    public String hora() {
        return momento.format(FORMATO_HORA);
    }

    /**
     * Mensagem no formato textual definido pela especificacao de requisitos.
     */
    public String mensagem() {
        if (ehFalha()) {
            return "Ocorreu a falha %s ao realizar a \"%s do contato %s, (%s, %s, e %s).\""
                    .formatted(mensagemDaFalha, operacao, nome, data(), hora(), usuario);
        }
        return "%s do contato %s, (%s, %s, e %s)"
                .formatted(operacao, nome, data(), hora(), usuario);
    }
}
