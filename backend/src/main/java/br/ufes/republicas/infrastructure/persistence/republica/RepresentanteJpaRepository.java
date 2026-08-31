package br.ufes.republicas.infrastructure.persistence.republica;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data para a entidade de persistencia do representante.
 */
public interface RepresentanteJpaRepository extends JpaRepository<RepresentanteEntity, Long> {

    Optional<RepresentanteEntity> findByRepublicaIdAndDataFimIsNull(Long republicaId);

    List<RepresentanteEntity> findByRepublicaIdOrderByDataInicioDesc(Long republicaId);

    List<RepresentanteEntity> findByMoradorIdOrderByDataInicioDesc(Long moradorId);
}
