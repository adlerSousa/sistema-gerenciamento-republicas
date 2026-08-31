package br.ufes.republicas.infrastructure.persistence.morador;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representacao do morador no modelo de persistencia.
 */
@Entity
@Table(name = "morador")
public class MoradorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 80)
    private String apelido;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(name = "link_rede_social", length = 255)
    private String linkRedeSocial;

    @Column(name = "telefone_responsavel_um", nullable = false, length = 20)
    private String telefoneResponsavelUm;

    @Column(name = "telefone_responsavel_dois", nullable = false, length = 20)
    private String telefoneResponsavelDois;

    @Column(name = "perfil_publico", nullable = false)
    private boolean perfilPublico;

    @Column(name = "republica_id")
    private Long republicaId;

    @Column(name = "data_ingresso")
    private LocalDate dataIngresso;

    @Column(name = "percentual_rateio", precision = 5, scale = 2)
    private BigDecimal percentualRateio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getLinkRedeSocial() {
        return linkRedeSocial;
    }

    public void setLinkRedeSocial(String linkRedeSocial) {
        this.linkRedeSocial = linkRedeSocial;
    }

    public String getTelefoneResponsavelUm() {
        return telefoneResponsavelUm;
    }

    public void setTelefoneResponsavelUm(String telefoneResponsavelUm) {
        this.telefoneResponsavelUm = telefoneResponsavelUm;
    }

    public String getTelefoneResponsavelDois() {
        return telefoneResponsavelDois;
    }

    public void setTelefoneResponsavelDois(String telefoneResponsavelDois) {
        this.telefoneResponsavelDois = telefoneResponsavelDois;
    }

    public boolean isPerfilPublico() {
        return perfilPublico;
    }

    public void setPerfilPublico(boolean perfilPublico) {
        this.perfilPublico = perfilPublico;
    }

    public Long getRepublicaId() {
        return republicaId;
    }

    public void setRepublicaId(Long republicaId) {
        this.republicaId = republicaId;
    }

    public LocalDate getDataIngresso() {
        return dataIngresso;
    }

    public void setDataIngresso(LocalDate dataIngresso) {
        this.dataIngresso = dataIngresso;
    }

    public BigDecimal getPercentualRateio() {
        return percentualRateio;
    }

    public void setPercentualRateio(BigDecimal percentualRateio) {
        this.percentualRateio = percentualRateio;
    }
}
