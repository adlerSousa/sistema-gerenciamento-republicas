package br.ufes.republicas.presentation;

import br.ufes.republicas.TesteDeIntegracao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica o comportamento dos recursos HTTP sobre a massa de dados inicial.
 */
@TesteDeIntegracao
@DisplayName("Recursos HTTP da API")
class RecursosHttpIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("lista as republicas cadastradas com o quantitativo de vagas")
    void deveListarRepublicasCadastradasComQuantitativoDeVagas() throws Exception {
        mockMvc.perform(get("/api/republicas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].nome").value("Republica Vila Velha"))
                .andExpect(jsonPath("$[0].vagasDisponiveis").value(2));
    }

    @Test
    @DisplayName("consulta uma republica pelo identificador")
    void deveConsultarRepublicaPeloIdentificador() throws Exception {
        mockMvc.perform(get("/api/republicas/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Republica Alto Alegre"))
                .andExpect(jsonPath("$.totalVagas").value(4));
    }

    @Test
    @DisplayName("responde com 404 ao consultar republica inexistente")
    void deveResponderNaoEncontradoAoConsultarRepublicaInexistente() throws Exception {
        mockMvc.perform(get("/api/republicas/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Nao encontrado"));
    }

    @Test
    @DisplayName("lista os moradores sem teto")
    void deveListarMoradoresSemTeto() throws Exception {
        mockMvc.perform(get("/api/moradores").param("semTeto", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].semTeto").value(true));
    }

    @Test
    @DisplayName("lista os lancamentos de uma republica com as participacoes no rateio")
    void deveListarLancamentosDeUmaRepublicaComAsParticipacoes() throws Exception {
        mockMvc.perform(get("/api/lancamentos").param("republicaId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].participacoes").isArray());
    }

    @Test
    @DisplayName("lista as tarefas de um morador responsavel")
    void deveListarTarefasDeUmMoradorResponsavel() throws Exception {
        mockMvc.perform(get("/api/tarefas").param("moradorId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("lista as reclamacoes de uma republica com a idade em dias das resolvidas")
    void deveListarReclamacoesComIdadeEmDiasDasResolvidas() throws Exception {
        mockMvc.perform(get("/api/reclamacoes").param("republicaId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }
}
