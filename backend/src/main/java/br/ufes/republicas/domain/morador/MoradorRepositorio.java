package br.ufes.republicas.domain.morador;

import java.util.List;
import java.util.Optional;

/**
 * Porta de acesso ao repositorio de moradores.
 */
public interface MoradorRepositorio {

    Morador salvar(Morador morador);

    Optional<Morador> buscarPorId(Long id);

    Optional<Morador> buscarPorCpf(String cpf);

    List<Morador> listarPorRepublica(Long republicaId);

    List<Morador> listarSemTeto();

    List<Morador> listarTodos();

    void remover(Long id);
}
