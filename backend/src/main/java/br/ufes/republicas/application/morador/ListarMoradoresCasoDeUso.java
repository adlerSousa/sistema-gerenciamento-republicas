package br.ufes.republicas.application.morador;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.morador.MoradorRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Consulta os moradores cadastrados (UC2 e UC7).
 */
@Service
public class ListarMoradoresCasoDeUso {

    private final MoradorRepositorio moradorRepositorio;

    public ListarMoradoresCasoDeUso(MoradorRepositorio moradorRepositorio) {
        this.moradorRepositorio = moradorRepositorio;
    }

    @Transactional(readOnly = true)
    public List<MoradorSaida> porRepublica(Long republicaId) {
        return moradorRepositorio.listarPorRepublica(republicaId).stream()
                .map(MoradorSaida::de)
                .toList();
    }

    /**
     * Lista os moradores que nao residem em nenhuma republica (BR04).
     */
    @Transactional(readOnly = true)
    public List<MoradorSaida> semTeto() {
        return moradorRepositorio.listarSemTeto().stream()
                .map(MoradorSaida::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MoradorSaida> todos() {
        return moradorRepositorio.listarTodos().stream()
                .map(MoradorSaida::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public MoradorSaida porId(Long moradorId) {
        return moradorRepositorio.buscarPorId(moradorId)
                .map(MoradorSaida::de)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Morador", moradorId));
    }
}
