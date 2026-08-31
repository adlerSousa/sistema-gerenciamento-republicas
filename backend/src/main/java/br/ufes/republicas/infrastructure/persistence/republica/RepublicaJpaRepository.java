package br.ufes.republicas.infrastructure.persistence.republica;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio Spring Data para a entidade de persistencia da republica.
 */
public interface RepublicaJpaRepository extends JpaRepository<RepublicaEntity, Long> {

    List<RepublicaEntity> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
}
