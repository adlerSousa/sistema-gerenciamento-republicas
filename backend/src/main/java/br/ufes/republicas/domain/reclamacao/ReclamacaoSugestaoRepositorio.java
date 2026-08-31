package br.ufes.republicas.domain.reclamacao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Porta de acesso ao repositorio de reclamacoes e sugestoes.
 */
public interface ReclamacaoSugestaoRepositorio {

    ReclamacaoSugestao salvar(ReclamacaoSugestao reclamacaoSugestao);

    Optional<ReclamacaoSugestao> buscarPorId(Long id);

    List<ReclamacaoSugestao> listarPorRepublica(Long republicaId);

    List<ReclamacaoSugestao> listarPorAutor(Long autorId);

    List<ReclamacaoSugestao> listarPorEnvolvidoEPeriodo(Long moradorId, LocalDate inicio, LocalDate fim);
}
