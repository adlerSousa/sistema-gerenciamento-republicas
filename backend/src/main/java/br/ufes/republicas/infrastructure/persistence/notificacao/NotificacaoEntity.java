package br.ufes.republicas.infrastructure.persistence.notificacao;

import br.ufes.republicas.domain.notificacao.TipoNotificacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Representacao da notificacao no modelo de persistencia.
 */
@Entity
@Table(name = "notificacao")
public class NotificacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "morador_id", nullable = false)
    private Long moradorId;

    @Column(name = "republica_id")
    private Long republicaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoNotificacao tipo;

    @Column(nullable = false, length = 500)
    private String mensagem;

    @Column(nullable = false)
    private boolean lida;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @Column(name = "lancamento_id")
    private Long lancamentoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMoradorId() {
        return moradorId;
    }

    public void setMoradorId(Long moradorId) {
        this.moradorId = moradorId;
    }

    public Long getRepublicaId() {
        return republicaId;
    }

    public void setRepublicaId(Long republicaId) {
        this.republicaId = republicaId;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Long getLancamentoId() {
        return lancamentoId;
    }

    public void setLancamentoId(Long lancamentoId) {
        this.lancamentoId = lancamentoId;
    }
}
