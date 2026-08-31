package br.ufes.republicas.infrastructure.persistence.republica;

import br.ufes.republicas.domain.republica.Representante;
import br.ufes.republicas.domain.republica.RepresentanteRepositorio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementacao da porta de repositorio de representantes sobre JPA.
 */
@Repository
public class RepresentanteRepositorioJpa implements RepresentanteRepositorio {

    private final RepresentanteJpaRepository repositorio;

    public RepresentanteRepositorioJpa(RepresentanteJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Representante salvar(Representante representante) {
        RepresentanteEntity entidade = representante.getId() == null
                ? new RepresentanteEntity()
                : repositorio.findById(representante.getId()).orElseGet(RepresentanteEntity::new);

        entidade.setId(representante.getId());
        entidade.setRepublicaId(representante.getRepublicaId());
        entidade.setMoradorId(representante.getMoradorId());
        entidade.setDataInicio(representante.getDataInicio());
        entidade.setDataFim(representante.getDataFim());

        return paraDominio(repositorio.save(entidade));
    }

    @Override
    public Optional<Representante> buscarVigentePorRepublica(Long republicaId) {
        return repositorio.findByRepublicaIdAndDataFimIsNull(republicaId).map(this::paraDominio);
    }

    @Override
    public List<Representante> listarPorRepublica(Long republicaId) {
        return repositorio.findByRepublicaIdOrderByDataInicioDesc(republicaId).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<Representante> listarPorMorador(Long moradorId) {
        return repositorio.findByMoradorIdOrderByDataInicioDesc(moradorId).stream()
                .map(this::paraDominio)
                .toList();
    }

    private Representante paraDominio(RepresentanteEntity entidade) {
        Representante representante = new Representante(
                entidade.getRepublicaId(), entidade.getMoradorId(), entidade.getDataInicio());
        representante.definirId(entidade.getId());
        if (entidade.getDataFim() != null) {
            representante.encerrarMandato(entidade.getDataFim());
        }
        return representante;
    }
}
