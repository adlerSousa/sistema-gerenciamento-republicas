package br.ufes.republicas.arquitetura;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Regras arquiteturais de cumprimento obrigatorio, conforme
 * {@code docs/CONVENCOES-TECNICAS.md}.
 *
 * <p>Estas regras constituem o instrumento de medicao da aderencia arquitetural
 * do codigo. Sao verificadas de forma isolada das demais etapas, por meio do
 * perfil Maven {@code arquitetura}, de modo que as violacoes arquiteturais sejam
 * medidas separadamente das falhas de compilacao e de teste.
 */
@Tag("arquitetura")
@DisplayName("Regras arquiteturais do projeto")
class RegrasDeArquiteturaArchTest {

    private static final String PACOTE_RAIZ = "br.ufes.republicas";

    private static final String DOMINIO = PACOTE_RAIZ + ".domain..";
    private static final String APLICACAO = PACOTE_RAIZ + ".application..";
    private static final String INFRAESTRUTURA = PACOTE_RAIZ + ".infrastructure..";
    private static final String APRESENTACAO = PACOTE_RAIZ + ".presentation..";

    private static JavaClasses classes;

    @BeforeAll
    static void importarClasses() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(PACOTE_RAIZ);
    }

    // ------------------------------------------------------------------
    // Independencia da camada de dominio
    // ------------------------------------------------------------------

    @Test
    @DisplayName("O dominio nao depende de nenhum framework")
    void oDominioNaoDependeDeNenhumFramework() {
        noClasses()
                .that().resideInAPackage(DOMINIO)
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence..",
                        "jakarta.validation..",
                        "com.fasterxml.jackson..",
                        "org.hibernate..")
                .because("a camada de dominio deve permanecer independente de tecnologia")
                .check(classes);
    }

    @Test
    @DisplayName("O dominio nao depende das demais camadas")
    void oDominioNaoDependeDasDemaisCamadas() {
        noClasses()
                .that().resideInAPackage(DOMINIO)
                .should().dependOnClassesThat()
                .resideInAnyPackage(APLICACAO, INFRAESTRUTURA, APRESENTACAO)
                .because("as dependencias devem apontar sempre para dentro")
                .check(classes);
    }

    // ------------------------------------------------------------------
    // Independencia da camada de aplicacao
    // ------------------------------------------------------------------

    @Test
    @DisplayName("A aplicacao nao depende da infraestrutura nem da apresentacao")
    void aAplicacaoNaoDependeDaInfraestruturaNemDaApresentacao() {
        noClasses()
                .that().resideInAPackage(APLICACAO)
                .should().dependOnClassesThat()
                .resideInAnyPackage(INFRAESTRUTURA, APRESENTACAO)
                .because("os casos de uso nao podem conhecer detalhes de tecnologia nem de entrega")
                .check(classes);
    }

    @Test
    @DisplayName("A aplicacao nao utiliza recursos de persistencia")
    void aAplicacaoNaoUtilizaRecursosDePersistencia() {
        noClasses()
                .that().resideInAPackage(APLICACAO)
                .should().dependOnClassesThat()
                .resideInAnyPackage("jakarta.persistence..", "org.hibernate..")
                .because("a persistencia e um detalhe da camada de infraestrutura")
                .check(classes);
    }

    // ------------------------------------------------------------------
    // Independencia da camada de apresentacao
    // ------------------------------------------------------------------

    @Test
    @DisplayName("A apresentacao nao depende da infraestrutura")
    void aApresentacaoNaoDependeDaInfraestrutura() {
        noClasses()
                .that().resideInAPackage(APRESENTACAO)
                .should().dependOnClassesThat()
                .resideInAPackage(INFRAESTRUTURA)
                .because("os controladores devem acessar o sistema apenas pelos casos de uso")
                .check(classes);
    }

    @Test
    @DisplayName("Os controladores nao acessam repositorios de persistencia")
    void osControladoresNaoAcessamRepositoriosDePersistencia() {
        noClasses()
                .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework.data..",
                        "jakarta.persistence..")
                .because("o acesso a dados deve ocorrer atraves dos casos de uso")
                .check(classes);
    }

    // ------------------------------------------------------------------
    // Localizacao das entidades de persistencia
    // ------------------------------------------------------------------

    @Test
    @DisplayName("As entidades JPA residem apenas no pacote de persistencia")
    void asEntidadesJpaResidemApenasNoPacoteDePersistencia() {
        classes()
                .that().areAnnotatedWith("jakarta.persistence.Entity")
                .should().resideInAPackage(PACOTE_RAIZ + ".infrastructure.persistence..")
                .because("o mapeamento objeto-relacional pertence a infraestrutura")
                .check(classes);
    }

    @Test
    @DisplayName("Os repositorios Spring Data residem apenas na infraestrutura")
    void osRepositoriosSpringDataResidemApenasNaInfraestrutura() {
        classes()
                .that().areAssignableTo("org.springframework.data.repository.Repository")
                .should().resideInAPackage(PACOTE_RAIZ + ".infrastructure.persistence..")
                .allowEmptyShould(true)
                .because("os detalhes de acesso a dados pertencem a infraestrutura")
                .check(classes);
    }

    // ------------------------------------------------------------------
    // Ausencia de ciclos
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Nao existem ciclos de dependencia entre os pacotes")
    void naoExistemCiclosDeDependenciaEntreOsPacotes() {
        SlicesRuleDefinition.slices()
                .matching(PACOTE_RAIZ + ".(*)..")
                .should().beFreeOfCycles()
                .check(classes);
    }
}
