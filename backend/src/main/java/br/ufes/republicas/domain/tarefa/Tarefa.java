package br.ufes.republicas.domain.tarefa;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Tarefa domestica atribuida a um ou mais moradores da republica (BR08).
 */
public class Tarefa {

    private Long id;
    private Long republicaId;
    private String descricao;
    private LocalDate dataAgendamento;
    private LocalDate dataTermino;
    private boolean finalizada;
    private LocalDate dataConclusao;
    private String descricaoConclusao;
    private final Set<Long> responsaveis = new LinkedHashSet<>();

    protected Tarefa() {
        // Exigido pelos mecanismos de reconstrucao a partir da persistencia.
    }

    public Tarefa(Long republicaId,
                  String descricao,
                  LocalDate dataAgendamento,
                  LocalDate dataTermino,
                  List<Long> responsaveis) {

        this.republicaId = Objects.requireNonNull(republicaId, "A republica e obrigatoria.");
        this.descricao = Objects.requireNonNull(descricao, "A descricao da tarefa e obrigatoria.");
        this.dataAgendamento = Objects.requireNonNull(dataAgendamento, "A data de agendamento e obrigatoria.");
        this.dataTermino = Objects.requireNonNull(dataTermino, "A data de termino e obrigatoria.");

        if (dataTermino.isBefore(dataAgendamento)) {
            throw new RegraDeNegocioException(
                    "A data de termino nao pode ser anterior a data de agendamento.");
        }
        if (responsaveis == null || responsaveis.isEmpty()) {
            throw new RegraDeNegocioException("A tarefa deve possuir ao menos um responsavel.");
        }

        this.responsaveis.addAll(responsaveis);
        this.finalizada = false;
    }

    /**
     * Registra a conclusao da tarefa pelo morador responsavel (UC12).
     */
    public void registrarConclusao(String descricaoConclusao, LocalDate dataConclusao) {
        Objects.requireNonNull(descricaoConclusao, "A descricao da conclusao e obrigatoria.");
        Objects.requireNonNull(dataConclusao, "A data de conclusao e obrigatoria.");
        if (finalizada) {
            throw new RegraDeNegocioException("A tarefa ja foi concluida.");
        }
        this.finalizada = true;
        this.descricaoConclusao = descricaoConclusao;
        this.dataConclusao = dataConclusao;
    }

    /**
     * Indica se a tarefa foi concluida dentro do prazo estipulado, condicao
     * considerada no indice de realizacao de tarefas (BR02.2.2).
     */
    public boolean concluidaNoPrazo() {
        return finalizada && dataConclusao != null && !dataConclusao.isAfter(dataTermino);
    }

    public boolean ehResponsavel(Long moradorId) {
        return responsaveis.contains(moradorId);
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

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public String getDescricaoConclusao() {
        return descricaoConclusao;
    }

    public List<Long> getResponsaveis() {
        return Collections.unmodifiableList(new ArrayList<>(responsaveis));
    }

    public void restaurarConclusao(boolean finalizada, LocalDate dataConclusao, String descricaoConclusao) {
        this.finalizada = finalizada;
        this.dataConclusao = dataConclusao;
        this.descricaoConclusao = descricaoConclusao;
    }

    public void adicionarResponsavel(Long moradorId) {
        responsaveis.add(Objects.requireNonNull(moradorId, "O responsavel e obrigatorio."));
    }
}
