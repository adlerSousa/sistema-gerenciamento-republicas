package br.ufes.republicas.domain.comum;

/**
 * Excecao base das violacoes detectadas na camada de dominio.
 */
public abstract class DominioException extends RuntimeException {

    protected DominioException(String mensagem) {
        super(mensagem);
    }
}
