package br.ufes.republicas.domain.republica;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Republica")
class RepublicaTest {

    private static final Endereco ENDERECO = new Endereco(
            "Rua das Palmeiras, 120", "29100-000", "Centro", "Ao lado da praca", null);

    private Republica republicaCom(int totalVagas, int vagasOcupadas) {
        return new Republica(
                "Republica de Teste",
                LocalDate.of(2020, 1, 1),
                ENDERECO,
                "Internet e area de estudos.",
                new BigDecimal("450.00"),
                totalVagas,
                vagasOcupadas);
    }

    @Test
    @DisplayName("calcula as vagas disponiveis como a diferenca entre o total e as ocupadas")
    void deveCalcularVagasDisponiveisComoDiferencaEntreTotalEOcupadas() {
        Republica republica = republicaCom(6, 4);

        assertThat(republica.vagasDisponiveis()).isEqualTo(2);
    }

    @Test
    @DisplayName("nao possui vagas disponiveis quando todas estao ocupadas")
    void deveIndicarAusenciaDeVagasQuandoTodasEstaoOcupadas() {
        Republica republica = republicaCom(5, 5);

        assertThat(republica.vagasDisponiveis()).isZero();
    }

    @Test
    @DisplayName("rejeita a criacao quando as vagas ocupadas superam o total")
    void deveRejeitarCriacaoQuandoVagasOcupadasSuperamOTotal() {
        assertThatThrownBy(() -> republicaCom(4, 5))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("nao pode ser superior ao total de vagas");
    }

    @Test
    @DisplayName("rejeita a criacao quando o total de vagas e negativo")
    void deveRejeitarCriacaoQuandoTotalDeVagasENegativo() {
        assertThatThrownBy(() -> republicaCom(-1, 0))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("nao pode ser negativo");
    }

    @Test
    @DisplayName("rejeita reduzir o total de vagas abaixo das vagas ocupadas")
    void deveRejeitarReduzirTotalDeVagasAbaixoDasOcupadas() {
        Republica republica = republicaCom(6, 4);

        assertThatThrownBy(() -> republica.alterarTotalDeVagas(3))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("nao pode ser inferior");
    }

    @Test
    @DisplayName("passa a constar como extinta apos o registro da data de extincao")
    void deveConstarComoExtintaAposRegistroDaDataDeExtincao() {
        Republica republica = republicaCom(6, 4);
        assertThat(republica.estaExtinta()).isFalse();

        republica.extinguir(LocalDate.of(2026, 1, 31));

        assertThat(republica.estaExtinta()).isTrue();
    }
}
