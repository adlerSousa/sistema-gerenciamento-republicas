package br.ufes.republicas.application.tarefa;

import java.time.LocalDate;
import java.util.List;

/**
 * Dados necessarios para cadastrar uma tarefa domestica (UC3).
 */
public record CadastrarTarefaEntrada(
        Long republicaId,
        String descricao,
        LocalDate dataAgendamento,
        LocalDate dataTermino,
        List<Long> responsaveis) {
}
