package br.ufes.republicas.domain.financeiro;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Saldo acumulado de receitas coletivas de uma republica (BR05).
 *
 * <p>As receitas coletivas provem de doacoes, de renda de eventos organizados e
 * de outras fontes. O saldo pode ficar acumulado para os meses seguintes e pode
 * ser utilizado no pagamento de lancamentos de despesa, sendo debitado no
 * momento do uso (BR06).
 */
public class ReceitaColetiva {

    private Long id;
    private Long republicaId;
    private BigDecimal saldo;

    protected ReceitaColetiva() {
        // Exigido pelos mecanismos de reconstrucao a partir da persistencia.
    }

    public ReceitaColetiva(Long republicaId) {
        this.republicaId = Objects.requireNonNull(republicaId, "A republica e obrigatoria.");
        this.saldo = BigDecimal.ZERO;
    }

    /**
     * Acrescenta um valor ao saldo acumulado.
     */
    public void creditar(BigDecimal valor) {
        Objects.requireNonNull(valor, "O valor e obrigatorio.");
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("O valor creditado deve ser positivo.");
        }
        this.saldo = this.saldo.add(valor);
    }

    /**
     * Retira do saldo acumulado o valor utilizado no pagamento de um lancamento
     * de despesa.
     */
    public void debitar(BigDecimal valor) {
        Objects.requireNonNull(valor, "O valor e obrigatorio.");
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("O valor debitado deve ser positivo.");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public boolean possuiSaldoSuficientePara(BigDecimal valor) {
        Objects.requireNonNull(valor, "O valor e obrigatorio.");
        return saldo.compareTo(valor) >= 0;
    }

    public Long getId() {
        return id;
    }

    public void definirId(Long id) {
        this.id = id;
    }

    public Long getRepublicaId() {
        return republicaId;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void definirSaldo(BigDecimal saldo) {
        this.saldo = Objects.requireNonNull(saldo, "O saldo e obrigatorio.");
    }
}
