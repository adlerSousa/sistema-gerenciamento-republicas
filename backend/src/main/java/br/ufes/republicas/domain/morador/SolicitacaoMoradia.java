package br.ufes.republicas.domain.morador;

import br.ufes.republicas.domain.comum.RegraDeNegocioException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Convite enviado pelo representante ou solicitacao enviada por um morador sem
 * teto para ingressar em uma republica (UC5, UC14 e UC17).
 */
public class SolicitacaoMoradia {

    private Long id;
    private Long republicaId;
    private Long moradorId;
    private OrigemSolicitacao origem;
    private StatusSolicitacao status;
    private LocalDate dataRegistro;
    private LocalDate dataResposta;

    protected SolicitacaoMoradia() {
        // Exigido pelos mecanismos de reconstrucao a partir da persistencia.
    }

    public SolicitacaoMoradia(Long republicaId,
                              Long moradorId,
                              OrigemSolicitacao origem,
                              LocalDate dataRegistro) {

        this.republicaId = Objects.requireNonNull(republicaId, "A republica e obrigatoria.");
        this.moradorId = Objects.requireNonNull(moradorId, "O morador e obrigatorio.");
        this.origem = Objects.requireNonNull(origem, "A origem da solicitacao e obrigatoria.");
        this.dataRegistro = Objects.requireNonNull(dataRegistro, "A data de registro e obrigatoria.");
        this.status = StatusSolicitacao.PENDENTE;
    }

    public void aceitar(LocalDate dataResposta) {
        garantirPendente();
        this.status = StatusSolicitacao.ACEITA;
        this.dataResposta = Objects.requireNonNull(dataResposta, "A data de resposta e obrigatoria.");
    }

    public void recusar(LocalDate dataResposta) {
        garantirPendente();
        this.status = StatusSolicitacao.RECUSADA;
        this.dataResposta = Objects.requireNonNull(dataResposta, "A data de resposta e obrigatoria.");
    }

    private void garantirPendente() {
        if (status != StatusSolicitacao.PENDENTE) {
            throw new RegraDeNegocioException("A solicitacao ja foi respondida.");
        }
    }

    public boolean estaPendente() {
        return status == StatusSolicitacao.PENDENTE;
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

    public OrigemSolicitacao getOrigem() {
        return origem;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public LocalDate getDataResposta() {
        return dataResposta;
    }

    public void restaurarSituacao(StatusSolicitacao status, LocalDate dataResposta) {
        this.status = status;
        this.dataResposta = dataResposta;
    }
}
