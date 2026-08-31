package br.ufes.republicas.application.reclamacao;

import br.ufes.republicas.domain.reclamacao.ReclamacaoSugestao;
import br.ufes.republicas.domain.reclamacao.TipoReclamacao;

import java.time.LocalDate;
import java.util.List;

/**
 * Dados de uma reclamacao ou sugestao devolvidos pelos casos de uso do modulo.
 */
public record ReclamacaoSaida(
        Long id,
        Long republicaId,
        Long autorId,
        TipoReclamacao tipo,
        String descricao,
        LocalDate dataRegistro,
        LocalDate dataSolucao,
        Long idadeEmDias,
        boolean resolvida,
        boolean solucaoConfirmada,
        boolean excluida,
        List<Long> envolvidos) {

    public static ReclamacaoSaida de(ReclamacaoSugestao reclamacao) {
        return new ReclamacaoSaida(
                reclamacao.getId(),
                reclamacao.getRepublicaId(),
                reclamacao.getAutorId(),
                reclamacao.getTipo(),
                reclamacao.getDescricao(),
                reclamacao.getDataRegistro(),
                reclamacao.getDataSolucao(),
                reclamacao.idadeEmDias(),
                reclamacao.isResolvida(),
                reclamacao.isSolucaoConfirmada(),
                reclamacao.isExcluida(),
                reclamacao.getEnvolvidos());
    }
}
