package br.ufes.republicas.infrastructure.persistence.reclamacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio Spring Data para a entidade de persistencia da reclamacao ou
 * sugestao.
 */
public interface ReclamacaoSugestaoJpaRepository extends JpaRepository<ReclamacaoSugestaoEntity, Long> {

    List<ReclamacaoSugestaoEntity> findByRepublicaIdOrderByDataRegistroDesc(Long republicaId);

    List<ReclamacaoSugestaoEntity> findByAutorIdOrderByDataRegistroDesc(Long autorId);

    @Query("""
            select reclamacao
            from ReclamacaoSugestaoEntity reclamacao
            join reclamacao.envolvidos envolvido
            where envolvido = :moradorId
              and reclamacao.dataRegistro between :inicio and :fim
              and reclamacao.excluida = false
            order by reclamacao.dataRegistro desc
            """)
    List<ReclamacaoSugestaoEntity> buscarPorEnvolvidoEPeriodo(@Param("moradorId") Long moradorId,
                                                              @Param("inicio") LocalDate inicio,
                                                              @Param("fim") LocalDate fim);
}
