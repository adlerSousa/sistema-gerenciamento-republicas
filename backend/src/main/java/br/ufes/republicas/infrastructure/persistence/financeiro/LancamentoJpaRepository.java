package br.ufes.republicas.infrastructure.persistence.financeiro;

import br.ufes.republicas.domain.financeiro.StatusLancamento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data para a entidade de persistencia do lancamento.
 *
 * <p>As participacoes no rateio integram o lancamento e sao sempre necessarias
 * na conversao para o dominio. Por isso, todas as consultas as carregam junto
 * com o lancamento, evitando tanto o acesso a colecao fora da sessao quanto a
 * emissao de uma consulta adicional por lancamento retornado.
 */
public interface LancamentoJpaRepository extends JpaRepository<LancamentoEntity, Long> {

    @Override
    @EntityGraph(attributePaths = "participacoes")
    Optional<LancamentoEntity> findById(Long id);

    @EntityGraph(attributePaths = "participacoes")
    List<LancamentoEntity> findByRepublicaIdOrderByDataVencimentoAsc(Long republicaId);

    @EntityGraph(attributePaths = "participacoes")
    List<LancamentoEntity> findByRepublicaIdAndDataVencimentoBetweenOrderByDataVencimentoAsc(
            Long republicaId, LocalDate inicio, LocalDate fim);

    @EntityGraph(attributePaths = "participacoes")
    List<LancamentoEntity> findByRepublicaIdAndStatusAndDataVencimentoLessThanEqualOrderByDataVencimentoAsc(
            Long republicaId, StatusLancamento status, LocalDate limite);

    @EntityGraph(attributePaths = "participacoes")
    @Query("""
            select distinct lancamento
            from LancamentoEntity lancamento
            join lancamento.participacoes participacao
            where participacao.moradorId = :moradorId
            order by lancamento.dataVencimento asc
            """)
    List<LancamentoEntity> buscarPorMoradorParticipante(@Param("moradorId") Long moradorId);
}
