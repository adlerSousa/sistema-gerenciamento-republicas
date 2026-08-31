# Convenções técnicas do projeto

> Documento **normativo e versionado**. Faz parte do contexto basal fornecido ao
> modelo em **todas** as condições experimentais, de modo que o modelo não precise
> redescobrir as convenções do sistema a cada execução.
>
> Estas convenções são **idênticas para todas as execuções** que utilizam o
> repositório-base e não mudam entre condições experimentais.

---

## 1. Stack e plataforma

- **Backend:** Java 21, Spring Boot 3.3.5, Maven.
- **Banco de dados:** PostgreSQL 16, migrações versionadas com Flyway.
- **Frontend:** React 18 com TypeScript, Vite.
- **Testes:** JUnit 5, Spring Boot Test, ArchUnit.
- **Build e verificação:** executados no ambiente Docker canônico do repositório.

## 2. Arquitetura em camadas (Arquitetura Limpa)

O sistema adota Arquitetura Limpa. As dependências apontam **sempre para dentro**:
camadas externas conhecem as internas; camadas internas **não** conhecem as externas.

```
presentation  ──►  application  ──►  domain  ◄──  infrastructure
   (HTTP)            (casos de uso)   (regras)      (persistência, adaptadores)
```

### 2.1 Pacote raiz

Todo código de produção reside sob:

```
br.ufes.republicas
```

### 2.2 Camadas e responsabilidades

| Pacote | Responsabilidade | Pode depender de |
|---|---|---|
| `br.ufes.republicas.domain` | Entidades, objetos de valor, regras de negócio, exceções de domínio e **interfaces** de repositório (portas). | Apenas Java padrão. |
| `br.ufes.republicas.application` | Casos de uso, orquestração das regras, DTOs de entrada e saída dos casos de uso. | `domain`. |
| `br.ufes.republicas.infrastructure` | Implementações de persistência (entidades JPA, repositórios, mapeadores), configuração, adaptadores externos, log. | `domain`, `application`. |
| `br.ufes.republicas.presentation` | Controladores HTTP, modelos de requisição/resposta, tratamento de erros HTTP. | `application`, `domain`. |

### 2.3 Restrições arquiteturais de cumprimento obrigatório

Estas restrições são verificadas automaticamente por regras ArchUnit
(`backend/src/test/java/br/ufes/republicas/arquitetura/`):

1. **`domain` não depende de framework algum.** É proibido, dentro de `domain`,
   qualquer referência a `org.springframework..`, `jakarta.persistence..`,
   `jakarta.validation..`, `com.fasterxml.jackson..` ou a qualquer outra camada
   do projeto.
2. **`domain` não depende de `application`, `infrastructure` nem `presentation`.**
3. **`application` não depende de `infrastructure` nem de `presentation`.**
4. **`application` não usa `jakarta.persistence`** — persistência é detalhe de
   infraestrutura.
5. **`presentation` não depende de `infrastructure`.** Controladores nunca acessam
   repositórios JPA, `EntityManager` ou entidades de persistência diretamente.
6. **Controladores não acessam persistência.** Classes anotadas com
   `@RestController` só invocam casos de uso da camada `application`.
7. **Entidades JPA vivem apenas em `infrastructure.persistence`.** Nenhuma classe
   anotada com `@Entity` pode estar fora desse pacote.
8. **Não há ciclos de dependência entre pacotes.**

> **Persistência e domínio são objetos distintos.** As entidades de domínio são
> classes Java puras. A camada de infraestrutura possui suas próprias entidades JPA
> (sufixo `Entity`) e **mapeadores** que convertem entre os dois mundos. Não se
> anota a entidade de domínio com `@Entity`.

## 3. Convenções de nomenclatura

