package br.ufes.republicas.presentation.republica;

import br.ufes.republicas.application.republica.AtualizarRepublicaCasoDeUso;
import br.ufes.republicas.application.republica.CadastrarRepublicaCasoDeUso;
import br.ufes.republicas.application.republica.ConsultarRepublicaCasoDeUso;
import br.ufes.republicas.application.republica.ListarRepublicasCasoDeUso;
import br.ufes.republicas.application.republica.RepublicaSaida;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Recursos HTTP de manutencao de republicas (UC1).
 */
@RestController
@RequestMapping("/api/republicas")
public class RepublicaController {

    private final CadastrarRepublicaCasoDeUso cadastrarRepublica;
    private final ConsultarRepublicaCasoDeUso consultarRepublica;
    private final ListarRepublicasCasoDeUso listarRepublicas;
    private final AtualizarRepublicaCasoDeUso atualizarRepublica;

    public RepublicaController(CadastrarRepublicaCasoDeUso cadastrarRepublica,
                               ConsultarRepublicaCasoDeUso consultarRepublica,
                               ListarRepublicasCasoDeUso listarRepublicas,
                               AtualizarRepublicaCasoDeUso atualizarRepublica) {
        this.cadastrarRepublica = cadastrarRepublica;
        this.consultarRepublica = consultarRepublica;
        this.listarRepublicas = listarRepublicas;
        this.atualizarRepublica = atualizarRepublica;
    }

    @PostMapping
    public ResponseEntity<RepublicaSaida> cadastrar(
            @Valid @RequestBody CadastrarRepublicaRequisicao requisicao) {

        RepublicaSaida saida = cadastrarRepublica.executar(requisicao.paraEntrada());
        return ResponseEntity
                .created(URI.create("/api/republicas/" + saida.id()))
                .body(saida);
    }

    @GetMapping("/{republicaId}")
    public ResponseEntity<RepublicaSaida> consultar(@PathVariable Long republicaId) {
        return ResponseEntity.ok(consultarRepublica.executar(republicaId));
    }

    @GetMapping
    public ResponseEntity<List<RepublicaSaida>> listar(
            @RequestParam(required = false) String nome) {

        List<RepublicaSaida> republicas = (nome == null || nome.isBlank())
                ? listarRepublicas.executar()
                : listarRepublicas.executarPorNome(nome);
        return ResponseEntity.ok(republicas);
    }

    @PutMapping("/{republicaId}")
    public ResponseEntity<RepublicaSaida> atualizar(
            @PathVariable Long republicaId,
            @Valid @RequestBody AtualizarRepublicaRequisicao requisicao,
            @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario) {

        return ResponseEntity.ok(
                atualizarRepublica.executar(republicaId, requisicao.paraEntrada(), usuario));
    }
}
