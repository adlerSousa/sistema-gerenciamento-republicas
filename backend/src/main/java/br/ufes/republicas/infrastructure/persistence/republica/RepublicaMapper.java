package br.ufes.republicas.infrastructure.persistence.republica;

import br.ufes.republicas.domain.republica.Endereco;
import br.ufes.republicas.domain.republica.Republica;
import org.springframework.stereotype.Component;

/**
 * Conversao entre a republica do dominio e sua representacao de persistencia.
 */
@Component
public class RepublicaMapper {

    public Republica paraDominio(RepublicaEntity entidade) {
        Endereco endereco = new Endereco(
                entidade.getLogradouro(),
                entidade.getCep(),
                entidade.getBairro(),
                entidade.getPontoReferencia(),
                entidade.getLocalizacaoGeografica());

        Republica republica = new Republica(
                entidade.getNome(),
                entidade.getDataFundacao(),
                endereco,
                entidade.getVantagens(),
                entidade.getDespesasMediasPorMorador(),
                entidade.getTotalVagas(),
                entidade.getVagasOcupadas());

        republica.definirId(entidade.getId());
        republica.definirCodigoEtica(entidade.getCodigoEtica());
        if (entidade.getDataExtincao() != null) {
            republica.extinguir(entidade.getDataExtincao());
        }
        return republica;
    }

    public RepublicaEntity paraEntidade(Republica republica) {
        RepublicaEntity entidade = new RepublicaEntity();
        aplicar(republica, entidade);
        return entidade;
    }

    public void aplicar(Republica republica, RepublicaEntity entidade) {
        entidade.setId(republica.getId());
        entidade.setNome(republica.getNome());
        entidade.setDataFundacao(republica.getDataFundacao());
        entidade.setDataExtincao(republica.getDataExtincao());
        entidade.setLogradouro(republica.getEndereco().logradouro());
        entidade.setCep(republica.getEndereco().cep());
        entidade.setBairro(republica.getEndereco().bairro());
        entidade.setPontoReferencia(republica.getEndereco().pontoReferencia());
        entidade.setLocalizacaoGeografica(republica.getEndereco().localizacaoGeografica());
        entidade.setCodigoEtica(republica.getCodigoEtica());
        entidade.setVantagens(republica.getVantagens());
        entidade.setDespesasMediasPorMorador(republica.getDespesasMediasPorMorador());
        entidade.setTotalVagas(republica.getTotalVagas());
        entidade.setVagasOcupadas(republica.getVagasOcupadas());
    }
}
