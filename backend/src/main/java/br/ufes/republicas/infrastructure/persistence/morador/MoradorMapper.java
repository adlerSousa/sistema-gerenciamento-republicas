package br.ufes.republicas.infrastructure.persistence.morador;

import br.ufes.republicas.domain.morador.Morador;
import org.springframework.stereotype.Component;

/**
 * Conversao entre o morador do dominio e sua representacao de persistencia.
 */
@Component
public class MoradorMapper {

    public Morador paraDominio(MoradorEntity entidade) {
        Morador morador = new Morador(
                entidade.getNome(),
                entidade.getApelido(),
                entidade.getCpf(),
                entidade.getTelefone(),
                entidade.getTelefoneResponsavelUm(),
                entidade.getTelefoneResponsavelDois());

        morador.definirId(entidade.getId());
        morador.definirLinkRedeSocial(entidade.getLinkRedeSocial());
        morador.definirRepublicaId(entidade.getRepublicaId());
        morador.definirDataIngresso(entidade.getDataIngresso());
        morador.definirPercentualRateio(entidade.getPercentualRateio());
        return morador;
    }

    public MoradorEntity paraEntidade(Morador morador) {
        MoradorEntity entidade = new MoradorEntity();
        aplicar(morador, entidade);
        return entidade;
    }

    public void aplicar(Morador morador, MoradorEntity entidade) {
        entidade.setId(morador.getId());
        entidade.setNome(morador.getNome());
        entidade.setApelido(morador.getApelido());
        entidade.setCpf(morador.getCpf());
        entidade.setTelefone(morador.getTelefone());
        entidade.setLinkRedeSocial(morador.getLinkRedeSocial());
        entidade.setTelefoneResponsavelUm(morador.getTelefoneResponsavelUm());
        entidade.setTelefoneResponsavelDois(morador.getTelefoneResponsavelDois());
        entidade.setPerfilPublico(morador.isPerfilPublico());
        entidade.setRepublicaId(morador.getRepublicaId());
        entidade.setDataIngresso(morador.getDataIngresso());
        entidade.setPercentualRateio(morador.getPercentualRateio());
    }
}
