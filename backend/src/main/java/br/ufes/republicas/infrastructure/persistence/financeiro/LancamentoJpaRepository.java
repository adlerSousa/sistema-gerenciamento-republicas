package br.ufes.republicas.infrastructure.persistence.financeiro;

import br.ufes.republicas.domain.financeiro.StatusLancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio Spring Data para a entidade de persistencia do lancamento.
 */
public interface LancamentoJpaRepository extends JpaRepository<LancamentoEntity, Long> {

    List<LancamentoEntity> findByRepublicaIdOrderByDataVencimentoAsc(Long republicaId);

    List<LancamentoEntity> findByRepublicaIdAndDataVencimentoBetweenOrderByDataVencimentoAsc(
            Long republicaId, LocalDate inicio, LocalDate fim);

    List<LancamentoEntity> findByRepublicaIdAndStatusAndDataVencimentoLessThanEqualOrderByDataVencimentoAsc(
            Long republicaId, StatusLancamento status, LocalDate limite);

    @Query("""
            select distinct lancamento
            from LancamentoEntity lancamento
            join lancamento.participacoes participacao
            where participacao.moradorId = :moradorId
            order by lancamento.dataVencimento asc
            """)
    List<LancamentoEntity> buscarPorMoradorParticipante(@Param("moradorId") Long moradorId);
}
