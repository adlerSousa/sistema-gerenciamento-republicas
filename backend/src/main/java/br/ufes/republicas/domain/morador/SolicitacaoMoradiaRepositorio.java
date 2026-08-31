package br.ufes.republicas.domain.morador;

import java.util.List;
import java.util.Optional;

/**
 * Porta de acesso ao repositorio de solicitacoes de moradia.
 */
public interface SolicitacaoMoradiaRepositorio {

    SolicitacaoMoradia salvar(SolicitacaoMoradia solicitacao);

    Optional<SolicitacaoMoradia> buscarPorId(Long id);

    List<SolicitacaoMoradia> listarPendentesPorRepublica(Long republicaId);

    List<SolicitacaoMoradia> listarPendentesPorMorador(Long moradorId);
}
