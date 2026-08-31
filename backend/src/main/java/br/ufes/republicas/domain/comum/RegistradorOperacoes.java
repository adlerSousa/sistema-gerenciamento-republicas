package br.ufes.republicas.domain.comum;

/**
 * Contrato de registro de operacoes do sistema (RF09).
 *
 * <p>As implementacoes concretas, responsaveis pelos formatos de arquivo
 * suportados, residem na camada de infraestrutura.
 */
public interface RegistradorOperacoes {

    /**
     * Registra a realizacao bem-sucedida de uma operacao.
     *
     * @param operacao operacao realizada, por exemplo "Inclusao"
     * @param nome     identificacao do elemento sobre o qual a operacao ocorreu
     * @param usuario  usuario autenticado que realizou a operacao
     */
    void registrarSucesso(String operacao, String nome, String usuario);

    /**
     * Registra a falha na realizacao de uma operacao.
     *
     * @param operacao        operacao tentada
     * @param nome            identificacao do elemento envolvido
     * @param usuario         usuario autenticado que tentou a operacao
     * @param mensagemDaFalha descricao da falha ocorrida
     */
    void registrarFalha(String operacao, String nome, String usuario, String mensagemDaFalha);
}
