package br.ufes.republicas.domain.financeiro;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Lancamento")
class LancamentoTest {

    private static final LocalDate HOJE = LocalDate.of(2026, 6, 15);

    private Lancamento despesaComVencimentoEm(LocalDate vencimento) {
        return new Lancamento(
                1L,
                TipoLancamento.DESPESA,
                "Conta de energia eletrica",
                new BigDecimal("320.00"),
                vencimento,
                HOJE.minusDays(10),
                Periodicidade.UNICA,
                FormaRateio.PERCENTUAL);
    }

    @Test
    @DisplayName("rejeita a criacao com valor nao positivo")
    void deveRejeitarCriacaoComValorNaoPositivo() {
        assertThatThrownBy(() -> new Lancamento(
                1L, TipoLancamento.DESPESA, "Conta", BigDecimal.ZERO,
                HOJE, HOJE, Periodicidade.UNICA, FormaRateio.PERCENTUAL))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("deve ser positivo");
    }

    @Test
    @DisplayName("sinaliza vencimento proximo quando vence dentro do prazo de antecedencia")
    void deveSinalizarVencimentoProximoQuandoVenceDentroDoPrazo() {
        Lancamento lancamento = despesaComVencimentoEm(HOJE.plusDays(3));

        assertThat(lancamento.venceAte(HOJE, 5)).isTrue();
    }

    @Test
    @DisplayName("nao sinaliza vencimento proximo quando vence apos o prazo de antecedencia")
    void naoDeveSinalizarVencimentoProximoQuandoVenceAposOPrazo() {
        Lancamento lancamento = despesaComVencimentoEm(HOJE.plusDays(10));

        assertThat(lancamento.venceAte(HOJE, 5)).isFalse();
    }

    @Test
    @DisplayName("mantem o aviso apos o vencimento enquanto o lancamento estiver pendente")
    void deveManterAvisoAposOVencimentoEnquantoPendente() {
        Lancamento lancamento = despesaComVencimentoEm(HOJE.minusDays(4));

        assertThat(lancamento.venceAte(HOJE, 5)).isTrue();
        assertThat(lancamento.estaVencido(HOJE)).isTrue();
    }

    @Test
    @DisplayName("passa a constar como pago quando todas as participacoes estao quitadas")
    void deveConstarComoPagoQuandoTodasAsParticipacoesEstaoQuitadas() {
        Lancamento lancamento = despesaComVencimentoEm(HOJE.plusDays(3));
        ParticipacaoLancamento primeira =
                new ParticipacaoLancamento(1L, new BigDecimal("50.00"), null, new BigDecimal("160.00"));
        ParticipacaoLancamento segunda =
                new ParticipacaoLancamento(2L, new BigDecimal("50.00"), null, new BigDecimal("160.00"));
        lancamento.adicionarParticipacao(primeira);
        lancamento.adicionarParticipacao(segunda);

        primeira.registrarPagamento(HOJE);
        lancamento.atualizarStatusConformeParticipacoes();
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);

        segunda.registrarPagamento(HOJE);
        lancamento.atualizarStatusConformeParticipacoes();
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PAGO);
    }

    @Test
    @DisplayName("rejeita a inclusao do mesmo morador duas vezes no rateio")
    void deveRejeitarInclusaoDoMesmoMoradorDuasVezesNoRateio() {
        Lancamento lancamento = despesaComVencimentoEm(HOJE.plusDays(3));
        lancamento.adicionarParticipacao(
                new ParticipacaoLancamento(1L, new BigDecimal("50.00"), null, new BigDecimal("160.00")));

        assertThatThrownBy(() -> lancamento.adicionarParticipacao(
                new ParticipacaoLancamento(1L, new BigDecimal("50.00"), null, new BigDecimal("160.00"))))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("ja consta como participante");
    }

    @Test
    @DisplayName("soma o total devido a partir das participacoes")
    void deveSomarTotalDevidoAPartirDasParticipacoes() {
        Lancamento lancamento = despesaComVencimentoEm(HOJE.plusDays(3));
        lancamento.adicionarParticipacao(
                new ParticipacaoLancamento(1L, null, new BigDecimal("200.00"), new BigDecimal("200.00")));
        lancamento.adicionarParticipacao(
                new ParticipacaoLancamento(2L, null, new BigDecimal("120.00"), new BigDecimal("120.00")));

        assertThat(lancamento.totalDevido()).isEqualByComparingTo("320.00");
    }
}
