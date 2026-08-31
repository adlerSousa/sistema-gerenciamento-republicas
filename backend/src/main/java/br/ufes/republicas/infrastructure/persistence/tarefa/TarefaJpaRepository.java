package br.ufes.republicas.infrastructure.persistence.tarefa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio Spring Data para a entidade de persistencia da tarefa.
 */
public interface TarefaJpaRepository extends JpaRepository<TarefaEntity, Long> {

    List<TarefaEntity> findByRepublicaIdOrderByDataTerminoAsc(Long republicaId);

    @Query("""
            select tarefa
            from TarefaEntity tarefa
            join tarefa.responsaveis responsavel
            where responsavel = :moradorId
            order by tarefa.dataTermino asc
            """)
    List<TarefaEntity> buscarPorResponsavel(@Param("moradorId") Long moradorId);

    @Query("""
            select tarefa
            from TarefaEntity tarefa
            join tarefa.responsaveis responsavel
            where responsavel = :moradorId
              and tarefa.dataTermino between :inicio and :fim
            order by tarefa.dataTermino asc
            """)
    List<TarefaEntity> buscarPorResponsavelEPeriodo(@Param("moradorId") Long moradorId,
                                                    @Param("inicio") LocalDate inicio,
                                                    @Param("fim") LocalDate fim);
}
