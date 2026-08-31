package br.ufes.republicas.infrastructure.persistence.reclamacao;

import br.ufes.republicas.domain.reclamacao.TipoReclamacao;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Representacao da reclamacao ou sugestao no modelo de persistencia.
 */
@Entity
@Table(name = "reclamacao_sugestao")
public class ReclamacaoSugestaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "republica_id", nullable = false)
    private Long republicaId;

    @Column(name = "autor_id", nullable = false)
    private Long autorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoReclamacao tipo;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @Column(name = "data_solucao")
    private LocalDate dataSolucao;

    @Column(nullable = false)
    private boolean resolvida;

    @Column(name = "solucao_confirmada", nullable = false)
    private boolean solucaoConfirmada;

    @Column(nullable = false)
    private boolean excluida;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "reclamacao_envolvido",
            joinColumns = @JoinColumn(name = "reclamacao_id"))
    @Column(name = "morador_id", nullable = false)
    private Set<Long> envolvidos = new LinkedHashSet<>();

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

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public TipoReclamacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoReclamacao tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public LocalDate getDataSolucao() {
        return dataSolucao;
    }

    public void setDataSolucao(LocalDate dataSolucao) {
        this.dataSolucao = dataSolucao;
    }

    public boolean isResolvida() {
        return resolvida;
    }

    public void setResolvida(boolean resolvida) {
        this.resolvida = resolvida;
    }

    public boolean isSolucaoConfirmada() {
        return solucaoConfirmada;
    }

    public void setSolucaoConfirmada(boolean solucaoConfirmada) {
        this.solucaoConfirmada = solucaoConfirmada;
    }

    public boolean isExcluida() {
        return excluida;
    }

    public void setExcluida(boolean excluida) {
        this.excluida = excluida;
    }

    public Set<Long> getEnvolvidos() {
        return envolvidos;
    }

    public void setEnvolvidos(Set<Long> envolvidos) {
        this.envolvidos = envolvidos;
    }
}
