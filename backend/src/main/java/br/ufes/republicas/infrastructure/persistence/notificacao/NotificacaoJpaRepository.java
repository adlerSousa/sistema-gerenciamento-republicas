package br.ufes.republicas.infrastructure.persistence.notificacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio Spring Data para a entidade de persistencia da notificacao.
 */
public interface NotificacaoJpaRepository extends JpaRepository<NotificacaoEntity, Long> {

    List<NotificacaoEntity> findByMoradorIdOrderByDataCriacaoDesc(Long moradorId);

    List<NotificacaoEntity> findByMoradorIdAndLidaFalseOrderByDataCriacaoDesc(Long moradorId);

    boolean existsByMoradorIdAndLancamentoId(Long moradorId, Long lancamentoId);
}
