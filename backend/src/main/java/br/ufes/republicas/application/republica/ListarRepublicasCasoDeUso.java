package br.ufes.republicas.application.republica;

import br.ufes.republicas.domain.republica.RepublicaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lista as republicas cadastradas no sistema.
 */
@Service
public class ListarRepublicasCasoDeUso {

    private final RepublicaRepositorio republicaRepositorio;

    public ListarRepublicasCasoDeUso(RepublicaRepositorio republicaRepositorio) {
        this.republicaRepositorio = republicaRepositorio;
    }

    @Transactional(readOnly = true)
    public List<RepublicaSaida> executar() {
        return republicaRepositorio.listarTodas().stream()
                .map(RepublicaSaida::de)
                .toList();
    }

    /**
     * Lista as republicas cujo nome contem o texto informado, em ordem
     * decrescente de similaridade de nome (RF08).
     */
    @Transactional(readOnly = true)
    public List<RepublicaSaida> executarPorNome(String nome) {
        return republicaRepositorio.listarPorNomeSemelhante(nome).stream()
                .map(RepublicaSaida::de)
                .toList();
    }
}
