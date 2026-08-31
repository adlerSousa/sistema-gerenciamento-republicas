package br.ufes.republicas.presentation.morador;

import br.ufes.republicas.application.morador.AceitarSolicitacaoMoradiaCasoDeUso;
import br.ufes.republicas.application.morador.EnviarSolicitacaoMoradiaCasoDeUso;
import br.ufes.republicas.application.morador.MoradorSaida;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Recursos HTTP de convites e solicitacoes de moradia (UC5, UC14 e UC17).
 */
@RestController
@RequestMapping("/api/solicitacoes-moradia")
public class SolicitacaoMoradiaController {

    private final EnviarSolicitacaoMoradiaCasoDeUso enviarSolicitacao;
    private final AceitarSolicitacaoMoradiaCasoDeUso aceitarSolicitacao;

    public SolicitacaoMoradiaController(EnviarSolicitacaoMoradiaCasoDeUso enviarSolicitacao,
                                        AceitarSolicitacaoMoradiaCasoDeUso aceitarSolicitacao) {
        this.enviarSolicitacao = enviarSolicitacao;
        this.aceitarSolicitacao = aceitarSolicitacao;
    }

    @PostMapping
    public ResponseEntity<Void> enviar(@Valid @RequestBody EnviarSolicitacaoRequisicao requisicao) {
        Long solicitacaoId = enviarSolicitacao.executar(
                requisicao.republicaId(), requisicao.moradorId(), requisicao.origem());

        return ResponseEntity
                .created(URI.create("/api/solicitacoes-moradia/" + solicitacaoId))
                .build();
    }

    @PostMapping("/{solicitacaoId}/aceite")
    public ResponseEntity<MoradorSaida> aceitar(
            @PathVariable Long solicitacaoId,
            @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario) {

        return ResponseEntity.ok(aceitarSolicitacao.executar(solicitacaoId, usuario));
    }
}
