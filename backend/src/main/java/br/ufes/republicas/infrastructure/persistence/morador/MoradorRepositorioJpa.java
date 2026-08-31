package br.ufes.republicas.infrastructure.persistence.morador;

import br.ufes.republicas.domain.morador.Morador;
import br.ufes.republicas.domain.morador.MoradorRepositorio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementacao da porta de repositorio de moradores sobre JPA.
 */
@Repository
public class MoradorRepositorioJpa implements MoradorRepositorio {

    private final MoradorJpaRepository repositorio;
    private final MoradorMapper mapeador;

    public MoradorRepositorioJpa(MoradorJpaRepository repositorio, MoradorMapper mapeador) {
        this.repositorio = repositorio;
        this.mapeador = mapeador;
    }

    @Override
    public Morador salvar(Morador morador) {
        MoradorEntity entidade = morador.getId() == null
                ? mapeador.paraEntidade(morador)
                : atualizarExistente(morador);
        return mapeador.paraDominio(repositorio.save(entidade));
    }

    private MoradorEntity atualizarExistente(Morador morador) {
        MoradorEntity entidade = repositorio.findById(morador.getId())
                .orElseGet(MoradorEntity::new);
        mapeador.aplicar(morador, entidade);
        return entidade;
    }

    @Override
    public Optional<Morador> buscarPorId(Long id) {
        return repositorio.findById(id).map(mapeador::paraDominio);
    }

    @Override
    public Optional<Morador> buscarPorCpf(String cpf) {
        return repositorio.findByCpf(cpf).map(mapeador::paraDominio);
    }

    @Override
    public List<Morador> listarPorRepublica(Long republicaId) {
        return repositorio.findByRepublicaIdOrderByNomeAsc(republicaId).stream()
                .map(mapeador::paraDominio)
                .toList();
    }

    @Override
    public List<Morador> listarSemTeto() {
        return repositorio.findByRepublicaIdIsNullOrderByNomeAsc().stream()
                .map(mapeador::paraDominio)
                .toList();
    }

    @Override
    public List<Morador> listarTodos() {
        return repositorio.findAll().stream().map(mapeador::paraDominio).toList();
    }

    @Override
    public void remover(Long id) {
        repositorio.deleteById(id);
    }
}
