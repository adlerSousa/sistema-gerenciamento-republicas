package br.ufes.republicas.infrastructure.log;

/**
 * Contrato de formatacao de uma linha do arquivo de registro de operacoes.
 *
 * <p>Cada formato suportado (RNF13) possui uma implementacao propria, o que
 * permite trocar o formato do arquivo sem alterar o codigo que registra as
 * operacoes.
 */
public interface FormatadorRegistro {

    /**
     * Formato de arquivo produzido por este formatador.
     */
    FormatoRegistro formato();

    /**
     * Extensao de arquivo correspondente ao formato.
     */
    String extensao();

    /**
     * Converte uma mensagem de registro na representacao textual do formato.
     */
    String formatar(RegistroOperacao registro);
}
