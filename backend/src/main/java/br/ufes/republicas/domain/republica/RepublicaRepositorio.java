package br.ufes.republicas.domain.republica;

import java.util.List;
import java.util.Optional;

/**
 * Porta de acesso ao repositorio de republicas.
 */
public interface RepublicaRepositorio {

    Republica salvar(Republica republica);

    Optional<Republica> buscarPorId(Long id);

    List<Republica> listarTodas();

    List<Republica> listarPorNomeSemelhante(String nome);

    void remover(Long id);

    boolean existePorId(Long id);
}
