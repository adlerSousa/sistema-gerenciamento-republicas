package br.ufes.republicas.infrastructure.persistence.financeiro;

import br.ufes.republicas.domain.financeiro.FormaRateio;
import br.ufes.republicas.domain.financeiro.Periodicidade;
import br.ufes.republicas.domain.financeiro.StatusLancamento;
import br.ufes.republicas.domain.financeiro.TipoLancamento;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representacao do lancamento financeiro no modelo de persistencia.
 */
@Entity
@Table(name = "lancamento")
public class LancamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "republica_id", nullable = false)
    private Long republicaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoLancamento tipo;

    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Periodicidade periodicidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_rateio", nullable = false, length = 20)
    private FormaRateio formaRateio;

    @Column(name = "numero_parcela")
    private Integer numeroParcela;

    @Column(name = "total_parcelas")
    private Integer totalParcelas;

    @Column(name = "lancamento_origem_id")
    private Long lancamentoOrigemId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusLancamento status;

    @Column(name = "justificativa_estorno", columnDefinition = "text")
    private String justificativaEstorno;

    @OneToMany(mappedBy = "lancamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipacaoLancamentoEntity> participacoes = new ArrayList<>();

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

    public TipoLancamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoLancamento tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Periodicidade getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(Periodicidade periodicidade) {
        this.periodicidade = periodicidade;
    }

    public FormaRateio getFormaRateio() {
        return formaRateio;
    }

    public void setFormaRateio(FormaRateio formaRateio) {
        this.formaRateio = formaRateio;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Integer getTotalParcelas() {
        return totalParcelas;
    }

    public void setTotalParcelas(Integer totalParcelas) {
        this.totalParcelas = totalParcelas;
    }

    public Long getLancamentoOrigemId() {
        return lancamentoOrigemId;
    }

    public void setLancamentoOrigemId(Long lancamentoOrigemId) {
        this.lancamentoOrigemId = lancamentoOrigemId;
    }

    public StatusLancamento getStatus() {
        return status;
    }

    public void setStatus(StatusLancamento status) {
        this.status = status;
    }

    public String getJustificativaEstorno() {
        return justificativaEstorno;
    }

    public void setJustificativaEstorno(String justificativaEstorno) {
        this.justificativaEstorno = justificativaEstorno;
    }

    public List<ParticipacaoLancamentoEntity> getParticipacoes() {
        return participacoes;
    }

    public void setParticipacoes(List<ParticipacaoLancamentoEntity> participacoes) {
        this.participacoes = participacoes;
    }
}
