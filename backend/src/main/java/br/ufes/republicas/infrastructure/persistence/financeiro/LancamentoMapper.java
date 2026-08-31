package br.ufes.republicas.infrastructure.persistence.financeiro;

import br.ufes.republicas.domain.financeiro.Lancamento;
import br.ufes.republicas.domain.financeiro.ParticipacaoLancamento;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Conversao entre o lancamento do dominio e sua representacao de persistencia.
 */
@Component
public class LancamentoMapper {

    public Lancamento paraDominio(LancamentoEntity entidade) {
        Lancamento lancamento = new Lancamento(
                entidade.getRepublicaId(),
                entidade.getTipo(),
                entidade.getDescricao(),
                entidade.getValor(),
                entidade.getDataVencimento(),
                entidade.getDataCadastro(),
                entidade.getPeriodicidade(),
                entidade.getFormaRateio());

        lancamento.definirId(entidade.getId());
        lancamento.definirParcela(entidade.getNumeroParcela(), entidade.getTotalParcelas());
        lancamento.definirLancamentoOrigemId(entidade.getLancamentoOrigemId());
        lancamento.definirStatus(entidade.getStatus());

        entidade.getParticipacoes().forEach(participacaoEntidade -> {
            ParticipacaoLancamento participacao = new ParticipacaoLancamento(
                    participacaoEntidade.getMoradorId(),
                    participacaoEntidade.getPercentual(),
                    participacaoEntidade.getValorFixo(),
                    participacaoEntidade.getValorDevido());
            participacao.definirId(participacaoEntidade.getId());
            participacao.definirLancamentoId(entidade.getId());
            participacao.restaurarPagamento(
                    participacaoEntidade.isPago(), participacaoEntidade.getDataPagamento());
            lancamento.adicionarParticipacao(participacao);
        });

        return lancamento;
    }

    public LancamentoEntity paraEntidade(Lancamento lancamento) {
        LancamentoEntity entidade = new LancamentoEntity();
        aplicar(lancamento, entidade);
        return entidade;
    }

    public void aplicar(Lancamento lancamento, LancamentoEntity entidade) {
        entidade.setId(lancamento.getId());
        entidade.setRepublicaId(lancamento.getRepublicaId());
        entidade.setTipo(lancamento.getTipo());
        entidade.setDescricao(lancamento.getDescricao());
        entidade.setValor(lancamento.getValor());
        entidade.setDataVencimento(lancamento.getDataVencimento());
        entidade.setDataCadastro(lancamento.getDataCadastro());
        entidade.setPeriodicidade(lancamento.getPeriodicidade());
        entidade.setFormaRateio(lancamento.getFormaRateio());
        entidade.setNumeroParcela(lancamento.getNumeroParcela());
        entidade.setTotalParcelas(lancamento.getTotalParcelas());
        entidade.setLancamentoOrigemId(lancamento.getLancamentoOrigemId());
        entidade.setStatus(lancamento.getStatus());
        entidade.setJustificativaEstorno(lancamento.getJustificativaEstorno());

        aplicarParticipacoes(lancamento, entidade);
    }

    private void aplicarParticipacoes(Lancamento lancamento, LancamentoEntity entidade) {
        List<ParticipacaoLancamentoEntity> participacoes = new ArrayList<>();

        for (ParticipacaoLancamento participacao : lancamento.getParticipacoes()) {
            ParticipacaoLancamentoEntity participacaoEntidade = entidade.getParticipacoes().stream()
                    .filter(existente -> existente.getMoradorId().equals(participacao.getMoradorId()))
                    .findFirst()
                    .orElseGet(ParticipacaoLancamentoEntity::new);

            participacaoEntidade.setLancamento(entidade);
            participacaoEntidade.setMoradorId(participacao.getMoradorId());
            participacaoEntidade.setPercentual(participacao.getPercentual());
            participacaoEntidade.setValorFixo(participacao.getValorFixo());
            participacaoEntidade.setValorDevido(participacao.getValorDevido());
            participacaoEntidade.setPago(participacao.isPago());
            participacaoEntidade.setDataPagamento(participacao.getDataPagamento());

            participacoes.add(participacaoEntidade);
        }

        entidade.getParticipacoes().clear();
        entidade.getParticipacoes().addAll(participacoes);
    }
}
