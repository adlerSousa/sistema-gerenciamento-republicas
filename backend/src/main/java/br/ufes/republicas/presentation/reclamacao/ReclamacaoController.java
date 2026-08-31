package br.ufes.republicas.presentation.reclamacao;

import br.ufes.republicas.application.reclamacao.GerenciarReclamacoesCasoDeUso;
import br.ufes.republicas.application.reclamacao.ReclamacaoSaida;
import br.ufes.republicas.domain.reclamacao.TipoReclamacao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Recursos HTTP das reclamacoes e sugestoes (UC6 e UC8).
 */
@RestController
@RequestMapping("/api/reclamacoes")
public class ReclamacaoController {

    private final GerenciarReclamacoesCasoDeUso gerenciarReclamacoes;

    public ReclamacaoController(GerenciarReclamacoesCasoDeUso gerenciarReclamacoes) {
        this.gerenciarReclamacoes = gerenciarReclamacoes;
    }

    @PostMapping
    public ResponseEntity<ReclamacaoSaida> cadastrar(
            @Valid @RequestBody CadastrarReclamacaoRequisicao requisicao) {

        ReclamacaoSaida saida = gerenciarReclamacoes.cadastrar(
                requisicao.republicaId(),
                requisicao.autorId(),
                requisicao.tipo(),
                requisicao.descricao(),
                requisicao.envolvidos() == null ? List.of() : requisicao.envolvidos());

        return ResponseEntity.created(URI.create("/api/reclamacoes/" + saida.id())).body(saida);
    }

    @GetMapping
    public ResponseEntity<List<ReclamacaoSaida>> listar(@RequestParam Long republicaId) {
        return ResponseEntity.ok(gerenciarReclamacoes.listarPorRepublica(republicaId));
    }

    @PostMapping("/{reclamacaoId}/resolucao")
    public ResponseEntity<ReclamacaoSaida> marcarComoResolvida(
            @PathVariable Long reclamacaoId,
            @Valid @RequestBody ResolverReclamacaoRequisicao requisicao) {

        return ResponseEntity.ok(
                gerenciarReclamacoes.marcarComoResolvida(reclamacaoId, requisicao.moradorId()));
    }

    @PostMapping("/{reclamacaoId}/confirmacao-solucao")
    public ResponseEntity<ReclamacaoSaida> confirmarSolucao(@PathVariable Long reclamacaoId) {
        return ResponseEntity.ok(gerenciarReclamacoes.confirmarSolucao(reclamacaoId));
    }

    /**
     * Corpo da requisicao de cadastro de reclamacao ou sugestao.
     */
    public record CadastrarReclamacaoRequisicao(
            @NotNull(message = "a republica e obrigatoria") Long republicaId,
            @NotNull(message = "o autor e obrigatorio") Long autorId,
            @NotNull(message = "o tipo e obrigatorio") TipoReclamacao tipo,
            @NotBlank(message = "a descricao e obrigatoria") String descricao,
            List<Long> envolvidos) {
    }

    /**
     * Corpo da requisicao de marcacao de reclamacao como resolvida.
     */
    public record ResolverReclamacaoRequisicao(
            @NotNull(message = "o morador e obrigatorio") Long moradorId) {
    }
}
