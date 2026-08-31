package br.ufes.republicas.application.republica;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.republica.RepublicaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Consulta os dados de uma republica (UC1).
 */
@Service
public class ConsultarRepublicaCasoDeUso {

    private final RepublicaRepositorio republicaRepositorio;

    public ConsultarRepublicaCasoDeUso(RepublicaRepositorio republicaRepositorio) {
        this.republicaRepositorio = republicaRepositorio;
    }

    @Transactional(readOnly = true)
    public RepublicaSaida executar(Long republicaId) {
        return republicaRepositorio.buscarPorId(republicaId)
                .map(RepublicaSaida::de)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Republica", republicaId));
    }
}
