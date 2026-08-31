package br.ufes.republicas.domain.financeiro;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Participacao de um morador no rateio de um lancamento (BR06).
 *
 * <p>Conforme a forma de rateio do lancamento, a participacao e expressa por um
 * percentual ou por um valor fixo. O valor devido corresponde a parcela que cabe
 * ao morador.
 */
public class ParticipacaoLancamento {

    private Long id;
    private Long lancamentoId;
    private Long moradorId;
    private BigDecimal percentual;
    private BigDecimal valorFixo;
    private BigDecimal valorDevido;
    private boolean pago;
    private LocalDate dataPagamento;

    protected ParticipacaoLancamento() {
        // Exigido pelos mecanismos de reconstrucao a partir da persistencia.
    }

    public ParticipacaoLancamento(Long moradorId,
                                  BigDecimal percentual,
                                  BigDecimal valorFixo,
                                  BigDecimal valorDevido) {
        this.moradorId = Objects.requireNonNull(moradorId, "O morador participante e obrigatorio.");
        this.valorDevido = Objects.requireNonNull(valorDevido, "O valor devido e obrigatorio.");

        if (valorDevido.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraDeNegocioException("O valor devido nao pode ser negativo.");
        }

        this.percentual = percentual;
        this.valorFixo = valorFixo;
        this.pago = false;
    }

    /**
     * Registra o pagamento da parcela devida pelo morador.
     */
    public void registrarPagamento(LocalDate dataPagamento) {
        Objects.requireNonNull(dataPagamento, "A data de pagamento e obrigatoria.");
        if (pago) {
            throw new RegraDeNegocioException("A parcela deste morador ja foi paga.");
        }
        this.pago = true;
        this.dataPagamento = dataPagamento;
    }

    public Long getId() {
        return id;
    }

    public void definirId(Long id) {
        this.id = id;
    }

    public Long getLancamentoId() {
        return lancamentoId;
    }

    public void definirLancamentoId(Long lancamentoId) {
        this.lancamentoId = lancamentoId;
    }

    public Long getMoradorId() {
        return moradorId;
    }

    public BigDecimal getPercentual() {
        return percentual;
    }

    public BigDecimal getValorFixo() {
        return valorFixo;
    }

    public BigDecimal getValorDevido() {
        return valorDevido;
    }

    public boolean isPago() {
        return pago;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void restaurarPagamento(boolean pago, LocalDate dataPagamento) {
        this.pago = pago;
        this.dataPagamento = dataPagamento;
    }
}