| Elemento | Convenção | Exemplo |
|---|---|---|
| Entidade de domínio | Substantivo singular, sem sufixo | `Republica`, `Morador`, `Lancamento` |
| Objeto de valor | Substantivo singular | `Endereco`, `Dinheiro`, `Periodo` |
| Porta de repositório (interface, em `domain`) | `<Entidade>Repositorio` | `RepublicaRepositorio` |
| Implementação do repositório (em `infrastructure`) | `<Entidade>RepositorioJpa` | `RepublicaRepositorioJpa` |
| Entidade JPA (em `infrastructure`) | `<Entidade>Entity` | `RepublicaEntity` |
| Mapeador (em `infrastructure`) | `<Entidade>Mapper` | `RepublicaMapper` |
| Caso de uso (em `application`) | Verbo no infinitivo + `CasoDeUso` | `RegistrarDespesaCasoDeUso` |
| Entrada de caso de uso | `<CasoDeUso>Entrada` | `RegistrarDespesaEntrada` |
| Saída de caso de uso | `<CasoDeUso>Saida` | `RegistrarDespesaSaida` |
| Controlador HTTP | `<Recurso>Controller` | `LancamentoController` |
| Exceção de domínio | `<Motivo>Exception` | `VagaIndisponivelException` |
| Classe de teste | `<ClasseTestada>Test` | `RepublicaTest` |
| Migração Flyway | `V<n>__<descricao_snake_case>.sql` | `V7__adiciona_prazo_aviso.sql` |

**Idioma:** o domínio é escrito em **português** (nomes de classes, métodos,
atributos, tabelas e colunas), acompanhando a linguagem da especificação de
requisitos. Palavras reservadas e APIs de terceiros permanecem em inglês.
**Não** se usa acentuação nem cedilha em identificadores de código: `Republica`,
não `República`.

## 4. Organização de pacotes por módulo funcional

Dentro de cada camada, o código é agrupado por módulo funcional do sistema:

```
br.ufes.republicas
├── domain
│   ├── republica
│   ├── morador
│   ├── financeiro
│   ├── tarefa
│   ├── reclamacao
│   ├── reputacao
│   ├── notificacao
│   └── comum
├── application
│   ├── republica
│   ├── morador
│   ├── financeiro
│   ├── tarefa
│   ├── reclamacao
│   ├── reputacao
│   └── notificacao
├── infrastructure
│   ├── persistence
│   ├── log
│   └── config
└── presentation
    ├── republica
    ├── morador
    ├── financeiro
    ├── tarefa
    ├── reclamacao
    ├── reputacao
    ├── notificacao
    └── comum
```

## 5. Dependências permitidas

**É permitido usar apenas as dependências já declaradas em `backend/pom.xml`.**

