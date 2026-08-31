package br.ufes.republicas.domain.reclamacao;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Reclamacao ou sugestao registrada por um morador da republica (BR09).
 */
public class ReclamacaoSugestao {

    private Long id;
    private Long republicaId;
    private Long autorId;
    private TipoReclamacao tipo;
    private String descricao;
    private LocalDate dataRegistro;
    private LocalDate dataSolucao;
    private boolean resolvida;
    private boolean solucaoConfirmada;
    private boolean excluida;
    private final Set<Long> envolvidos = new LinkedHashSet<>();

    protected ReclamacaoSugestao() {
        // Exigido pelos mecanismos de reconstrucao a partir da persistencia.
    }

    public ReclamacaoSugestao(Long republicaId,
                              Long autorId,
                              TipoReclamacao tipo,
                              String descricao,
                              LocalDate dataRegistro,
                              List<Long> envolvidos) {

        this.republicaId = Objects.requireNonNull(republicaId, "A republica e obrigatoria.");
        this.autorId = Objects.requireNonNull(autorId, "O autor e obrigatorio.");
        this.tipo = Objects.requireNonNull(tipo, "O tipo do registro e obrigatorio.");
        this.descricao = Objects.requireNonNull(descricao, "A descricao e obrigatoria.");
        this.dataRegistro = Objects.requireNonNull(dataRegistro, "A data de registro e obrigatoria.");

        if (envolvidos != null) {
            this.envolvidos.addAll(envolvidos);
        }
    }

    /**
     * Marca o registro como resolvido. Apenas o morador que o registrou pode
     * realizar essa marcacao (UC6).
     */
    public void marcarComoResolvida(LocalDate dataSolucao) {
        Objects.requireNonNull(dataSolucao, "A data de solucao e obrigatoria.");
        if (excluida) {
            throw new RegraDeNegocioException("O registro foi excluido e nao pode ser resolvido.");
        }
        if (resolvida) {
            throw new RegraDeNegocioException("O registro ja esta resolvido.");
        }
        this.resolvida = true;
        this.dataSolucao = dataSolucao;
    }

    /**
     * Confirma a solucao apontada. Apenas o representante da republica pode
     * confirmar (UC6).
     */
    public void confirmarSolucao() {
        if (!resolvida) {
            throw new RegraDeNegocioException(
                    "O registro precisa estar resolvido para que a solucao seja confirmada.");
        }
        if (solucaoConfirmada) {
            throw new RegraDeNegocioException("A solucao ja esta confirmada.");
        }
        this.solucaoConfirmada = true;
    }

    public void excluir() {
        this.excluida = true;
    }

    public void alterarDescricao(String descricao) {
        this.descricao = Objects.requireNonNull(descricao, "A descricao e obrigatoria.");
    }

    /**
     * Idade do registro em dias, isto e, quanto tempo levou para ser solucionado
     * (BR09).
     */
    public Long idadeEmDias() {
        if (dataSolucao == null) {
            return null;
        }
        return ChronoUnit.DAYS.between(dataRegistro, dataSolucao);
    }

    public boolean envolve(Long moradorId) {
        return envolvidos.contains(moradorId);
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

    public Long getAutorId() {
        return autorId;
    }

    public TipoReclamacao getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public LocalDate getDataSolucao() {
        return dataSolucao;
    }

    public boolean isResolvida() {
        return resolvida;
    }

    public boolean isSolucaoConfirmada() {
        return solucaoConfirmada;
    }

    public boolean isExcluida() {
        return excluida;
    }

    public List<Long> getEnvolvidos() {
        return Collections.unmodifiableList(new ArrayList<>(envolvidos));
    }

    public void adicionarEnvolvido(Long moradorId) {
        envolvidos.add(Objects.requireNonNull(moradorId, "O envolvido e obrigatorio."));
    }

    public void restaurarSituacao(boolean resolvida,
                                  boolean solucaoConfirmada,
                                  boolean excluida,
                                  LocalDate dataSolucao) {
        this.resolvida = resolvida;
        this.solucaoConfirmada = solucaoConfirmada;
        this.excluida = excluida;
        this.dataSolucao = dataSolucao;
    }
}
