package br.ufes.republicas.domain.notificacao;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Aviso apresentado ao morador no acesso ao sistema (RF01 e RF02).
 */
public class Notificacao {

    private Long id;
    private Long moradorId;
    private Long republicaId;
    private TipoNotificacao tipo;
    private String mensagem;
    private boolean lida;
    private LocalDate dataCriacao;
    private Long lancamentoId;

    protected Notificacao() {
        // Exigido pelos mecanismos de reconstrucao a partir da persistencia.
    }

    public Notificacao(Long moradorId,
                       Long republicaId,
                       TipoNotificacao tipo,
                       String mensagem,
                       LocalDate dataCriacao) {

        this.moradorId = Objects.requireNonNull(moradorId, "O morador destinatario e obrigatorio.");
        this.republicaId = republicaId;
        this.tipo = Objects.requireNonNull(tipo, "O tipo da notificacao e obrigatorio.");
        this.mensagem = Objects.requireNonNull(mensagem, "A mensagem e obrigatoria.");
        this.dataCriacao = Objects.requireNonNull(dataCriacao, "A data de criacao e obrigatoria.");
        this.lida = false;
    }

    public void marcarComoLida() {
        this.lida = true;
    }

    public Long getId() {
        return id;
    }

    public void definirId(Long id) {
        this.id = id;
    }

    public Long getMoradorId() {
        return moradorId;
    }

    public Long getRepublicaId() {
        return republicaId;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public boolean isLida() {
        return lida;
    }

    public void definirLida(boolean lida) {
        this.lida = lida;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public Long getLancamentoId() {
        return lancamentoId;
    }

    public void vincularLancamento(Long lancamentoId) {
        this.lancamentoId = lancamentoId;
    }
}
