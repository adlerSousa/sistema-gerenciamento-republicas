package br.ufes.republicas.application.financeiro;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.comum.RegistradorOperacoes;
import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import br.ufes.republicas.domain.financeiro.Lancamento;
import br.ufes.republicas.domain.financeiro.LancamentoRepositorio;
import br.ufes.republicas.domain.financeiro.ParticipacaoLancamento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Registra o pagamento da parcela devida por um morador em um lancamento (UC9).
 */
@Service
public class RegistrarPagamentoCasoDeUso {

    private final LancamentoRepositorio lancamentoRepositorio;
    private final RegistradorOperacoes registradorOperacoes;

    public RegistrarPagamentoCasoDeUso(LancamentoRepositorio lancamentoRepositorio,
                                       RegistradorOperacoes registradorOperacoes) {
        this.lancamentoRepositorio = lancamentoRepositorio;
        this.registradorOperacoes = registradorOperacoes;
    }

    @Transactional
    public LancamentoSaida executar(Long lancamentoId, Long moradorId, String usuario) {
        Lancamento lancamento = lancamentoRepositorio.buscarPorId(lancamentoId)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Lancamento", lancamentoId));

        ParticipacaoLancamento participacao = lancamento.getParticipacoes().stream()
                .filter(candidata -> candidata.getMoradorId().equals(moradorId))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException(
                        "O morador informado nao participa deste lancamento."));

        participacao.registrarPagamento(LocalDate.now());
        lancamento.atualizarStatusConformeParticipacoes();

        Lancamento lancamentoSalvo = lancamentoRepositorio.salvar(lancamento);

        registradorOperacoes.registrarSucesso(
                "Inclusao", "pagamento de " + lancamento.getDescricao(), usuario);

        return LancamentoSaida.de(lancamentoSalvo);
    }
}
