package br.ufes.republicas.presentation.morador;

import br.ufes.republicas.application.morador.ListarMoradoresCasoDeUso;
import br.ufes.republicas.application.morador.MoradorSaida;
import br.ufes.republicas.application.morador.RemoverMoradorDaRepublicaCasoDeUso;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Recursos HTTP de consulta e manutencao de moradores (UC2 e UC7).
 */
@RestController
@RequestMapping("/api/moradores")
public class MoradorController {

    private final ListarMoradoresCasoDeUso listarMoradores;
    private final RemoverMoradorDaRepublicaCasoDeUso removerMoradorDaRepublica;

    public MoradorController(ListarMoradoresCasoDeUso listarMoradores,
                             RemoverMoradorDaRepublicaCasoDeUso removerMoradorDaRepublica) {
        this.listarMoradores = listarMoradores;
        this.removerMoradorDaRepublica = removerMoradorDaRepublica;
    }

    @GetMapping
    public ResponseEntity<List<MoradorSaida>> listar(
            @RequestParam(required = false) Long republicaId,
            @RequestParam(required = false, defaultValue = "false") boolean semTeto) {

        if (semTeto) {
            return ResponseEntity.ok(listarMoradores.semTeto());
        }
        if (republicaId != null) {
            return ResponseEntity.ok(listarMoradores.porRepublica(republicaId));
        }
        return ResponseEntity.ok(listarMoradores.todos());
    }

    @GetMapping("/{moradorId}")
    public ResponseEntity<MoradorSaida> consultar(@PathVariable Long moradorId) {
        return ResponseEntity.ok(listarMoradores.porId(moradorId));
    }

    @DeleteMapping("/{moradorId}/republica")
    public ResponseEntity<MoradorSaida> removerDaRepublica(
            @PathVariable Long moradorId,
            @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario) {

        return ResponseEntity.ok(removerMoradorDaRepublica.executar(moradorId, usuario));
    }
}
