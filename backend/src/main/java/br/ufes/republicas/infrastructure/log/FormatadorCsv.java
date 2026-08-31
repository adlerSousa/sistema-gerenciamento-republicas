package br.ufes.republicas.infrastructure.log;

import org.springframework.stereotype.Component;

/**
 * Formatacao do registro de operacoes em CSV, com separacao de campos por ponto
 * e virgula (RNF13).
 */
@Component
public class FormatadorCsv implements FormatadorRegistro {

    private static final String SEPARADOR = ";";

    @Override
    public FormatoRegistro formato() {
        return FormatoRegistro.CSV;
    }

    @Override
    public String extensao() {
        return "csv";
    }

    @Override
    public String formatar(RegistroOperacao registro) {
        return String.join(SEPARADOR,
                escapar(registro.data()),
                escapar(registro.hora()),
                escapar(registro.usuario()),
                escapar(registro.operacao()),
                escapar(registro.nome()),
                escapar(registro.ehFalha() ? "FALHA" : "SUCESSO"),
                escapar(registro.mensagem()));
    }

    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        String semQuebras = valor.replace("\n", " ").replace("\r", " ");
        if (semQuebras.contains(SEPARADOR) || semQuebras.contains("\"")) {
            return "\"" + semQuebras.replace("\"", "\"\"") + "\"";
        }
        return semQuebras;
    }
}