Dependências atualmente disponíveis:

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-validation`
- `flyway-core`, `flyway-database-postgresql`
- `postgresql` (driver JDBC)
- `spring-boot-starter-test` (escopo de teste)
- `archunit-junit5` (escopo de teste)

**É proibido acrescentar novas dependências ao `pom.xml`** durante as tarefas de
evolução. Se uma tarefa parecer exigir uma biblioteca nova, a solução deve ser
implementada com os recursos já disponíveis (Java 21 padrão + dependências acima).

**Bibliotecas explicitamente não disponíveis** (não devem ser referenciadas):
Lombok, MapStruct, ModelMapper, Apache Commons, Guava, Hibernate Validator fora do
starter de validação, bibliotecas de mock além das contidas em
`spring-boot-starter-test`.

## 6. Convenções da API REST

- Rotas em **plural**, prefixadas por `/api`: `/api/republicas`, `/api/lancamentos`.
- Recursos aninhados quando houver relação de posse:
  `/api/republicas/{republicaId}/moradores`.
- Verbos HTTP: `GET` (consulta), `POST` (criação), `PUT` (substituição),
  `PATCH` (atualização parcial), `DELETE` (remoção).
- Códigos de resposta:
  - `200 OK` — consulta ou atualização bem-sucedida.
  - `201 Created` — criação bem-sucedida, com cabeçalho `Location`.
  - `204 No Content` — remoção bem-sucedida.
  - `400 Bad Request` — dados de entrada inválidos.
  - `404 Not Found` — recurso inexistente.
  - `409 Conflict` — violação de regra de negócio (por exemplo, ausência de vaga).
  - `422 Unprocessable Entity` — entrada sintaticamente válida, semanticamente incorreta.
- Corpo de erro padronizado, produzido pelo tratador global de exceções:

```json
{
  "timestamp": "2026-08-31T14:30:00Z",
  "status": 409,
  "erro": "Conflito",
  "mensagem": "Nao ha vaga disponivel na republica informada.",
  "caminho": "/api/republicas/1/moradores"
}
```

- Datas em ISO-8601 (`yyyy-MM-dd` para data, `yyyy-MM-dd'T'HH:mm:ss'Z'` para instante).
- Valores monetários em `BigDecimal`, com escala 2 e arredondamento `HALF_UP`.
  **Nunca** usar `double` ou `float` para dinheiro.

## 7. Persistência

- Toda alteração de esquema é feita por **migração Flyway versionada**, em
  `backend/src/main/resources/db/migration/`.
- **É proibido** editar uma migração já existente; toda mudança gera um novo arquivo
  `V<n>__<descricao>.sql` com o próximo número livre.
- `spring.jpa.hibernate.ddl-auto` é `validate`. O Hibernate **não** cria nem altera
  o esquema.
- Nomes de tabelas e colunas em `snake_case`, sem acentuação: `republica`,
  `vagas_ocupadas`, `data_vencimento`.
- Chaves primárias: coluna `id`, tipo `BIGINT`, geradas por identidade.

## 8. Padrões de teste

- Testes ficam em `backend/src/test/java`, espelhando o pacote da classe testada.
- **Testes de unidade** do domínio não sobem contexto Spring e não tocam o banco.
- **Testes de integração** usam `@SpringBootTest` e o PostgreSQL do ambiente Docker.
- Nomes de métodos de teste descrevem o comportamento esperado em português, no
  formato `deve<Comportamento>Quando<Condicao>`:

```java
@Test
void deveRejeitarIngressoQuandoNaoHouverVagaDisponivel() { ... }
```

- Cada teste segue a estrutura **preparação / execução / verificação**.
- Regras arquiteturais ficam em `backend/src/test/java/br/ufes/republicas/arquitetura/`.

## 9. Registro de operações (log)

Conforme a especificação de requisitos (RF09), as operações de **aceite de convite**,
**registro de lançamentos**, **registro de conclusão de tarefa** e **manutenção de
república** são registradas em arquivo de log.

- O contrato de log é a interface `RegistradorOperacoes`, em
  `br.ufes.republicas.domain.comum`.
- As implementações (formatos CSV, JSON e XML) ficam em
  `br.ufes.republicas.infrastructure.log`.
- Mensagem de sucesso:
  `"<OPERACAO> do contato <NOME>, (<DATA>, <HORA>, e <USUARIO>)"`
- Mensagem de falha:
  `"Ocorreu a falha <MENSAGEM DA FALHA> ao realizar a \"<OPERACAO> do contato <NOME>, (<DATA>, <HORA>, e <USUARIO>).\""`
- `DATA` no formato `dd/MM/yyyy`; `HORA` no formato `HH:mm:ss`.

## 10. Estilo de código

- Indentação de 4 espaços em Java; 2 espaços em TypeScript, JSON, YAML e SQL.
- Limite indicativo de 120 colunas por linha.
- Uma classe pública por arquivo.
- Ordem dos membros de uma classe: constantes, atributos, construtores, métodos
  públicos, métodos privados.
- Campos que não mudam após a construção são declarados `final`.
- Comentários explicam **por que**, não **o que**. Código autoexplicativo dispensa
  comentário.
- **Não** se usa `System.out.println` em código de produção.

## 11. Fora do escopo deste projeto

A especificação de requisitos original foi redigida para uma aplicação **desktop**
(Java Swing, SQLite, padrão DAO). Este repositório-base implementa o **mesmo
domínio** em arquitetura web, conforme definido na metodologia do trabalho.
Portanto, **não** se aplicam a este projeto:

- interface Java Swing e padrão de janelas MDI;
- persistência em SQLite e padrão DAO manual;
- exigência dos padrões Observer, Decorator, State, Memento e Command;
- biblioteca externa de validação de senha distribuída como `jar`.

O domínio, as regras de negócio (BR01 a BR09), os casos de uso (UC1 a UC18) e o
comportamento funcional descrito na especificação **permanecem válidos** e são a
referência normativa do sistema.
