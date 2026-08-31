package br.ufes.republicas.domain.financeiro;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ReceitaColetiva")
class ReceitaColetivaTest {

    @Test
    @DisplayName("comeca com saldo zerado")
    void deveComecarComSaldoZerado() {
        assertThat(new ReceitaColetiva(1L).getSaldo()).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("acumula o saldo a cada credito registrado")
    void deveAcumularSaldoACadaCreditoRegistrado() {
        ReceitaColetiva receitaColetiva = new ReceitaColetiva(1L);

        receitaColetiva.creditar(new BigDecimal("500.00"));
        receitaColetiva.creditar(new BigDecimal("300.00"));

        assertThat(receitaColetiva.getSaldo()).isEqualByComparingTo("800.00");
    }

    @Test
    @DisplayName("reduz o saldo no valor debitado")
    void deveReduzirSaldoNoValorDebitado() {
        ReceitaColetiva receitaColetiva = new ReceitaColetiva(1L);
        receitaColetiva.creditar(new BigDecimal("800.00"));

        receitaColetiva.debitar(new BigDecimal("250.00"));

        assertThat(receitaColetiva.getSaldo()).isEqualByComparingTo("550.00");
    }

    @Test
    @DisplayName("informa se o saldo cobre um valor consultado")
    void deveInformarSeOSaldoCobreUmValorConsultado() {
        ReceitaColetiva receitaColetiva = new ReceitaColetiva(1L);
        receitaColetiva.creditar(new BigDecimal("200.00"));

        assertThat(receitaColetiva.possuiSaldoSuficientePara(new BigDecimal("200.00"))).isTrue();
        assertThat(receitaColetiva.possuiSaldoSuficientePara(new BigDecimal("200.01"))).isFalse();
    }

    @Test
    @DisplayName("rejeita credito e debito de valores nao positivos")
    void deveRejeitarCreditoEDebitoDeValoresNaoPositivos() {
        ReceitaColetiva receitaColetiva = new ReceitaColetiva(1L);

        assertThatThrownBy(() -> receitaColetiva.creditar(BigDecimal.ZERO))
                .isInstanceOf(RegraDeNegocioException.class);
        assertThatThrownBy(() -> receitaColetiva.debitar(new BigDecimal("-1")))
                .isInstanceOf(RegraDeNegocioException.class);
    }
}
