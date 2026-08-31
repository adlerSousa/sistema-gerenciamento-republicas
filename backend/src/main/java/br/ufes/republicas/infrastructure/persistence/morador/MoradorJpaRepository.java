package br.ufes.republicas.infrastructure.persistence.morador;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data para a entidade de persistencia do morador.
 */
public interface MoradorJpaRepository extends JpaRepository<MoradorEntity, Long> {

    Optional<MoradorEntity> findByCpf(String cpf);

    List<MoradorEntity> findByRepublicaIdOrderByNomeAsc(Long republicaId);

    List<MoradorEntity> findByRepublicaIdIsNullOrderByNomeAsc();
}
