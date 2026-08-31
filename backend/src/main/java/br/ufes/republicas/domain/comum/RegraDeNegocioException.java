package br.ufes.republicas.domain.comum;

/**
 * Indica que uma regra de negocio do sistema foi violada.
 */
public class RegraDeNegocioException extends DominioException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
