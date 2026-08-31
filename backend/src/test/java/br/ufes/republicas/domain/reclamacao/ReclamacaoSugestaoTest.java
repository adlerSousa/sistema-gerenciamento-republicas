package br.ufes.republicas.domain.reclamacao;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ReclamacaoSugestao")
class ReclamacaoSugestaoTest {

    private static final LocalDate REGISTRO = LocalDate.of(2026, 5, 4);

    private ReclamacaoSugestao novaReclamacao() {
        return new ReclamacaoSugestao(
                1L, 2L, TipoReclamacao.RECLAMACAO,
                "Louca acumulada na pia.", REGISTRO, List.of(3L));
    }

    @Test
    @DisplayName("calcula a idade em dias entre o registro e a solucao")
    void deveCalcularIdadeEmDiasEntreRegistroESolucao() {
        ReclamacaoSugestao reclamacao = novaReclamacao();

        reclamacao.marcarComoResolvida(REGISTRO.plusDays(3));

        assertThat(reclamacao.idadeEmDias()).isEqualTo(3L);
    }

    @Test
    @DisplayName("nao possui idade enquanto nao for solucionada")
    void naoDevePossuirIdadeEnquantoNaoForSolucionada() {
        assertThat(novaReclamacao().idadeEmDias()).isNull();
    }

    @Test
    @DisplayName("rejeita confirmar a solucao antes de o registro ser resolvido")
    void deveRejeitarConfirmarSolucaoAntesDeResolver() {
        ReclamacaoSugestao reclamacao = novaReclamacao();

        assertThatThrownBy(reclamacao::confirmarSolucao)
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("precisa estar resolvido");
    }

    @Test
    @DisplayName("confirma a solucao apos o registro ser resolvido")
    void deveConfirmarSolucaoAposORegistroSerResolvido() {
        ReclamacaoSugestao reclamacao = novaReclamacao();
        reclamacao.marcarComoResolvida(REGISTRO.plusDays(2));

        reclamacao.confirmarSolucao();

        assertThat(reclamacao.isSolucaoConfirmada()).isTrue();
    }

    @Test
    @DisplayName("rejeita resolver um registro ja excluido")
    void deveRejeitarResolverRegistroJaExcluido() {
        ReclamacaoSugestao reclamacao = novaReclamacao();
        reclamacao.excluir();

        assertThatThrownBy(() -> reclamacao.marcarComoResolvida(REGISTRO.plusDays(1)))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("foi excluido");
    }

    @Test
    @DisplayName("reconhece os moradores envolvidos no registro")
    void deveReconhecerOsMoradoresEnvolvidos() {
        ReclamacaoSugestao reclamacao = novaReclamacao();

        assertThat(reclamacao.envolve(3L)).isTrue();
        assertThat(reclamacao.envolve(4L)).isFalse();
    }
}
