package br.ufes.republicas.domain.tarefa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Porta de acesso ao repositorio de tarefas domesticas.
 */
public interface TarefaRepositorio {

    Tarefa salvar(Tarefa tarefa);

    Optional<Tarefa> buscarPorId(Long id);

    List<Tarefa> listarPorRepublica(Long republicaId);

    List<Tarefa> listarPorResponsavel(Long moradorId);

    List<Tarefa> listarPorResponsavelEPeriodo(Long moradorId, LocalDate inicio, LocalDate fim);

    void remover(Long id);
}
