package br.ufes.republicas.infrastructure.persistence.financeiro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representacao da participacao de um morador no rateio de um lancamento, no
 * modelo de persistencia.
 */
@Entity
@Table(name = "participacao_lancamento")
public class ParticipacaoLancamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lancamento_id", nullable = false)
    private LancamentoEntity lancamento;

    @Column(name = "morador_id", nullable = false)
    private Long moradorId;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentual;

    @Column(name = "valor_fixo", precision = 12, scale = 2)
    private BigDecimal valorFixo;

    @Column(name = "valor_devido", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorDevido;

    @Column(nullable = false)
    private boolean pago;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LancamentoEntity getLancamento() {
        return lancamento;
    }

    public void setLancamento(LancamentoEntity lancamento) {
        this.lancamento = lancamento;
    }

    public Long getMoradorId() {
        return moradorId;
    }

    public void setMoradorId(Long moradorId) {
        this.moradorId = moradorId;
    }

    public BigDecimal getPercentual() {
        return percentual;
    }

    public void setPercentual(BigDecimal percentual) {
        this.percentual = percentual;
    }

    public BigDecimal getValorFixo() {
        return valorFixo;
    }

    public void setValorFixo(BigDecimal valorFixo) {
        this.valorFixo = valorFixo;
    }

    public BigDecimal getValorDevido() {
        return valorDevido;
    }

    public void setValorDevido(BigDecimal valorDevido) {
        this.valorDevido = valorDevido;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}
