package br.ufes.republicas.application.morador;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import br.ufes.republicas.domain.morador.Morador;
import br.ufes.republicas.domain.morador.MoradorRepositorio;
import br.ufes.republicas.domain.morador.OrigemSolicitacao;
import br.ufes.republicas.domain.morador.SolicitacaoMoradia;
import br.ufes.republicas.domain.morador.SolicitacaoMoradiaRepositorio;
import br.ufes.republicas.domain.notificacao.Notificacao;
import br.ufes.republicas.domain.notificacao.NotificacaoRepositorio;
import br.ufes.republicas.domain.notificacao.TipoNotificacao;
import br.ufes.republicas.domain.republica.Republica;
import br.ufes.republicas.domain.republica.RepublicaRepositorio;
import br.ufes.republicas.domain.republica.RepresentanteRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Registra um convite enviado pelo representante ou uma solicitacao enviada por
 * um morador sem teto (UC5 e UC14).
 *
 * <p>O destinatario e notificado do registro (RF02).
 */
@Service
public class EnviarSolicitacaoMoradiaCasoDeUso {

    private final SolicitacaoMoradiaRepositorio solicitacaoRepositorio;
    private final MoradorRepositorio moradorRepositorio;
    private final RepublicaRepositorio republicaRepositorio;
    private final RepresentanteRepositorio representanteRepositorio;
    private final NotificacaoRepositorio notificacaoRepositorio;

    public EnviarSolicitacaoMoradiaCasoDeUso(SolicitacaoMoradiaRepositorio solicitacaoRepositorio,
                                             MoradorRepositorio moradorRepositorio,
                                             RepublicaRepositorio republicaRepositorio,
                                             RepresentanteRepositorio representanteRepositorio,
                                             NotificacaoRepositorio notificacaoRepositorio) {
        this.solicitacaoRepositorio = solicitacaoRepositorio;
        this.moradorRepositorio = moradorRepositorio;
        this.republicaRepositorio = republicaRepositorio;
        this.representanteRepositorio = representanteRepositorio;
        this.notificacaoRepositorio = notificacaoRepositorio;
    }

    @Transactional
    public Long executar(Long republicaId, Long moradorId, OrigemSolicitacao origem) {
        Republica republica = republicaRepositorio.buscarPorId(republicaId)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Republica", republicaId));

        Morador morador = moradorRepositorio.buscarPorId(moradorId)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Morador", moradorId));

        if (!morador.estaSemTeto()) {
            throw new RegraDeNegocioException(
                    "O morador ja reside em uma republica e nao pode solicitar moradia em outra.");
        }

        SolicitacaoMoradia solicitacao = solicitacaoRepositorio.salvar(
                new SolicitacaoMoradia(republicaId, moradorId, origem, LocalDate.now()));

        notificarDestinatario(republica, morador, origem);

        return solicitacao.getId();
    }

    private void notificarDestinatario(Republica republica, Morador morador, OrigemSolicitacao origem) {
        if (origem == OrigemSolicitacao.CONVITE) {
            notificacaoRepositorio.salvar(new Notificacao(
                    morador.getId(),
                    republica.getId(),
                    TipoNotificacao.CONVITE_RECEBIDO,
                    "Voce recebeu um convite para morar na republica " + republica.getNome() + ".",
                    LocalDate.now()));
            return;
        }

        representanteRepositorio.buscarVigentePorRepublica(republica.getId())
                .ifPresent(representante -> notificacaoRepositorio.salvar(new Notificacao(
                        representante.getMoradorId(),
                        republica.getId(),
                        TipoNotificacao.SOLICITACAO_MORADIA,
                        "%s enviou uma solicitacao para morar na republica.".formatted(morador.getNome()),
                        LocalDate.now())));
    }
}
