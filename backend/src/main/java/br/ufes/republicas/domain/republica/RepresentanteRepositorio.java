package br.ufes.republicas.domain.republica;

import java.util.List;
import java.util.Optional;

/**
 * Porta de acesso ao repositorio de representantes de republica.
 */
public interface RepresentanteRepositorio {

    Representante salvar(Representante representante);

    Optional<Representante> buscarVigentePorRepublica(Long republicaId);

    List<Representante> listarPorRepublica(Long republicaId);

    List<Representante> listarPorMorador(Long moradorId);
}
