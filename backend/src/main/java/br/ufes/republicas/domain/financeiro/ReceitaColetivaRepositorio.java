package br.ufes.republicas.domain.financeiro;

import java.util.Optional;

/**
 * Porta de acesso ao repositorio da receita coletiva.
 */
public interface ReceitaColetivaRepositorio {

    ReceitaColetiva salvar(ReceitaColetiva receitaColetiva);

    Optional<ReceitaColetiva> buscarPorRepublica(Long republicaId);
}
