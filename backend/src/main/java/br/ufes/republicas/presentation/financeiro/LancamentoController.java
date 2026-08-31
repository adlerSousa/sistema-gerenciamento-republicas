package br.ufes.republicas.presentation.financeiro;

import br.ufes.republicas.application.financeiro.ConsultarLancamentoCasoDeUso;
import br.ufes.republicas.application.financeiro.LancamentoSaida;
import br.ufes.republicas.application.financeiro.ListarLancamentosCasoDeUso;
import br.ufes.republicas.application.financeiro.PagarDespesaComReceitaColetivaCasoDeUso;
import br.ufes.republicas.application.financeiro.RegistrarPagamentoCasoDeUso;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Recursos HTTP de consulta de lancamentos e de registro de pagamentos
 * (UC4, UC9 e UC10).
 */
@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

    private final ListarLancamentosCasoDeUso listarLancamentos;
    private final ConsultarLancamentoCasoDeUso consultarLancamento;
    private final RegistrarPagamentoCasoDeUso registrarPagamento;
    private final PagarDespesaComReceitaColetivaCasoDeUso pagarComReceitaColetiva;

    public LancamentoController(ListarLancamentosCasoDeUso listarLancamentos,
                                ConsultarLancamentoCasoDeUso consultarLancamento,
                                RegistrarPagamentoCasoDeUso registrarPagamento,
                                PagarDespesaComReceitaColetivaCasoDeUso pagarComReceitaColetiva) {
        this.listarLancamentos = listarLancamentos;
        this.consultarLancamento = consultarLancamento;
        this.registrarPagamento = registrarPagamento;
        this.pagarComReceitaColetiva = pagarComReceitaColetiva;
    }

    @GetMapping
    public ResponseEntity<List<LancamentoSaida>> listar(
            @RequestParam(required = false) Long republicaId,
            @RequestParam(required = false) Long moradorId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        if (moradorId != null) {
            return ResponseEntity.ok(listarLancamentos.porMorador(moradorId));
        }
        if (republicaId != null && inicio != null && fim != null) {
            return ResponseEntity.ok(
                    listarLancamentos.porRepublicaEPeriodo(republicaId, inicio, fim));
        }
        if (republicaId != null) {
            return ResponseEntity.ok(listarLancamentos.porRepublica(republicaId));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{lancamentoId}")
    public ResponseEntity<LancamentoSaida> consultar(@PathVariable Long lancamentoId) {
        return ResponseEntity.ok(consultarLancamento.executar(lancamentoId));
    }

    @PostMapping("/{lancamentoId}/pagamentos")
    public ResponseEntity<LancamentoSaida> pagar(
            @PathVariable Long lancamentoId,
            @Valid @RequestBody RegistrarPagamentoRequisicao requisicao,
            @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario) {

        return ResponseEntity.ok(
                registrarPagamento.executar(lancamentoId, requisicao.moradorId(), usuario));
    }

    @PostMapping("/{lancamentoId}/pagamentos-receita-coletiva")
    public ResponseEntity<LancamentoSaida> pagarComReceitaColetiva(
            @PathVariable Long lancamentoId,
            @Valid @RequestBody PagarComReceitaColetivaRequisicao requisicao,
            @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario) {

        return ResponseEntity.ok(
                pagarComReceitaColetiva.executar(lancamentoId, requisicao.valorUtilizado(), usuario));
    }
}
