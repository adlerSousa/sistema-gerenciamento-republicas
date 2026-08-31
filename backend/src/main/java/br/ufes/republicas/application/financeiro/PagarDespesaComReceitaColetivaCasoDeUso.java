package br.ufes.republicas.application.financeiro;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.comum.RegistradorOperacoes;
import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import br.ufes.republicas.domain.financeiro.Lancamento;
import br.ufes.republicas.domain.financeiro.LancamentoRepositorio;
import br.ufes.republicas.domain.financeiro.ParticipacaoLancamento;
import br.ufes.republicas.domain.financeiro.ReceitaColetiva;
import br.ufes.republicas.domain.financeiro.ReceitaColetivaRepositorio;
import br.ufes.republicas.domain.financeiro.TipoLancamento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Paga um lancamento de despesa utilizando o saldo da receita coletiva da
 * republica (BR05 e BR06).
 *
 * <p>O valor utilizado e debitado do saldo acumulado e as parcelas dos moradores
 * participantes cobertas por esse valor sao quitadas.
 */
@Service
public class PagarDespesaComReceitaColetivaCasoDeUso {

    private final LancamentoRepositorio lancamentoRepositorio;
    private final ReceitaColetivaRepositorio receitaColetivaRepositorio;
    private final RegistradorOperacoes registradorOperacoes;

    public PagarDespesaComReceitaColetivaCasoDeUso(
            LancamentoRepositorio lancamentoRepositorio,
            ReceitaColetivaRepositorio receitaColetivaRepositorio,
            RegistradorOperacoes registradorOperacoes) {
        this.lancamentoRepositorio = lancamentoRepositorio;
        this.receitaColetivaRepositorio = receitaColetivaRepositorio;
        this.registradorOperacoes = registradorOperacoes;
    }

    @Transactional
    public LancamentoSaida executar(Long lancamentoId, BigDecimal valorUtilizado, String usuario) {
        Lancamento lancamento = lancamentoRepositorio.buscarPorId(lancamentoId)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Lancamento", lancamentoId));

        if (lancamento.getTipo() != TipoLancamento.DESPESA) {
            throw new RegraDeNegocioException(
                    "A receita coletiva so pode ser utilizada no pagamento de despesas.");
        }

        ReceitaColetiva receitaColetiva = receitaColetivaRepositorio
                .buscarPorRepublica(lancamento.getRepublicaId())
                .orElseThrow(() -> new RegraDeNegocioException(
                        "A republica nao possui receita coletiva registrada."));

        receitaColetiva.debitar(valorUtilizado);
        receitaColetivaRepositorio.salvar(receitaColetiva);

        quitarParcelasCobertas(lancamento, valorUtilizado);
        lancamento.atualizarStatusConformeParticipacoes();

        Lancamento lancamentoSalvo = lancamentoRepositorio.salvar(lancamento);

        registradorOperacoes.registrarSucesso(
                "Inclusao",
                "pagamento com receita coletiva de " + lancamento.getDescricao(),
                usuario);

        return LancamentoSaida.de(lancamentoSalvo);
    }

    private void quitarParcelasCobertas(Lancamento lancamento, BigDecimal valorDisponivel) {
        BigDecimal restante = valorDisponivel;
        for (ParticipacaoLancamento participacao : lancamento.getParticipacoes()) {
            if (participacao.isPago()) {
                continue;
            }
            if (restante.compareTo(participacao.getValorDevido()) < 0) {
                break;
            }
            participacao.registrarPagamento(LocalDate.now());
            restante = restante.subtract(participacao.getValorDevido());
        }
    }
}
