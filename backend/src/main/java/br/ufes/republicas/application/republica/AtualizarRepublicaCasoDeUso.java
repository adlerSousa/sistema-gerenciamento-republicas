package br.ufes.republicas.application.republica;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.comum.RegistradorOperacoes;
import br.ufes.republicas.domain.republica.Endereco;
import br.ufes.republicas.domain.republica.Republica;
import br.ufes.republicas.domain.republica.RepublicaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Altera os dados cadastrais de uma republica (UC1).
 */
@Service
public class AtualizarRepublicaCasoDeUso {

    private final RepublicaRepositorio republicaRepositorio;
    private final RegistradorOperacoes registradorOperacoes;

    public AtualizarRepublicaCasoDeUso(RepublicaRepositorio republicaRepositorio,
                                       RegistradorOperacoes registradorOperacoes) {
        this.republicaRepositorio = republicaRepositorio;
        this.registradorOperacoes = registradorOperacoes;
    }

    @Transactional
    public RepublicaSaida executar(Long republicaId, AtualizarRepublicaEntrada entrada, String usuario) {
        Republica republica = republicaRepositorio.buscarPorId(republicaId)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Republica", republicaId));

        Endereco endereco = new Endereco(
                entrada.logradouro(),
                entrada.cep(),
                entrada.bairro(),
                entrada.pontoReferencia(),
                entrada.localizacaoGeografica());

        republica.alterarDadosCadastrais(
                entrada.nome(),
                endereco,
                entrada.vantagens(),
                entrada.codigoEtica(),
                entrada.despesasMediasPorMorador());
        republica.alterarTotalDeVagas(entrada.totalVagas());

        Republica republicaSalva = republicaRepositorio.salvar(republica);

        registradorOperacoes.registrarSucesso("Correcao", republicaSalva.getNome(), usuario);

        return RepublicaSaida.de(republicaSalva);
    }
}
