# Sistema de Gerenciamento de Repúblicas

Repositório-base do experimento controlado sobre os efeitos de especificações
estruturadas e orquestração multietapa na geração de código por modelos de
linguagem.

O sistema implementa o domínio de **gerenciamento de repúblicas estudantis**,
a partir da especificação de requisitos consolidada em disciplina de Engenharia
de Requisitos da UFES e adaptada para este trabalho.

---

## Papel deste repositório no experimento

Este repositório define o **estado inicial** sobre o qual as tarefas de evolução
são executadas. Todas as execuções experimentais partem do **mesmo commit**
(tag `base-v1.0`), em ramificação independente e ambiente isolado.

O repositório **não** contém as funcionalidades correspondentes às tarefas de
evolução avaliadas: essas ausências são deliberadas e documentadas, pois
constituem exatamente o que cada tarefa deve implementar.

> A suíte-oráculo utilizada na avaliação **não** faz parte deste repositório.
> Ela é mantida fora do contexto disponibilizado ao modelo e executada em etapa
> externa de avaliação, sobre a ramificação produzida.

## Arquitetura

Arquitetura Limpa, com dependências apontando sempre para dentro:

```
presentation  ──►  application  ──►  domain  ◄──  infrastructure
   (HTTP)          (casos de uso)    (regras)     (persistência, adaptadores)
```

| Camada | Pacote | Responsabilidade |
|---|---|---|
| Domínio | `br.ufes.republicas.domain` | Entidades, objetos de valor, regras de negócio e portas de repositório. Sem dependência de framework. |
| Aplicação | `br.ufes.republicas.application` | Casos de uso e orquestração das regras. |
| Infraestrutura | `br.ufes.republicas.infrastructure` | Persistência JPA/PostgreSQL, configuração, registro de operações. |
| Apresentação | `br.ufes.republicas.presentation` | Controladores HTTP da API REST. |

As restrições arquiteturais são verificadas automaticamente por regras ArchUnit.
O conjunto completo de convenções está em [`docs/CONVENCOES-TECNICAS.md`](docs/CONVENCOES-TECNICAS.md).

## Stack

- **Backend:** Java 21, Spring Boot 3.3.5, Maven
- **Banco de dados:** PostgreSQL 16, migrações Flyway
- **Frontend:** React 18, TypeScript, Vite
- **Testes:** JUnit 5, Spring Boot Test, ArchUnit

As versões estão fixadas em [`docs/MANIFESTO-VERSOES.md`](docs/MANIFESTO-VERSOES.md).

---

## Pré-requisitos

Apenas **Docker Desktop**. O JDK, o Maven, o PostgreSQL e o Node.js são fornecidos
pelas imagens do ambiente canônico — não é necessário instalá-los na máquina.

## Como executar a verificação canônica

Toda compilação e verificação deve ocorrer pelo ambiente Docker, para que os
resultados sejam idênticos em qualquer máquina.

Subir o banco de dados:

```bash
docker compose up -d db
```

Compilar e executar os testes de unidade e de integração:

```bash
docker compose run --rm build mvn -B verify
```

Executar somente as regras arquiteturais:

```bash
docker compose run --rm build mvn -B -Parquitetura test
```

Executar a sequência completa de verificação do experimento
(build, testes unitários, testes de integração e regras arquiteturais),
registrando o resultado de cada etapa:

```bash
./scripts/verificar.sh
```

No Windows, em PowerShell:

```powershell
.\scripts\verificar.ps1
```

Encerrar o ambiente:

```bash
docker compose down
```

## Como executar a aplicação

Subir a API (o banco sobe junto):

```bash
docker compose up app
```

A API fica disponível em `http://localhost:8080/api`.

Em outro terminal, subir a interface web:

```bash
docker compose run --rm --service-ports frontend npm run dev -- --host 0.0.0.0
```

A interface fica disponível em `http://localhost:5173`.

Para recomeçar de um banco limpo, com a massa de dados inicial:

```bash
docker compose down -v
```

> O serviço `app` usa o banco `republicas` e o serviço `build` usa o banco
> `republicas_test`. Essa separação é deliberada: os testes de integração
> recriam o esquema a cada execução, de modo que o resultado nunca dependa do
> estado deixado pela aplicação ou por execuções anteriores.

---

## Estrutura do repositório

```
.
├── backend/                  API REST em Java com Spring Boot
│   ├── src/main/java/        Código de produção, organizado por camada
│   ├── src/main/resources/   Configuração e migrações Flyway
│   └── src/test/java/        Testes de unidade, integração e regras arquiteturais
├── frontend/                 Interface web em React com TypeScript
├── docker/                   Scripts de inicialização do banco
├── docs/                     Convenções técnicas e manifesto de versões
├── scripts/                  Scripts da sequência de verificação
└── docker-compose.yml        Ambiente canônico de build e verificação
```

## Regras de negócio implementadas

O comportamento do sistema segue as regras da especificação de requisitos:

| Regra | Descrição |
|---|---|
| BR01 | República e seus dados de identificação |
| BR01.1 | Configuração de prazo de aviso de vencimento |
| BR02 | Morador e restrição de residência única |
| BR02.1 | Histórico de repúblicas do morador |
| BR02.2 | Reputação mensal do morador (ISS, IRT e ICP) |
| BR03 | Representante da república |
| BR04 | Perfil "sem teto" |
| BR05 | Receita coletiva e saldo acumulado |
| BR06 | Lançamentos de receitas e despesas, rateio e parcelamento |
| BR07 | Controle de vagas (VD = TV − VO) |
| BR08 | Tarefas domésticas |
| BR09 | Reclamações e sugestões |
