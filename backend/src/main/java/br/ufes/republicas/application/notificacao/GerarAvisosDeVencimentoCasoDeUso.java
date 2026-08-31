package br.ufes.republicas.application.notificacao;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.financeiro.Lancamento;
import br.ufes.republicas.domain.financeiro.LancamentoRepositorio;
import br.ufes.republicas.domain.morador.Morador;
import br.ufes.republicas.domain.morador.MoradorRepositorio;
import br.ufes.republicas.domain.notificacao.Notificacao;
import br.ufes.republicas.domain.notificacao.NotificacaoRepositorio;
import br.ufes.republicas.domain.notificacao.TipoNotificacao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Emite os avisos de lancamentos com vencimento proximo para um morador (RF01).
 *
 * <p>Conforme a regra BR01.1, o aviso e emitido com cinco dias de antecedencia.
 * O aviso e mantido apos o vencimento, enquanto o lancamento permanecer
 * pendente.
 */
@Service
public class GerarAvisosDeVencimentoCasoDeUso {

    private final LancamentoRepositorio lancamentoRepositorio;
    private final MoradorRepositorio moradorRepositorio;
    private final NotificacaoRepositorio notificacaoRepositorio;
    private final int prazoAvisoVencimentoDias;

    public GerarAvisosDeVencimentoCasoDeUso(
            LancamentoRepositorio lancamentoRepositorio,
            MoradorRepositorio moradorRepositorio,
            NotificacaoRepositorio notificacaoRepositorio,
            @Value("${republicas.notificacao.prazo-aviso-vencimento-dias-padrao:5}")
            int prazoAvisoVencimentoDias) {
        this.lancamentoRepositorio = lancamentoRepositorio;
        this.moradorRepositorio = moradorRepositorio;
        this.notificacaoRepositorio = notificacaoRepositorio;
        this.prazoAvisoVencimentoDias = prazoAvisoVencimentoDias;
    }

    @Transactional
    public List<NotificacaoSaida> executar(Long moradorId) {
        Morador morador = moradorRepositorio.buscarPorId(moradorId)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Morador", moradorId));

        if (morador.estaSemTeto()) {
            return List.of();
        }

        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(prazoAvisoVencimentoDias);

        List<Notificacao> avisos = new ArrayList<>();
        for (Lancamento lancamento
                : lancamentoRepositorio.listarPendentesComVencimentoAte(morador.getRepublicaId(), limite)) {

            if (!moradorParticipaEDeve(lancamento, moradorId)) {
                continue;
            }
            if (notificacaoRepositorio.existeParaMoradorELancamento(moradorId, lancamento.getId())) {
                continue;
            }

            Notificacao aviso = new Notificacao(
                    moradorId,
                    morador.getRepublicaId(),
                    TipoNotificacao.VENCIMENTO_PROXIMO,
                    montarMensagem(lancamento, hoje),
                    hoje);
            aviso.vincularLancamento(lancamento.getId());
            avisos.add(notificacaoRepositorio.salvar(aviso));
        }

        return avisos.stream().map(NotificacaoSaida::de).toList();
    }

    private boolean moradorParticipaEDeve(Lancamento lancamento, Long moradorId) {
        return lancamento.getParticipacoes().stream()
                .filter(participacao -> participacao.getMoradorId().equals(moradorId))
                .anyMatch(participacao -> !participacao.isPago());
    }

    private String montarMensagem(Lancamento lancamento, LocalDate hoje) {
        if (lancamento.estaVencido(hoje)) {
            return "O lancamento \"%s\" venceu em %s e continua pendente."
                    .formatted(lancamento.getDescricao(), lancamento.getDataVencimento());
        }
        return "O lancamento \"%s\" vence em %s."
                .formatted(lancamento.getDescricao(), lancamento.getDataVencimento());
    }
}
