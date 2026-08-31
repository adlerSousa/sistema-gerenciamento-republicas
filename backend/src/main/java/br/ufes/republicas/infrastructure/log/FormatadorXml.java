package br.ufes.republicas.infrastructure.log;

import org.springframework.stereotype.Component;

/**
 * Formatacao do registro de operacoes em XML (RNF13).
 */
@Component
public class FormatadorXml implements FormatadorRegistro {

    @Override
    public FormatoRegistro formato() {
        return FormatoRegistro.XML;
    }

    @Override
    public String extensao() {
        return "xml";
    }

    @Override
    public String formatar(RegistroOperacao registro) {
        return ("<registro>"
                + "<data>%s</data>"
                + "<hora>%s</hora>"
                + "<usuario>%s</usuario>"
                + "<operacao>%s</operacao>"
                + "<nome>%s</nome>"
                + "<situacao>%s</situacao>"
                + "<mensagem>%s</mensagem>"
                + "</registro>")
                .formatted(escapar(registro.data()),
                        escapar(registro.hora()),
                        escapar(registro.usuario()),
                        escapar(registro.operacao()),
                        escapar(registro.nome()),
                        registro.ehFalha() ? "FALHA" : "SUCESSO",
                        escapar(registro.mensagem()));
    }

    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
