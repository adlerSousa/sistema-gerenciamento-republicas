package br.ufes.republicas.infrastructure.persistence.republica;

import br.ufes.republicas.domain.republica.Republica;
import br.ufes.republicas.domain.republica.RepublicaRepositorio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementacao da porta de repositorio de republicas sobre JPA.
 */
@Repository
public class RepublicaRepositorioJpa implements RepublicaRepositorio {

    private final RepublicaJpaRepository repositorio;
    private final RepublicaMapper mapeador;

    public RepublicaRepositorioJpa(RepublicaJpaRepository repositorio, RepublicaMapper mapeador) {
        this.repositorio = repositorio;
        this.mapeador = mapeador;
    }

    @Override
    public Republica salvar(Republica republica) {
        RepublicaEntity entidade = republica.getId() == null
                ? mapeador.paraEntidade(republica)
                : atualizarExistente(republica);
        return mapeador.paraDominio(repositorio.save(entidade));
    }

    private RepublicaEntity atualizarExistente(Republica republica) {
        RepublicaEntity entidade = repositorio.findById(republica.getId())
                .orElseGet(RepublicaEntity::new);
        mapeador.aplicar(republica, entidade);
        return entidade;
    }

    @Override
    public Optional<Republica> buscarPorId(Long id) {
        return repositorio.findById(id).map(mapeador::paraDominio);
    }

    @Override
    public List<Republica> listarTodas() {
        return repositorio.findAll().stream().map(mapeador::paraDominio).toList();
    }

    @Override
    public List<Republica> listarPorNomeSemelhante(String nome) {
        return repositorio.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome).stream()
                .map(mapeador::paraDominio)
                .toList();
    }

    @Override
    public void remover(Long id) {
        repositorio.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return repositorio.existsById(id);
    }
}
