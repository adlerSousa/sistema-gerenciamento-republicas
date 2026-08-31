package br.ufes.republicas.infrastructure.persistence.financeiro;

import br.ufes.republicas.domain.financeiro.ReceitaColetiva;
import br.ufes.republicas.domain.financeiro.ReceitaColetivaRepositorio;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementacao da porta de repositorio da receita coletiva sobre JPA.
 */
@Repository
public class ReceitaColetivaRepositorioJpa implements ReceitaColetivaRepositorio {

    private final ReceitaColetivaJpaRepository repositorio;

    public ReceitaColetivaRepositorioJpa(ReceitaColetivaJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public ReceitaColetiva salvar(ReceitaColetiva receitaColetiva) {
        ReceitaColetivaEntity entidade = receitaColetiva.getId() == null
                ? new ReceitaColetivaEntity()
                : repositorio.findById(receitaColetiva.getId()).orElseGet(ReceitaColetivaEntity::new);

        entidade.setId(receitaColetiva.getId());
        entidade.setRepublicaId(receitaColetiva.getRepublicaId());
        entidade.setSaldo(receitaColetiva.getSaldo());

        return paraDominio(repositorio.save(entidade));
    }

    @Override
    public Optional<ReceitaColetiva> buscarPorRepublica(Long republicaId) {
        return repositorio.findByRepublicaId(republicaId).map(this::paraDominio);
    }

    private ReceitaColetiva paraDominio(ReceitaColetivaEntity entidade) {
        ReceitaColetiva receitaColetiva = new ReceitaColetiva(entidade.getRepublicaId());
        receitaColetiva.definirId(entidade.getId());
        receitaColetiva.definirSaldo(entidade.getSaldo());
        return receitaColetiva;
    }
}
