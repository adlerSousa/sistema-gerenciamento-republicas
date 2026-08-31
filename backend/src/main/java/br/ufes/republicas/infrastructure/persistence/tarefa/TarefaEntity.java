package br.ufes.republicas.infrastructure.persistence.tarefa;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Representacao da tarefa domestica no modelo de persistencia.
 */
@Entity
@Table(name = "tarefa")
public class TarefaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "republica_id", nullable = false)
    private Long republicaId;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDate dataAgendamento;

    @Column(name = "data_termino", nullable = false)
    private LocalDate dataTermino;

    @Column(nullable = false)
    private boolean finalizada;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;

    @Column(name = "descricao_conclusao", length = 500)
    private String descricaoConclusao;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tarefa_responsavel",
            joinColumns = @JoinColumn(name = "tarefa_id"))
    @Column(name = "morador_id", nullable = false)
    private Set<Long> responsaveis = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRepublicaId() {
        return republicaId;
    }

    public void setRepublicaId(Long republicaId) {
        this.republicaId = republicaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public String getDescricaoConclusao() {
        return descricaoConclusao;
    }

    public void setDescricaoConclusao(String descricaoConclusao) {
        this.descricaoConclusao = descricaoConclusao;
    }

    public Set<Long> getResponsaveis() {
        return responsaveis;
    }

    public void setResponsaveis(Set<Long> responsaveis) {
        this.responsaveis = responsaveis;
    }
}
