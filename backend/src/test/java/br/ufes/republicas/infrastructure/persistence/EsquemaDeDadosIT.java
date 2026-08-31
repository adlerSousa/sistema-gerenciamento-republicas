package br.ufes.republicas.infrastructure.persistence;

import br.ufes.republicas.TesteDeIntegracao;
import br.ufes.republicas.domain.financeiro.LancamentoRepositorio;
import br.ufes.republicas.domain.financeiro.ReceitaColetivaRepositorio;
import br.ufes.republicas.domain.morador.MoradorRepositorio;
import br.ufes.republicas.domain.reclamacao.ReclamacaoSugestaoRepositorio;
import br.ufes.republicas.domain.republica.Republica;
import br.ufes.republicas.domain.republica.RepublicaRepositorio;
import br.ufes.republicas.domain.tarefa.TarefaRepositorio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica que as migracoes sao aplicadas, que o mapeamento objeto-relacional e
 * valido e que a massa de dados inicial do repositorio-base esta disponivel.
 */
@TesteDeIntegracao
@DisplayName("Esquema de dados e massa inicial")
class EsquemaDeDadosIT {

    @Autowired
    private RepublicaRepositorio republicaRepositorio;

    @Autowired
    private MoradorRepositorio moradorRepositorio;

    @Autowired
    private LancamentoRepositorio lancamentoRepositorio;

    @Autowired
    private ReceitaColetivaRepositorio receitaColetivaRepositorio;

    @Autowired
    private TarefaRepositorio tarefaRepositorio;

    @Autowired
    private ReclamacaoSugestaoRepositorio reclamacaoRepositorio;

    @Test
    @DisplayName("carrega as republicas da massa inicial com o controle de vagas coerente")
    void deveCarregarRepublicasDaMassaInicial() {
        assertThat(republicaRepositorio.listarTodas()).hasSize(3);

        Republica primeira = republicaRepositorio.buscarPorId(1L).orElseThrow();
        assertThat(primeira.getNome()).isEqualTo("Republica Vila Velha");
        assertThat(primeira.getTotalVagas()).isEqualTo(6);
        assertThat(primeira.getVagasOcupadas()).isEqualTo(4);
        assertThat(primeira.vagasDisponiveis()).isEqualTo(2);
    }

    @Test
    @DisplayName("distingue os moradores residentes dos moradores sem teto")
    void deveDistinguirMoradoresResidentesDosSemTeto() {
        assertThat(moradorRepositorio.listarPorRepublica(1L)).hasSize(4);
        assertThat(moradorRepositorio.listarSemTeto()).hasSize(3);
        assertThat(moradorRepositorio.buscarPorCpf("11122233344")).isPresent();
    }

    @Test
    @DisplayName("carrega os lancamentos com as participacoes no rateio")
    void deveCarregarLancamentosComAsParticipacoesNoRateio() {
        assertThat(lancamentoRepositorio.listarPorRepublica(1L)).isNotEmpty();

        var lancamento = lancamentoRepositorio.buscarPorId(1L).orElseThrow();
        assertThat(lancamento.getParticipacoes()).hasSize(4);
        assertThat(lancamento.totalDevido()).isEqualByComparingTo("320.00");
    }

    @Test
    @DisplayName("carrega o saldo acumulado da receita coletiva")
    void deveCarregarSaldoAcumuladoDaReceitaColetiva() {
        var receitaColetiva = receitaColetivaRepositorio.buscarPorRepublica(1L).orElseThrow();

        assertThat(receitaColetiva.getSaldo()).isEqualByComparingTo("800.00");
    }

    @Test
    @DisplayName("carrega tarefas e reclamacoes com seus responsaveis e envolvidos")
    void deveCarregarTarefasEReclamacoesComResponsaveisEEnvolvidos() {
        var tarefa = tarefaRepositorio.buscarPorId(1L).orElseThrow();
        assertThat(tarefa.getResponsaveis()).containsExactlyInAnyOrder(1L, 2L);

        var reclamacao = reclamacaoRepositorio.buscarPorId(1L).orElseThrow();
        assertThat(reclamacao.getEnvolvidos()).containsExactly(3L);
        assertThat(reclamacao.isResolvida()).isTrue();
    }
}
