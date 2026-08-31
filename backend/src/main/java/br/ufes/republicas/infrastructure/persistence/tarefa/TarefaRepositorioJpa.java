package br.ufes.republicas.infrastructure.persistence.tarefa;

import br.ufes.republicas.domain.tarefa.Tarefa;
import br.ufes.republicas.domain.tarefa.TarefaRepositorio;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

/**
 * Implementacao da porta de repositorio de tarefas sobre JPA.
 */
@Repository
public class TarefaRepositorioJpa implements TarefaRepositorio {

    private final TarefaJpaRepository repositorio;

    public TarefaRepositorioJpa(TarefaJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Tarefa salvar(Tarefa tarefa) {
        TarefaEntity entidade = tarefa.getId() == null
                ? new TarefaEntity()
                : repositorio.findById(tarefa.getId()).orElseGet(TarefaEntity::new);

        entidade.setId(tarefa.getId());
        entidade.setRepublicaId(tarefa.getRepublicaId());
        entidade.setDescricao(tarefa.getDescricao());
        entidade.setDataAgendamento(tarefa.getDataAgendamento());
        entidade.setDataTermino(tarefa.getDataTermino());
        entidade.setFinalizada(tarefa.isFinalizada());
        entidade.setDataConclusao(tarefa.getDataConclusao());
        entidade.setDescricaoConclusao(tarefa.getDescricaoConclusao());
        entidade.setResponsaveis(new LinkedHashSet<>(tarefa.getResponsaveis()));

        return paraDominio(repositorio.save(entidade));
    }

    @Override
    public Optional<Tarefa> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    public List<Tarefa> listarPorRepublica(Long republicaId) {
        return repositorio.findByRepublicaIdOrderByDataTerminoAsc(republicaId).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<Tarefa> listarPorResponsavel(Long moradorId) {
        return repositorio.buscarPorResponsavel(moradorId).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<Tarefa> listarPorResponsavelEPeriodo(Long moradorId, LocalDate inicio, LocalDate fim) {
        return repositorio.buscarPorResponsavelEPeriodo(moradorId, inicio, fim).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public void remover(Long id) {
        repositorio.deleteById(id);
    }

    private Tarefa paraDominio(TarefaEntity entidade) {
        Tarefa tarefa = new Tarefa(
                entidade.getRepublicaId(),
                entidade.getDescricao(),
                entidade.getDataAgendamento(),
                entidade.getDataTermino(),
                List.copyOf(entidade.getResponsaveis()));

        tarefa.definirId(entidade.getId());
        tarefa.restaurarConclusao(
                entidade.isFinalizada(),
                entidade.getDataConclusao(),
                entidade.getDescricaoConclusao());
        return tarefa;
    }
}
