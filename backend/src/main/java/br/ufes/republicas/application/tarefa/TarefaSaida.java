package br.ufes.republicas.application.tarefa;

import br.ufes.republicas.domain.tarefa.Tarefa;

import java.time.LocalDate;
import java.util.List;

/**
 * Dados de uma tarefa devolvidos pelos casos de uso do modulo.
 */
public record TarefaSaida(
        Long id,
        Long republicaId,
        String descricao,
        LocalDate dataAgendamento,
        LocalDate dataTermino,
        boolean finalizada,
        LocalDate dataConclusao,
        String descricaoConclusao,
        boolean concluidaNoPrazo,
        List<Long> responsaveis) {

    public static TarefaSaida de(Tarefa tarefa) {
        return new TarefaSaida(
                tarefa.getId(),
                tarefa.getRepublicaId(),
                tarefa.getDescricao(),
                tarefa.getDataAgendamento(),
                tarefa.getDataTermino(),
                tarefa.isFinalizada(),
                tarefa.getDataConclusao(),
                tarefa.getDescricaoConclusao(),
                tarefa.concluidaNoPrazo(),
                tarefa.getResponsaveis());
    }
}
