package br.ufes.republicas.application.morador;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.comum.RegistradorOperacoes;
import br.ufes.republicas.domain.morador.Morador;
import br.ufes.republicas.domain.morador.MoradorRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Desliga um morador da republica em que reside, devolvendo-o a condicao de
 * "sem teto" (UC2 e BR04).
 */
@Service
public class RemoverMoradorDaRepublicaCasoDeUso {

    private final MoradorRepositorio moradorRepositorio;
    private final RegistradorOperacoes registradorOperacoes;

    public RemoverMoradorDaRepublicaCasoDeUso(MoradorRepositorio moradorRepositorio,
                                              RegistradorOperacoes registradorOperacoes) {
        this.moradorRepositorio = moradorRepositorio;
        this.registradorOperacoes = registradorOperacoes;
    }

    @Transactional
    public MoradorSaida executar(Long moradorId, String usuario) {
        Morador morador = moradorRepositorio.buscarPorId(moradorId)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Morador", moradorId));

        morador.sairDaRepublica();
        Morador moradorSalvo = moradorRepositorio.salvar(morador);

        registradorOperacoes.registrarSucesso("Exclusao", morador.getNome(), usuario);

        return MoradorSaida.de(moradorSalvo);
    }
}
