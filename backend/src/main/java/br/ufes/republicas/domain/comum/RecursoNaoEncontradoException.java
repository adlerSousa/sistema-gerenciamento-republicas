package br.ufes.republicas.domain.comum;

/**
 * Indica que um recurso referenciado nao existe.
 */
public class RecursoNaoEncontradoException extends DominioException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public static RecursoNaoEncontradoException de(String recurso, Long id) {
        return new RecursoNaoEncontradoException(
                "%s de identificador %d nao foi encontrado.".formatted(recurso, id));
    }
}
