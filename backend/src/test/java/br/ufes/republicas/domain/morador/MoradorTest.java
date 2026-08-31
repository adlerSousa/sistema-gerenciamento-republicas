package br.ufes.republicas.domain.morador;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Morador")
class MoradorTest {

    private Morador novoMorador() {
        return new Morador(
                "Ana Beatriz Moreira", "Ana", "11122233344",
                "27999110001", "27999220001", "27999330001");
    }

    @Test
    @DisplayName("comeca na condicao de sem teto quando nao reside em republica")
    void deveComecarNaCondicaoDeSemTeto() {
        assertThat(novoMorador().estaSemTeto()).isTrue();
    }

    @Test
    @DisplayName("deixa a condicao de sem teto ao ingressar em uma republica")
    void deveDeixarCondicaoDeSemTetoAoIngressar() {
        Morador morador = novoMorador();

        morador.ingressarEm(1L, LocalDate.of(2026, 3, 1));

        assertThat(morador.estaSemTeto()).isFalse();
        assertThat(morador.getRepublicaId()).isEqualTo(1L);
        assertThat(morador.getDataIngresso()).isEqualTo(LocalDate.of(2026, 3, 1));
    }

    @Test
    @DisplayName("rejeita ingressar em uma segunda republica enquanto reside em outra")
    void deveRejeitarIngressoQuandoJaResideEmOutraRepublica() {
        Morador morador = novoMorador();
        morador.ingressarEm(1L, LocalDate.of(2026, 3, 1));

        assertThatThrownBy(() -> morador.ingressarEm(2L, LocalDate.of(2026, 4, 1)))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("nao pode residir em mais de uma");
    }

    @Test
    @DisplayName("retorna a condicao de sem teto ao sair da republica")
    void deveRetornarACondicaoDeSemTetoAoSairDaRepublica() {
        Morador morador = novoMorador();
        morador.ingressarEm(1L, LocalDate.of(2026, 3, 1));

        morador.sairDaRepublica();

        assertThat(morador.estaSemTeto()).isTrue();
        assertThat(morador.getDataIngresso()).isNull();
    }

    @Test
    @DisplayName("rejeita a saida quando nao reside em nenhuma republica")
    void deveRejeitarSaidaQuandoNaoResideEmNenhumaRepublica() {
        Morador morador = novoMorador();

        assertThatThrownBy(morador::sairDaRepublica)
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("nao reside em nenhuma republica");
    }
}
