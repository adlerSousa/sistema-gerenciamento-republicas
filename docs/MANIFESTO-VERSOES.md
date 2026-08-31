# Manifesto de versões do experimento

> Este documento fixa **todas** as versões de ferramentas, bibliotecas e componentes
> utilizadas no experimento. Ele existe para garantir que as 54 execuções principais
> partam de um ambiente idêntico e que o estudo seja replicável por terceiros.
>
> **Regra:** nenhuma versão listada aqui pode ser alterada após o congelamento do
> repositório-base (tag `base-v1.0`). Qualquer alteração invalida as execuções já
> realizadas e exige reinício da coleta.

## 1. Plataforma de execução da geração

| Item | Versão fixada | Como verificar |
|---|---|---|
| Antigravity CLI | *(a preencher no congelamento)* | `antigravity --version` |
| Modelo de linguagem | Gemini 3.1 Pro | identificador exibido pela ferramenta na chamada |
| OpenSpec | 1.4.1 | `openspec --version` |
| Skills (Agent Skills — Tech Leads Club) | *(a preencher: commit/tag)* | escopo local do projeto |

**Restrição metodológica:** não é utilizada nenhuma *Skill* que produza, altere ou
revise artefatos de especificação, planejamento, tarefas ou validação próprios do
OpenSpec, pois essas atividades **integram os tratamentos comparados**.

As *Skills* instaladas são **idênticas nas três condições experimentais**.

## 2. Ambiente canônico de build e verificação

Toda compilação, execução de testes, verificação de regras arquiteturais e execução
da suíte-oráculo ocorre **dentro do ambiente Docker definido neste repositório**,
nunca diretamente na máquina do pesquisador. Isso torna os resultados de
"compilou / passou nos testes / violou arquitetura" idênticos em qualquer máquina.

| Item | Versão fixada |
|---|---|
| Imagem base de build | `maven:3.9.9-eclipse-temurin-21` |
| JDK | Eclipse Temurin 21 (LTS) |
| Maven | 3.9.9 |
| PostgreSQL | 16.4 (`postgres:16.4-alpine`) |
| Node.js (frontend) | 22 (`node:22-alpine`) |

## 3. Dependências principais do backend

| Dependência | Versão |
|---|---|
| Spring Boot | 3.3.5 |
| Spring Framework | (gerenciado pelo Spring Boot) |
| Flyway | (gerenciado pelo Spring Boot) |
| PostgreSQL JDBC Driver | (gerenciado pelo Spring Boot) |
| ArchUnit (JUnit 5) | 1.3.0 |
| JUnit | 5 (via `spring-boot-starter-test`) |
| Testcontainers | (gerenciado pelo Spring Boot) |

> As versões marcadas como "gerenciado pelo Spring Boot" são resolvidas pelo BOM do
> Spring Boot 3.3.5 e, portanto, também ficam fixas enquanto essa versão não mudar.
> O arquivo `backend/pom.xml` é a fonte normativa.

## 4. Dependências principais do frontend

| Dependência | Versão |
|---|---|
| React | 18.3.x |
| TypeScript | 5.6.x |
| Vite | 5.4.x |

> O arquivo `frontend/package.json` e o `package-lock.json` são a fonte normativa.

## 5. Repositório-base

| Item | Valor |
|---|---|
| Tag do commit-base | `base-v1.0` *(a preencher no congelamento)* |
| Hash do commit-base | *(a preencher no congelamento)* |

Todas as execuções experimentais partem **deste mesmo commit**, em ramificação
independente e ambiente isolado.

## 6. Registro de congelamento

| Campo | Valor |
|---|---|
| Data do congelamento | *(a preencher)* |
| Responsável | Adler Amorim De Sousa |
| Janela temporal da coleta | *(a preencher)* |

**Procedimento se o modelo mudar de versão durante a coleta:** a execução afetada é
descartada ou repetida, conforme a regra estabelecida antes do início da coleta
(ver `docs/PROTOCOLO-EXECUCAO.md`), e a ocorrência é registrada.
