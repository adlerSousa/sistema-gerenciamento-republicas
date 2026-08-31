package br.ufes.republicas.application.morador;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.comum.RegistradorOperacoes;
import br.ufes.republicas.domain.morador.Morador;
import br.ufes.republicas.domain.morador.MoradorRepositorio;
import br.ufes.republicas.domain.morador.SolicitacaoMoradia;
import br.ufes.republicas.domain.morador.SolicitacaoMoradiaRepositorio;
import br.ufes.republicas.domain.republica.Republica;
import br.ufes.republicas.domain.republica.RepublicaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Efetiva o ingresso de um morador em uma republica a partir do aceite de um
 * convite ou de uma solicitacao de moradia (UC5 e UC17).
 */
@Service
public class AceitarSolicitacaoMoradiaCasoDeUso {

    private final SolicitacaoMoradiaRepositorio solicitacaoRepositorio;
    private final MoradorRepositorio moradorRepositorio;
    private final RepublicaRepositorio republicaRepositorio;
    private final RegistradorOperacoes registradorOperacoes;

    public AceitarSolicitacaoMoradiaCasoDeUso(SolicitacaoMoradiaRepositorio solicitacaoRepositorio,
                                              MoradorRepositorio moradorRepositorio,
                                              RepublicaRepositorio republicaRepositorio,
                                              RegistradorOperacoes registradorOperacoes) {
        this.solicitacaoRepositorio = solicitacaoRepositorio;
        this.moradorRepositorio = moradorRepositorio;
        this.republicaRepositorio = republicaRepositorio;
        this.registradorOperacoes = registradorOperacoes;
    }

    @Transactional
    public MoradorSaida executar(Long solicitacaoId, String usuario) {
        SolicitacaoMoradia solicitacao = solicitacaoRepositorio.buscarPorId(solicitacaoId)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Solicitacao", solicitacaoId));

        Republica republica = republicaRepositorio.buscarPorId(solicitacao.getRepublicaId())
                .orElseThrow(() -> RecursoNaoEncontradoException.de(
                        "Republica", solicitacao.getRepublicaId()));

        Morador morador = moradorRepositorio.buscarPorId(solicitacao.getMoradorId())
                .orElseThrow(() -> RecursoNaoEncontradoException.de(
                        "Morador", solicitacao.getMoradorId()));

        solicitacao.aceitar(LocalDate.now());
        solicitacaoRepositorio.salvar(solicitacao);

        morador.ingressarEm(republica.getId(), LocalDate.now());
        Morador moradorSalvo = moradorRepositorio.salvar(morador);

        registradorOperacoes.registrarSucesso(
                "Inclusao", morador.getNome() + " em " + republica.getNome(), usuario);

        return MoradorSaida.de(moradorSalvo);
    }
}
