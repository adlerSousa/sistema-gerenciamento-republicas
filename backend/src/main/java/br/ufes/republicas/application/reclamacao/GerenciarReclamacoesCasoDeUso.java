package br.ufes.republicas.application.reclamacao;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import br.ufes.republicas.domain.reclamacao.ReclamacaoSugestao;
import br.ufes.republicas.domain.reclamacao.ReclamacaoSugestaoRepositorio;
import br.ufes.republicas.domain.reclamacao.TipoReclamacao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Manutencao das reclamacoes e sugestoes da republica (UC6 e UC8).
 */
@Service
public class GerenciarReclamacoesCasoDeUso {

    private final ReclamacaoSugestaoRepositorio reclamacaoRepositorio;

    public GerenciarReclamacoesCasoDeUso(ReclamacaoSugestaoRepositorio reclamacaoRepositorio) {
        this.reclamacaoRepositorio = reclamacaoRepositorio;
    }

    @Transactional
    public ReclamacaoSaida cadastrar(Long republicaId,
                                     Long autorId,
                                     TipoReclamacao tipo,
                                     String descricao,
                                     List<Long> envolvidos) {
        ReclamacaoSugestao reclamacao = new ReclamacaoSugestao(
                republicaId, autorId, tipo, descricao, LocalDate.now(), envolvidos);

        return ReclamacaoSaida.de(reclamacaoRepositorio.salvar(reclamacao));
    }

    @Transactional(readOnly = true)
    public List<ReclamacaoSaida> listarPorRepublica(Long republicaId) {
        return reclamacaoRepositorio.listarPorRepublica(republicaId).stream()
                .map(ReclamacaoSaida::de)
                .toList();
    }

    /**
     * Marca o registro como resolvido. Apenas o autor pode realizar essa
     * marcacao (UC6).
     */
    @Transactional
    public ReclamacaoSaida marcarComoResolvida(Long reclamacaoId, Long moradorId) {
        ReclamacaoSugestao reclamacao = buscar(reclamacaoId);

        if (!reclamacao.getAutorId().equals(moradorId)) {
            throw new RegraDeNegocioException(
                    "Apenas o morador que registrou a reclamacao pode marca-la como resolvida.");
        }

        reclamacao.marcarComoResolvida(LocalDate.now());
        return ReclamacaoSaida.de(reclamacaoRepositorio.salvar(reclamacao));
    }

    /**
     * Confirma a solucao apontada. Apenas o representante da republica pode
     * confirmar (UC6).
     */
    @Transactional
    public ReclamacaoSaida confirmarSolucao(Long reclamacaoId) {
        ReclamacaoSugestao reclamacao = buscar(reclamacaoId);
        reclamacao.confirmarSolucao();
        return ReclamacaoSaida.de(reclamacaoRepositorio.salvar(reclamacao));
    }

    private ReclamacaoSugestao buscar(Long reclamacaoId) {
        return reclamacaoRepositorio.buscarPorId(reclamacaoId)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Reclamacao", reclamacaoId));
    }
}
