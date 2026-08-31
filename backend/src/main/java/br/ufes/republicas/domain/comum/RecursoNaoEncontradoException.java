package br.ufes.republicas.domain.comum;

/**
 * Indica que um recurso referenciado nao existe.
 */
public class RecursoNaoEncontradoException extends DominioException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    /**
     * Mensagem redigida sem flexao de genero, de modo a permanecer correta para
     * qualquer recurso informado.
     */
    public static RecursoNaoEncontradoException de(String recurso, Long id) {
        return new RecursoNaoEncontradoException(
                "Nao existe %s com o identificador %d.".formatted(recurso, id));
    }
}
