package br.ufes.republicas.infrastructure.persistence.morador;

import br.ufes.republicas.domain.morador.SolicitacaoMoradia;
import br.ufes.republicas.domain.morador.SolicitacaoMoradiaRepositorio;
import br.ufes.republicas.domain.morador.StatusSolicitacao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementacao da porta de repositorio de solicitacoes de moradia sobre JPA.
 */
@Repository
public class SolicitacaoMoradiaRepositorioJpa implements SolicitacaoMoradiaRepositorio {

    private final SolicitacaoMoradiaJpaRepository repositorio;

    public SolicitacaoMoradiaRepositorioJpa(SolicitacaoMoradiaJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public SolicitacaoMoradia salvar(SolicitacaoMoradia solicitacao) {
        SolicitacaoMoradiaEntity entidade = solicitacao.getId() == null
                ? new SolicitacaoMoradiaEntity()
                : repositorio.findById(solicitacao.getId()).orElseGet(SolicitacaoMoradiaEntity::new);

        entidade.setId(solicitacao.getId());
        entidade.setRepublicaId(solicitacao.getRepublicaId());
        entidade.setMoradorId(solicitacao.getMoradorId());
        entidade.setOrigem(solicitacao.getOrigem());
        entidade.setStatus(solicitacao.getStatus());
        entidade.setDataRegistro(solicitacao.getDataRegistro());
        entidade.setDataResposta(solicitacao.getDataResposta());

        return paraDominio(repositorio.save(entidade));
    }

    @Override
    public Optional<SolicitacaoMoradia> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    public List<SolicitacaoMoradia> listarPendentesPorRepublica(Long republicaId) {
        return repositorio
                .findByRepublicaIdAndStatusOrderByDataRegistroAsc(
                        republicaId, StatusSolicitacao.PENDENTE)
                .stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<SolicitacaoMoradia> listarPendentesPorMorador(Long moradorId) {
        return repositorio
                .findByMoradorIdAndStatusOrderByDataRegistroAsc(
                        moradorId, StatusSolicitacao.PENDENTE)
                .stream()
                .map(this::paraDominio)
                .toList();
    }

    private SolicitacaoMoradia paraDominio(SolicitacaoMoradiaEntity entidade) {
        SolicitacaoMoradia solicitacao = new SolicitacaoMoradia(
                entidade.getRepublicaId(),
                entidade.getMoradorId(),
                entidade.getOrigem(),
                entidade.getDataRegistro());
        solicitacao.definirId(entidade.getId());
        solicitacao.restaurarSituacao(entidade.getStatus(), entidade.getDataResposta());
        return solicitacao;
    }
}
