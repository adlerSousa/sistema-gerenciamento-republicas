package br.ufes.republicas.domain.republica;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Morador responsavel pela republica durante um periodo determinado (BR03).
 */
public class Representante {

    private Long id;
    private Long republicaId;
    private Long moradorId;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    protected Representante() {
        // Exigido pelos mecanismos de reconstrucao a partir da persistencia.
    }

    public Representante(Long republicaId, Long moradorId, LocalDate dataInicio) {
        this.republicaId = Objects.requireNonNull(republicaId, "A republica e obrigatoria.");
        this.moradorId = Objects.requireNonNull(moradorId, "O morador e obrigatorio.");
        this.dataInicio = Objects.requireNonNull(dataInicio, "A data de inicio e obrigatoria.");
    }

    public boolean estaVigente() {
        return dataFim == null;
    }

    public void encerrarMandato(LocalDate dataFim) {
        Objects.requireNonNull(dataFim, "A data de fim e obrigatoria.");
        if (dataFim.isBefore(dataInicio)) {
            throw new RegraDeNegocioException(
                    "A data de fim do mandato nao pode ser anterior a data de inicio.");
        }
        this.dataFim = dataFim;
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

    public Long getMoradorId() {
        return moradorId;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }
}
