package br.ufes.republicas.application.republica;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.comum.RegistradorOperacoes;
import br.ufes.republicas.domain.financeiro.ReceitaColetiva;
import br.ufes.republicas.domain.financeiro.ReceitaColetivaRepositorio;
import br.ufes.republicas.domain.morador.Morador;
import br.ufes.republicas.domain.morador.MoradorRepositorio;
import br.ufes.republicas.domain.republica.Endereco;
import br.ufes.republicas.domain.republica.Republica;
import br.ufes.republicas.domain.republica.RepublicaRepositorio;
import br.ufes.republicas.domain.republica.Representante;
import br.ufes.republicas.domain.republica.RepresentanteRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Funda uma nova republica (UC1).
 *
 * <p>Conforme a regra BR02, ao confirmar a criacao de uma republica o morador
 * deixa a republica atual e passa a ser o representante da nova republica
 * (BR03, BR04).
 */
@Service
public class CadastrarRepublicaCasoDeUso {

    private final RepublicaRepositorio republicaRepositorio;
    private final MoradorRepositorio moradorRepositorio;
    private final RepresentanteRepositorio representanteRepositorio;
    private final ReceitaColetivaRepositorio receitaColetivaRepositorio;
    private final RegistradorOperacoes registradorOperacoes;

    public CadastrarRepublicaCasoDeUso(RepublicaRepositorio republicaRepositorio,
                                       MoradorRepositorio moradorRepositorio,
                                       RepresentanteRepositorio representanteRepositorio,
                                       ReceitaColetivaRepositorio receitaColetivaRepositorio,
                                       RegistradorOperacoes registradorOperacoes) {
        this.republicaRepositorio = republicaRepositorio;
        this.moradorRepositorio = moradorRepositorio;
        this.representanteRepositorio = representanteRepositorio;
        this.receitaColetivaRepositorio = receitaColetivaRepositorio;
        this.registradorOperacoes = registradorOperacoes;
    }

    @Transactional
    public RepublicaSaida executar(CadastrarRepublicaEntrada entrada) {
        Morador fundador = moradorRepositorio.buscarPorId(entrada.moradorFundadorId())
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Morador", entrada.moradorFundadorId()));

        Endereco endereco = new Endereco(
                entrada.logradouro(),
                entrada.cep(),
                entrada.bairro(),
                entrada.pontoReferencia(),
                entrada.localizacaoGeografica());

        Republica republica = new Republica(
                entrada.nome(),
                entrada.dataFundacao(),
                endereco,
                entrada.vantagens(),
                entrada.despesasMediasPorMorador(),
                entrada.totalVagas(),
                entrada.vagasOcupadas());
        republica.definirCodigoEtica(entrada.codigoEtica());

        Republica republicaSalva = republicaRepositorio.salvar(republica);

        receitaColetivaRepositorio.salvar(new ReceitaColetiva(republicaSalva.getId()));

        transferirFundador(fundador, republicaSalva);

        registradorOperacoes.registrarSucesso(
                "Inclusao", republicaSalva.getNome(), fundador.getApelido());

        return RepublicaSaida.de(republicaSalva);
    }

    private void transferirFundador(Morador fundador, Republica republica) {
        if (!fundador.estaSemTeto()) {
            fundador.sairDaRepublica();
        }
        fundador.ingressarEm(republica.getId(), republica.getDataFundacao());
        moradorRepositorio.salvar(fundador);

        representanteRepositorio.salvar(new Representante(
                republica.getId(), fundador.getId(), LocalDate.now()));
    }
}
