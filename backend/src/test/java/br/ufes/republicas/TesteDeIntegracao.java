package br.ufes.republicas;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuracao comum dos testes de integracao.
 *
 * <p>Os testes sobem o contexto completo da aplicacao e utilizam o banco de
 * dados do ambiente canonico, sobre o qual as migracoes Flyway sao aplicadas.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("integracao")
public @interface TesteDeIntegracao {
}
