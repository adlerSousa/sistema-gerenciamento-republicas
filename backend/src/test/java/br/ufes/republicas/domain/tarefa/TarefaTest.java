package br.ufes.republicas.domain.tarefa;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tarefa")
class TarefaTest {

    private static final LocalDate AGENDAMENTO = LocalDate.of(2026, 5, 2);
    private static final LocalDate TERMINO = LocalDate.of(2026, 5, 9);

    private Tarefa novaTarefa() {
        return new Tarefa(1L, "Limpeza da area comum", AGENDAMENTO, TERMINO, List.of(1L, 2L));
    }

    @Test
    @DisplayName("rejeita a criacao sem responsaveis")
    void deveRejeitarCriacaoSemResponsaveis() {
        assertThatThrownBy(() -> new Tarefa(1L, "Limpeza", AGENDAMENTO, TERMINO, List.of()))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("ao menos um responsavel");
    }

    @Test
    @DisplayName("rejeita a criacao com termino anterior ao agendamento")
    void deveRejeitarCriacaoComTerminoAnteriorAoAgendamento() {
        assertThatThrownBy(() -> new Tarefa(
                1L, "Limpeza", TERMINO, AGENDAMENTO, List.of(1L)))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("nao pode ser anterior");
    }

    @Test
    @DisplayName("considera concluida no prazo quando a conclusao ocorre ate a data de termino")
    void deveConsiderarConcluidaNoPrazoQuandoConclusaoOcorreAteOTermino() {
        Tarefa tarefa = novaTarefa();

        tarefa.registrarConclusao("Area varrida.", TERMINO.minusDays(1));

        assertThat(tarefa.isFinalizada()).isTrue();
        assertThat(tarefa.concluidaNoPrazo()).isTrue();
    }

    @Test
    @DisplayName("nao considera concluida no prazo quando a conclusao ocorre apos o termino")
    void naoDeveConsiderarConcluidaNoPrazoQuandoConclusaoOcorreAposOTermino() {
        Tarefa tarefa = novaTarefa();

        tarefa.registrarConclusao("Concluida com atraso.", TERMINO.plusDays(3));

        assertThat(tarefa.isFinalizada()).isTrue();
        assertThat(tarefa.concluidaNoPrazo()).isFalse();
    }

    @Test
    @DisplayName("rejeita registrar a conclusao duas vezes")
    void deveRejeitarRegistrarConclusaoDuasVezes() {
        Tarefa tarefa = novaTarefa();
        tarefa.registrarConclusao("Area varrida.", TERMINO);

        assertThatThrownBy(() -> tarefa.registrarConclusao("De novo.", TERMINO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("ja foi concluida");
    }

    @Test
    @DisplayName("reconhece os moradores responsaveis pela tarefa")
    void deveReconhecerOsMoradoresResponsaveis() {
        Tarefa tarefa = novaTarefa();

        assertThat(tarefa.ehResponsavel(1L)).isTrue();
        assertThat(tarefa.ehResponsavel(3L)).isFalse();
    }
}
