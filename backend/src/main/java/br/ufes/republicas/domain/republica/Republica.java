package br.ufes.republicas.domain.republica;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Moradia dividida por estudantes (BR01).
 *
 * <p>O controle de vagas segue a regra BR07: as vagas disponiveis correspondem a
 * diferenca entre o total de vagas e as vagas ocupadas.
 */
public class Republica {

    private Long id;
    private String nome;
    private LocalDate dataFundacao;
    private LocalDate dataExtincao;
    private Endereco endereco;
    private String codigoEtica;
    private String vantagens;
    private BigDecimal despesasMediasPorMorador;
    private int totalVagas;
    private int vagasOcupadas;

    protected Republica() {
        // Exigido pelos mecanismos de reconstrucao a partir da persistencia.
    }

    public Republica(String nome,
                     LocalDate dataFundacao,
                     Endereco endereco,
                     String vantagens,
                     BigDecimal despesasMediasPorMorador,
                     int totalVagas,
                     int vagasOcupadas) {

        this.nome = Objects.requireNonNull(nome, "O nome da republica e obrigatorio.");
        this.dataFundacao = Objects.requireNonNull(dataFundacao, "A data de fundacao e obrigatoria.");
        this.endereco = Objects.requireNonNull(endereco, "O endereco e obrigatorio.");
        this.vantagens = Objects.requireNonNull(vantagens, "As vantagens sao obrigatorias.");
        this.despesasMediasPorMorador = Objects.requireNonNull(
                despesasMediasPorMorador, "As despesas medias por morador sao obrigatorias.");

        if (totalVagas < 0) {
            throw new RegraDeNegocioException("O total de vagas nao pode ser negativo.");
        }
        if (vagasOcupadas < 0) {
            throw new RegraDeNegocioException("O numero de vagas ocupadas nao pode ser negativo.");
        }
        if (vagasOcupadas > totalVagas) {
            throw new RegraDeNegocioException(
                    "O numero de vagas ocupadas nao pode ser superior ao total de vagas.");
        }

        this.totalVagas = totalVagas;
        this.vagasOcupadas = vagasOcupadas;
    }

    /**
     * Vagas disponiveis para ocupacao (BR07): VD = TV - VO.
     */
    public int vagasDisponiveis() {
        return totalVagas - vagasOcupadas;
    }

    public boolean estaExtinta() {
        return dataExtincao != null;
    }

    public void alterarDadosCadastrais(String nome,
                                       Endereco endereco,
                                       String vantagens,
                                       String codigoEtica,
                                       BigDecimal despesasMediasPorMorador) {
        this.nome = Objects.requireNonNull(nome, "O nome da republica e obrigatorio.");
        this.endereco = Objects.requireNonNull(endereco, "O endereco e obrigatorio.");
        this.vantagens = Objects.requireNonNull(vantagens, "As vantagens sao obrigatorias.");
        this.codigoEtica = codigoEtica;
        this.despesasMediasPorMorador = Objects.requireNonNull(
                despesasMediasPorMorador, "As despesas medias por morador sao obrigatorias.");
    }

    public void alterarTotalDeVagas(int novoTotal) {
        if (novoTotal < 0) {
            throw new RegraDeNegocioException("O total de vagas nao pode ser negativo.");
        }
        if (novoTotal < vagasOcupadas) {
            throw new RegraDeNegocioException(
                    "O total de vagas nao pode ser inferior ao numero de vagas ocupadas.");
        }
        this.totalVagas = novoTotal;
    }

    public void extinguir(LocalDate dataExtincao) {
        this.dataExtincao = Objects.requireNonNull(dataExtincao, "A data de extincao e obrigatoria.");
    }

    public Long getId() {
        return id;
    }

    public void definirId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataFundacao() {
        return dataFundacao;
    }

    public LocalDate getDataExtincao() {
        return dataExtincao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getCodigoEtica() {
        return codigoEtica;
    }

    public void definirCodigoEtica(String codigoEtica) {
        this.codigoEtica = codigoEtica;
    }

    public String getVantagens() {
        return vantagens;
    }

    public BigDecimal getDespesasMediasPorMorador() {
        return despesasMediasPorMorador;
    }

    public int getTotalVagas() {
        return totalVagas;
    }

    public int getVagasOcupadas() {
        return vagasOcupadas;
    }

    public void definirVagasOcupadas(int vagasOcupadas) {
        if (vagasOcupadas < 0) {
            throw new RegraDeNegocioException("O numero de vagas ocupadas nao pode ser negativo.");
        }
        this.vagasOcupadas = vagasOcupadas;
    }
}
