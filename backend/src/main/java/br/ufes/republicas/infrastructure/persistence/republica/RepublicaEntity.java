package br.ufes.republicas.infrastructure.persistence.republica;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representacao da republica no modelo de persistencia.
 */
@Entity
@Table(name = "republica")
public class RepublicaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "data_fundacao", nullable = false)
    private LocalDate dataFundacao;

    @Column(name = "data_extincao")
    private LocalDate dataExtincao;

    @Column(nullable = false, length = 200)
    private String logradouro;

    @Column(nullable = false, length = 9)
    private String cep;

    @Column(nullable = false, length = 100)
    private String bairro;

    @Column(name = "ponto_referencia", nullable = false, length = 200)
    private String pontoReferencia;

    @Column(name = "localizacao_geografica", length = 100)
    private String localizacaoGeografica;

    @Column(name = "codigo_etica", columnDefinition = "text")
    private String codigoEtica;

    @Column(nullable = false, columnDefinition = "text")
    private String vantagens;

    @Column(name = "despesas_medias_por_morador", nullable = false, precision = 12, scale = 2)
    private BigDecimal despesasMediasPorMorador;

    @Column(name = "total_vagas", nullable = false)
    private int totalVagas;

    @Column(name = "vagas_ocupadas", nullable = false)
    private int vagasOcupadas;

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

    public LocalDate getDataFundacao() {
        return dataFundacao;
    }

    public void setDataFundacao(LocalDate dataFundacao) {
        this.dataFundacao = dataFundacao;
    }

    public LocalDate getDataExtincao() {
        return dataExtincao;
    }

    public void setDataExtincao(LocalDate dataExtincao) {
        this.dataExtincao = dataExtincao;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public String getLocalizacaoGeografica() {
        return localizacaoGeografica;
    }

    public void setLocalizacaoGeografica(String localizacaoGeografica) {
        this.localizacaoGeografica = localizacaoGeografica;
    }

    public String getCodigoEtica() {
        return codigoEtica;
    }

    public void setCodigoEtica(String codigoEtica) {
        this.codigoEtica = codigoEtica;
    }

    public String getVantagens() {
        return vantagens;
    }

    public void setVantagens(String vantagens) {
        this.vantagens = vantagens;
    }

    public BigDecimal getDespesasMediasPorMorador() {
        return despesasMediasPorMorador;
    }

    public void setDespesasMediasPorMorador(BigDecimal despesasMediasPorMorador) {
        this.despesasMediasPorMorador = despesasMediasPorMorador;
    }

    public int getTotalVagas() {
        return totalVagas;
    }

    public void setTotalVagas(int totalVagas) {
        this.totalVagas = totalVagas;
    }

    public int getVagasOcupadas() {
        return vagasOcupadas;
    }

    public void setVagasOcupadas(int vagasOcupadas) {
        this.vagasOcupadas = vagasOcupadas;
    }
}
