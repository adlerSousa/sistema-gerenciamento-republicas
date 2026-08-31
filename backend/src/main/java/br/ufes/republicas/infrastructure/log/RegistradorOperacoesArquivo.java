package br.ufes.republicas.infrastructure.log;

import br.ufes.republicas.domain.comum.RegistradorOperacoes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registro das operacoes do sistema em arquivo (RF09).
 *
 * <p>O formato do arquivo e definido em configuracao e pode ser alterado sem
 * modificar o codigo que registra as operacoes, pois cada formato suportado
 * possui um formatador proprio.
 */
@Component
public class RegistradorOperacoesArquivo implements RegistradorOperacoes {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistradorOperacoesArquivo.class);

    private final Map<FormatoRegistro, FormatadorRegistro> formatadores;
    private final FormatoRegistro formatoConfigurado;
    private final Path diretorio;

    public RegistradorOperacoesArquivo(
            List<FormatadorRegistro> formatadores,
            @Value("${republicas.log.formato:CSV}") FormatoRegistro formatoConfigurado,
            @Value("${republicas.log.diretorio:./logs}") String diretorio) {

        this.formatadores = formatadores.stream()
                .collect(Collectors.toMap(FormatadorRegistro::formato, Function.identity()));
        this.formatoConfigurado = formatoConfigurado;
        this.diretorio = Path.of(diretorio);
    }

    @Override
    public void registrarSucesso(String operacao, String nome, String usuario) {
        escrever(new RegistroOperacao(operacao, nome, usuario, LocalDateTime.now(), null));
    }

    @Override
    public void registrarFalha(String operacao, String nome, String usuario, String mensagemDaFalha) {
        escrever(new RegistroOperacao(operacao, nome, usuario, LocalDateTime.now(), mensagemDaFalha));
    }

    private void escrever(RegistroOperacao registro) {
        FormatadorRegistro formatador = formatadores.get(formatoConfigurado);
        if (formatador == null) {
            LOGGER.warn("Nao ha formatador disponivel para o formato {}.", formatoConfigurado);
            return;
        }

        try {
            Files.createDirectories(diretorio);
            Path arquivo = diretorio.resolve(
                    "operacoes-%s.%s".formatted(LocalDate.now(), formatador.extensao()));
            Files.writeString(arquivo,
                    formatador.formatar(registro) + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException excecao) {
            // A indisponibilidade do arquivo de registro nao pode interromper a
            // operacao de negocio que estava sendo realizada.
            LOGGER.error("Nao foi possivel gravar o registro de operacoes.", excecao);
        }
    }
}
