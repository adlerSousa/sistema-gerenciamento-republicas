package br.ufes.republicas.domain.financeiro;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Lancamento de receita ou despesa de uma republica (BR06).
 *
 * <p>Um lancamento repetitivo da origem a lancamentos derivados, que reproduzem
 * os mesmos dados e recebem um numero sequencial na descricao indicando a
 * parcela.
 */
public class Lancamento {

    private Long id;
    private Long republicaId;
    private TipoLancamento tipo;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private LocalDate dataCadastro;
    private Periodicidade periodicidade;
    private FormaRateio formaRateio;
    private Integer numeroParcela;
    private Integer totalParcelas;
    private Long lancamentoOrigemId;
    private StatusLancamento status;
    private String justificativaEstorno;
    private final List<ParticipacaoLancamento> participacoes = new ArrayList<>();

    protected Lancamento() {
        // Exigido pelos mecanismos de reconstrucao a partir da persistencia.
    }

    public Lancamento(Long republicaId,
                      TipoLancamento tipo,
                      String descricao,
                      BigDecimal valor,
                      LocalDate dataVencimento,
                      LocalDate dataCadastro,
                      Periodicidade periodicidade,
                      FormaRateio formaRateio) {

        this.republicaId = Objects.requireNonNull(republicaId, "A republica e obrigatoria.");
        this.tipo = Objects.requireNonNull(tipo, "O tipo do lancamento e obrigatorio.");
        this.descricao = Objects.requireNonNull(descricao, "A descricao e obrigatoria.");
        this.valor = Objects.requireNonNull(valor, "O valor e obrigatorio.");
        this.dataVencimento = Objects.requireNonNull(dataVencimento, "A data de vencimento e obrigatoria.");
        this.dataCadastro = Objects.requireNonNull(dataCadastro, "A data de cadastro e obrigatoria.");
        this.periodicidade = Objects.requireNonNull(periodicidade, "A periodicidade e obrigatoria.");
        this.formaRateio = Objects.requireNonNull(formaRateio, "A forma de rateio e obrigatoria.");

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("O valor do lancamento deve ser positivo.");
        }

        this.status = StatusLancamento.PENDENTE;
    }

    /**
     * Indica se o lancamento vence dentro do numero de dias informado, contado a
     * partir da data de referencia. Lancamentos ja vencidos permanecem
     * sinalizados (RF01).
     */
    public boolean venceAte(LocalDate dataReferencia, int diasDeAntecedencia) {
        Objects.requireNonNull(dataReferencia, "A data de referencia e obrigatoria.");
        if (status != StatusLancamento.PENDENTE) {
            return false;
        }
        return !dataVencimento.isAfter(dataReferencia.plusDays(diasDeAntecedencia));
    }

    public boolean estaVencido(LocalDate dataReferencia) {
        Objects.requireNonNull(dataReferencia, "A data de referencia e obrigatoria.");
        return status == StatusLancamento.PENDENTE && dataVencimento.isBefore(dataReferencia);
    }

    public void adicionarParticipacao(ParticipacaoLancamento participacao) {
        Objects.requireNonNull(participacao, "A participacao e obrigatoria.");
        boolean moradorJaParticipa = participacoes.stream()
                .anyMatch(existente -> existente.getMoradorId().equals(participacao.getMoradorId()));
        if (moradorJaParticipa) {
            throw new RegraDeNegocioException(
                    "O morador ja consta como participante deste lancamento.");
        }
        participacoes.add(participacao);
    }

    /**
     * Marca o lancamento como pago quando todas as participacoes estiverem
     * quitadas.
     */
    public void atualizarStatusConformeParticipacoes() {
        if (status == StatusLancamento.ESTORNADO) {
            return;
        }
        boolean todasPagas = !participacoes.isEmpty()
                && participacoes.stream().allMatch(ParticipacaoLancamento::isPago);
        this.status = todasPagas ? StatusLancamento.PAGO : StatusLancamento.PENDENTE;
    }

    public void estornar(String justificativa) {
        Objects.requireNonNull(justificativa, "A justificativa do estorno e obrigatoria.");
        if (status == StatusLancamento.ESTORNADO) {
            throw new RegraDeNegocioException("O lancamento ja foi estornado.");
        }
        this.status = StatusLancamento.ESTORNADO;
        this.justificativaEstorno = justificativa;
    }

    public BigDecimal totalDevido() {
        return participacoes.stream()
                .map(ParticipacaoLancamento::getValorDevido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public void definirId(Long id) {
        this.id = id;
    }

    public Long getRepublicaId() {
        return republicaId;
    }

    public TipoLancamento getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public Periodicidade getPeriodicidade() {
        return periodicidade;
    }

    public FormaRateio getFormaRateio() {
        return formaRateio;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public Integer getTotalParcelas() {
        return totalParcelas;
    }

    public void definirParcela(Integer numeroParcela, Integer totalParcelas) {
        this.numeroParcela = numeroParcela;
        this.totalParcelas = totalParcelas;
    }

    public Long getLancamentoOrigemId() {
        return lancamentoOrigemId;
    }

    public void definirLancamentoOrigemId(Long lancamentoOrigemId) {
        this.lancamentoOrigemId = lancamentoOrigemId;
    }

    public StatusLancamento getStatus() {
        return status;
    }

    public void definirStatus(StatusLancamento status) {
        this.status = Objects.requireNonNull(status, "O status e obrigatorio.");
    }

    public String getJustificativaEstorno() {
        return justificativaEstorno;
    }

    public void definirDescricao(String descricao) {
        this.descricao = Objects.requireNonNull(descricao, "A descricao e obrigatoria.");
    }

    public List<ParticipacaoLancamento> getParticipacoes() {
        return Collections.unmodifiableList(participacoes);
    }
}
