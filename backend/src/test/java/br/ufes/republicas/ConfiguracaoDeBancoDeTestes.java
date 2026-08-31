package br.ufes.republicas;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Garante que os testes de integracao partam sempre do mesmo estado do banco de
 * dados.
 *
 * <p>Antes de aplicar as migracoes, o esquema de testes e removido por completo.
 * Sem isso, o resultado de um teste poderia depender dos dados deixados por
 * execucoes anteriores, o que tornaria a aprovacao em testes um indicador
 * instavel.
 */
@Configuration
@Profile("test")
public class ConfiguracaoDeBancoDeTestes {

    @Bean
    public FlywayMigrationStrategy recriarEsquemaAntesDeCadaExecucao() {
        return flyway -> {
            limpar(flyway);
            flyway.migrate();
        };
    }

    private void limpar(Flyway flyway) {
        flyway.clean();
    }
}
