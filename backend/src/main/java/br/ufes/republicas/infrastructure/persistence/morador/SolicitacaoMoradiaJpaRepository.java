package br.ufes.republicas.infrastructure.persistence.morador;

import br.ufes.republicas.domain.morador.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio Spring Data para a entidade de persistencia da solicitacao de
 * moradia.
 */
public interface SolicitacaoMoradiaJpaRepository extends JpaRepository<SolicitacaoMoradiaEntity, Long> {

    List<SolicitacaoMoradiaEntity> findByRepublicaIdAndStatusOrderByDataRegistroAsc(
            Long republicaId, StatusSolicitacao status);

    List<SolicitacaoMoradiaEntity> findByMoradorIdAndStatusOrderByDataRegistroAsc(
            Long moradorId, StatusSolicitacao status);
}
