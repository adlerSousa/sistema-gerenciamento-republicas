package br.ufes.republicas.presentation.notificacao;

import br.ufes.republicas.application.notificacao.GerarAvisosDeVencimentoCasoDeUso;
import br.ufes.republicas.application.notificacao.NotificacaoSaida;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Recursos HTTP de notificacoes do morador (RF01 e RF02).
 */
@RestController
@RequestMapping("/api/moradores/{moradorId}/notificacoes")
public class NotificacaoController {

    private final GerarAvisosDeVencimentoCasoDeUso gerarAvisosDeVencimento;

    public NotificacaoController(GerarAvisosDeVencimentoCasoDeUso gerarAvisosDeVencimento) {
        this.gerarAvisosDeVencimento = gerarAvisosDeVencimento;
    }

    /**
     * Apura os lancamentos com vencimento proximo e registra os avisos
     * correspondentes, operacao realizada a cada acesso do morador ao sistema
     * (RF01).
     */
    @PostMapping("/avisos-vencimento")
    public ResponseEntity<List<NotificacaoSaida>> gerarAvisosDeVencimento(
            @PathVariable Long moradorId) {

        return ResponseEntity.ok(gerarAvisosDeVencimento.executar(moradorId));
    }
}
