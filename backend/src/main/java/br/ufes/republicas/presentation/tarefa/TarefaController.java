package br.ufes.republicas.presentation.tarefa;

import br.ufes.republicas.application.tarefa.CadastrarTarefaEntrada;
import br.ufes.republicas.application.tarefa.GerenciarTarefasCasoDeUso;
import br.ufes.republicas.application.tarefa.TarefaSaida;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/**
 * Recursos HTTP das tarefas domesticas (UC3 e UC12).
 */
@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final GerenciarTarefasCasoDeUso gerenciarTarefas;

    public TarefaController(GerenciarTarefasCasoDeUso gerenciarTarefas) {
        this.gerenciarTarefas = gerenciarTarefas;
    }

    @PostMapping
    public ResponseEntity<TarefaSaida> cadastrar(
            @Valid @RequestBody CadastrarTarefaRequisicao requisicao) {

        TarefaSaida saida = gerenciarTarefas.cadastrar(new CadastrarTarefaEntrada(
                requisicao.republicaId(),
                requisicao.descricao(),
                requisicao.dataAgendamento(),
                requisicao.dataTermino(),
                requisicao.responsaveis()));

        return ResponseEntity.created(URI.create("/api/tarefas/" + saida.id())).body(saida);
    }

    @GetMapping
    public ResponseEntity<List<TarefaSaida>> listar(
            @RequestParam(required = false) Long republicaId,
            @RequestParam(required = false) Long moradorId) {

        if (moradorId != null) {
            return ResponseEntity.ok(gerenciarTarefas.listarPorResponsavel(moradorId));
        }
        if (republicaId != null) {
            return ResponseEntity.ok(gerenciarTarefas.listarPorRepublica(republicaId));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{tarefaId}/conclusao")
    public ResponseEntity<TarefaSaida> registrarConclusao(
            @PathVariable Long tarefaId,
            @Valid @RequestBody RegistrarConclusaoRequisicao requisicao,
            @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario) {

        return ResponseEntity.ok(gerenciarTarefas.registrarConclusao(
                tarefaId, requisicao.moradorId(), requisicao.descricaoConclusao(), usuario));
    }

    /**
     * Corpo da requisicao de cadastro de tarefa.
     */
    public record CadastrarTarefaRequisicao(
            @NotNull(message = "a republica e obrigatoria") Long republicaId,
            @NotBlank(message = "a descricao e obrigatoria") String descricao,
            @NotNull(message = "a data de agendamento e obrigatoria") LocalDate dataAgendamento,
            @NotNull(message = "a data de termino e obrigatoria") LocalDate dataTermino,
            @NotEmpty(message = "a tarefa deve ter ao menos um responsavel") List<Long> responsaveis) {
    }

    /**
     * Corpo da requisicao de registro de conclusao de tarefa.
     */
    public record RegistrarConclusaoRequisicao(
            @NotNull(message = "o morador e obrigatorio") Long moradorId,
            @NotBlank(message = "a descricao da conclusao e obrigatoria") String descricaoConclusao) {
    }
}
