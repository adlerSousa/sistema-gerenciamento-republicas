package br.ufes.republicas.domain.morador;

/**
 * Origem de uma solicitacao de moradia.
 *
 * <p>Um CONVITE parte do representante da republica (UC5); uma SOLICITACAO parte
 * do proprio morador interessado (UC14).
 */
public enum OrigemSolicitacao {
    CONVITE,
    SOLICITACAO
}
