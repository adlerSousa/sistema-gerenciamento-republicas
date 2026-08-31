package br.ufes.republicas.domain.morador;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Pessoa cadastrada no sistema (BR02).
 *
 * <p>Um morador nao pode residir em mais de uma republica ao mesmo tempo. Quando
 * nao esta vinculado a nenhuma republica, encontra-se na condicao de "sem teto"
 * (BR04).
 */
public class Morador {

    private Long id;
    private String nome;
    private String apelido;
    private String cpf;
    private String telefone;
    private String linkRedeSocial;
    private String telefoneResponsavelUm;
    private String telefoneResponsavelDois;
    private boolean perfilPublico;
    private Long republicaId;
    private LocalDate dataIngresso;
    private BigDecimal percentualRateio;

    protected Morador() {
        // Exigido pelos mecanismos de reconstrucao a partir da persistencia.
    }

    public Morador(String nome,
                   String apelido,
                   String cpf,
                   String telefone,
                   String telefoneResponsavelUm,
                   String telefoneResponsavelDois) {

        this.nome = Objects.requireNonNull(nome, "O nome do morador e obrigatorio.");
        this.apelido = Objects.requireNonNull(apelido, "O apelido do morador e obrigatorio.");
        this.cpf = Objects.requireNonNull(cpf, "O CPF do morador e obrigatorio.");
        this.telefone = Objects.requireNonNull(telefone, "O telefone do morador e obrigatorio.");
        this.telefoneResponsavelUm = Objects.requireNonNull(
                telefoneResponsavelUm, "O primeiro telefone de responsavel e obrigatorio.");
        this.telefoneResponsavelDois = Objects.requireNonNull(
                telefoneResponsavelDois, "O segundo telefone de responsavel e obrigatorio.");
        this.perfilPublico = true;
    }

    /**
     * Indica se o morador esta na condicao de "sem teto" (BR04), isto e, se nao
     * reside atualmente em nenhuma republica.
     */
    public boolean estaSemTeto() {
        return republicaId == null;
    }

    /**
     * Vincula o morador a uma republica.
     *
     * <p>A verificacao de disponibilidade de vaga e a atualizacao do quantitativo
     * de vagas ocupadas da republica sao responsabilidade do caso de uso que
     * coordena o ingresso.
     */
    public void ingressarEm(Long republicaId, LocalDate dataIngresso) {
        Objects.requireNonNull(republicaId, "A republica de destino e obrigatoria.");
        Objects.requireNonNull(dataIngresso, "A data de ingresso e obrigatoria.");

        if (!estaSemTeto()) {
            throw new RegraDeNegocioException(
                    "O morador ja reside em uma republica e nao pode residir em mais de uma ao mesmo tempo.");
        }

        this.republicaId = republicaId;
        this.dataIngresso = dataIngresso;
    }

    /**
     * Desvincula o morador da republica em que reside, retornando-o a condicao de
     * "sem teto".
     */
    public void sairDaRepublica() {
        if (estaSemTeto()) {
            throw new RegraDeNegocioException("O morador nao reside em nenhuma republica.");
        }
        this.republicaId = null;
        this.dataIngresso = null;
        this.percentualRateio = null;
    }

    public void alterarPerfil(String nome,
                              String apelido,
                              String telefone,
                              String linkRedeSocial,
                              String telefoneResponsavelUm,
                              String telefoneResponsavelDois,
                              boolean perfilPublico) {
        this.nome = Objects.requireNonNull(nome, "O nome do morador e obrigatorio.");
        this.apelido = Objects.requireNonNull(apelido, "O apelido do morador e obrigatorio.");
        this.telefone = Objects.requireNonNull(telefone, "O telefone do morador e obrigatorio.");
        this.linkRedeSocial = linkRedeSocial;
        this.telefoneResponsavelUm = Objects.requireNonNull(
                telefoneResponsavelUm, "O primeiro telefone de responsavel e obrigatorio.");
        this.telefoneResponsavelDois = Objects.requireNonNull(
                telefoneResponsavelDois, "O segundo telefone de responsavel e obrigatorio.");
        this.perfilPublico = perfilPublico;
    }

    public void definirPercentualRateio(BigDecimal percentualRateio) {
        if (percentualRateio != null
                && (percentualRateio.compareTo(BigDecimal.ZERO) < 0
                    || percentualRateio.compareTo(new BigDecimal("100")) > 0)) {
            throw new RegraDeNegocioException(
                    "O percentual de rateio deve estar entre 0 e 100.");
        }
        this.percentualRateio = percentualRateio;
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

    public String getApelido() {
        return apelido;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getLinkRedeSocial() {
        return linkRedeSocial;
    }

    public void definirLinkRedeSocial(String linkRedeSocial) {
        this.linkRedeSocial = linkRedeSocial;
    }

    public String getTelefoneResponsavelUm() {
        return telefoneResponsavelUm;
    }

    public String getTelefoneResponsavelDois() {
        return telefoneResponsavelDois;
    }

    public boolean isPerfilPublico() {
        return perfilPublico;
    }

    public Long getRepublicaId() {
        return republicaId;
    }

    public void definirRepublicaId(Long republicaId) {
        this.republicaId = republicaId;
    }

    public LocalDate getDataIngresso() {
        return dataIngresso;
    }

    public void definirDataIngresso(LocalDate dataIngresso) {
        this.dataIngresso = dataIngresso;
    }

    public BigDecimal getPercentualRateio() {
        return percentualRateio;
    }
}
