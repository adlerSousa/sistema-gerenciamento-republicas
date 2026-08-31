package br.ufes.republicas.infrastructure.persistence.morador;

import br.ufes.republicas.domain.morador.OrigemSolicitacao;
import br.ufes.republicas.domain.morador.StatusSolicitacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Representacao da solicitacao de moradia no modelo de persistencia.
 */
@Entity
@Table(name = "solicitacao_moradia")
public class SolicitacaoMoradiaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "republica_id", nullable = false)
    private Long republicaId;

    @Column(name = "morador_id", nullable = false)
    private Long moradorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrigemSolicitacao origem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSolicitacao status;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @Column(name = "data_resposta")
    private LocalDate dataResposta;

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

    public Long getMoradorId() {
        return moradorId;
    }

    public void setMoradorId(Long moradorId) {
        this.moradorId = moradorId;
    }

    public OrigemSolicitacao getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemSolicitacao origem) {
        this.origem = origem;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public LocalDate getDataResposta() {
        return dataResposta;
    }

    public void setDataResposta(LocalDate dataResposta) {
        this.dataResposta = dataResposta;
    }
}
