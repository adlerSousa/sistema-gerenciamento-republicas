package br.ufes.republicas.infrastructure.persistence.financeiro;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio Spring Data para a entidade de persistencia da receita coletiva.
 */
public interface ReceitaColetivaJpaRepository extends JpaRepository<ReceitaColetivaEntity, Long> {

    Optional<ReceitaColetivaEntity> findByRepublicaId(Long republicaId);
}
