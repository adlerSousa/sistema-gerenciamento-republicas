package br.ufes.republicas.infrastructure.log;

import org.springframework.stereotype.Component;

/**
 * Formatacao do registro de operacoes em JSON (RNF13).
 */
@Component
public class FormatadorJson implements FormatadorRegistro {

    @Override
    public FormatoRegistro formato() {
        return FormatoRegistro.JSON;
    }

    @Override
    public String extensao() {
        return "json";
    }

    @Override
    public String formatar(RegistroOperacao registro) {
        return "{\"data\":\"%s\",\"hora\":\"%s\",\"usuario\":\"%s\",\"operacao\":\"%s\","
                .formatted(escapar(registro.data()), escapar(registro.hora()),
                        escapar(registro.usuario()), escapar(registro.operacao()))
                + "\"nome\":\"%s\",\"situacao\":\"%s\",\"mensagem\":\"%s\"}"
                .formatted(escapar(registro.nome()),
                        registro.ehFalha() ? "FALHA" : "SUCESSO",
                        escapar(registro.mensagem()));
    }

    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
