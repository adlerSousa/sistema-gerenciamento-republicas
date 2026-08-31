package br.ufes.republicas.application.tarefa;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.comum.RegistradorOperacoes;
import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import br.ufes.republicas.domain.tarefa.Tarefa;
import br.ufes.republicas.domain.tarefa.TarefaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Manutencao das tarefas domesticas da republica (UC3 e UC12).
 */
@Service
public class GerenciarTarefasCasoDeUso {

    private final TarefaRepositorio tarefaRepositorio;
    private final RegistradorOperacoes registradorOperacoes;

    public GerenciarTarefasCasoDeUso(TarefaRepositorio tarefaRepositorio,
                                     RegistradorOperacoes registradorOperacoes) {
        this.tarefaRepositorio = tarefaRepositorio;
        this.registradorOperacoes = registradorOperacoes;
    }

    @Transactional
    public TarefaSaida cadastrar(CadastrarTarefaEntrada entrada) {
        Tarefa tarefa = new Tarefa(
                entrada.republicaId(),
                entrada.descricao(),
                entrada.dataAgendamento(),
                entrada.dataTermino(),
                entrada.responsaveis());

        return TarefaSaida.de(tarefaRepositorio.salvar(tarefa));
    }

    @Transactional(readOnly = true)
    public List<TarefaSaida> listarPorRepublica(Long republicaId) {
        return tarefaRepositorio.listarPorRepublica(republicaId).stream()
                .map(TarefaSaida::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TarefaSaida> listarPorResponsavel(Long moradorId) {
        return tarefaRepositorio.listarPorResponsavel(moradorId).stream()
                .map(TarefaSaida::de)
                .toList();
    }

    /**
     * Registra a conclusao de uma tarefa pelo morador responsavel (UC12).
     */
    @Transactional
    public TarefaSaida registrarConclusao(Long tarefaId,
                                          Long moradorId,
                                          String descricaoConclusao,
                                          String usuario) {
        Tarefa tarefa = tarefaRepositorio.buscarPorId(tarefaId)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Tarefa", tarefaId));

        if (!tarefa.ehResponsavel(moradorId)) {
            throw new RegraDeNegocioException(
                    "Apenas um morador responsavel pode registrar a conclusao da tarefa.");
        }

        tarefa.registrarConclusao(descricaoConclusao, LocalDate.now());
        Tarefa tarefaSalva = tarefaRepositorio.salvar(tarefa);

        registradorOperacoes.registrarSucesso("Correcao", tarefa.getDescricao(), usuario);

        return TarefaSaida.de(tarefaSalva);
    }
}
