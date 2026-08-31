#!/usr/bin/env bash
#
# Sequencia canonica de verificacao do codigo gerado.
#
# Ordem (conforme a metodologia do experimento):
#   1. build
#   2. testes unitarios
#   3. testes de integracao
#   4. regras arquiteturais
#
# Regra importante: quando a falha de build impede a execucao das etapas
# seguintes, essas etapas sao registradas como NAO_EXECUTADO, e nao como
# falha, para evitar dupla contagem da mesma ocorrencia.
#
# Uso:
#   ./scripts/verificar.sh [diretorio-de-saida]
#
# Produz, no diretorio de saida, o arquivo resultado-verificacao.json e os
# registros completos de cada etapa.

set -uo pipefail

RAIZ="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SAIDA="${1:-$RAIZ/target-verificacao}"
COMPOSE="docker compose"

mkdir -p "$SAIDA"

BUILD="NAO_EXECUTADO"
UNITARIOS="NAO_EXECUTADO"
INTEGRACAO="NAO_EXECUTADO"
ARQUITETURA="NAO_EXECUTADO"

inicio=$(date +%s)

echo "==> Subindo o banco de dados do ambiente canonico"
$COMPOSE up -d db > "$SAIDA/00-banco.log" 2>&1
if [ $? -ne 0 ]; then
  echo "ERRO: nao foi possivel subir o banco de dados. Verifique se o Docker esta em execucao."
  cat "$SAIDA/00-banco.log"
  exit 2
fi

# ---------------------------------------------------------------- 1. build
echo "==> Etapa 1/4: build"
$COMPOSE run --rm build mvn -B -DskipTests clean package \
  > "$SAIDA/01-build.log" 2>&1
if [ $? -eq 0 ]; then
  BUILD="APROVADO"
  echo "    build APROVADO"
else
  BUILD="FALHOU"
  echo "    build FALHOU - etapas seguintes registradas como NAO_EXECUTADO"
fi

if [ "$BUILD" = "APROVADO" ]; then

  # ------------------------------------------------- 2. testes unitarios
  echo "==> Etapa 2/4: testes unitarios"
  $COMPOSE run --rm build mvn -B test \
    > "$SAIDA/02-testes-unitarios.log" 2>&1
  if [ $? -eq 0 ]; then
    UNITARIOS="APROVADO"
    echo "    testes unitarios APROVADO"
  else
    UNITARIOS="FALHOU"
    echo "    testes unitarios FALHOU"
  fi

  # ----------------------------------------------- 3. testes de integracao
  echo "==> Etapa 3/4: testes de integracao"
  $COMPOSE run --rm build mvn -B verify -DskipUnitTests \
    > "$SAIDA/03-testes-integracao.log" 2>&1
  if [ $? -eq 0 ]; then
    INTEGRACAO="APROVADO"
    echo "    testes de integracao APROVADO"
  else
    INTEGRACAO="FALHOU"
    echo "    testes de integracao FALHOU"
  fi

  # ----------------------------------------------- 4. regras arquiteturais
  echo "==> Etapa 4/4: regras arquiteturais"
  $COMPOSE run --rm build mvn -B -Parquitetura test \
    > "$SAIDA/04-arquitetura.log" 2>&1
  if [ $? -eq 0 ]; then
    ARQUITETURA="APROVADO"
    echo "    regras arquiteturais APROVADO"
  else
    ARQUITETURA="FALHOU"
    echo "    regras arquiteturais FALHOU"
  fi

fi

fim=$(date +%s)
duracao=$((fim - inicio))

commit=$(git -C "$RAIZ" rev-parse HEAD 2>/dev/null || echo "desconhecido")
ramo=$(git -C "$RAIZ" rev-parse --abbrev-ref HEAD 2>/dev/null || echo "desconhecido")

cat > "$SAIDA/resultado-verificacao.json" <<JSON
{
  "momento": "$(date -u +%Y-%m-%dT%H:%M:%SZ)",
  "ramo": "$ramo",
  "commit": "$commit",
  "duracao_segundos": $duracao,
  "etapas": {
    "build": "$BUILD",
    "testes_unitarios": "$UNITARIOS",
    "testes_integracao": "$INTEGRACAO",
    "regras_arquiteturais": "$ARQUITETURA"
  }
}
JSON

echo
echo "===================== RESUMO ====================="
echo "  build ................. $BUILD"
echo "  testes unitarios ...... $UNITARIOS"
echo "  testes de integracao .. $INTEGRACAO"
echo "  regras arquiteturais .. $ARQUITETURA"
echo "=================================================="
echo "Resultado gravado em: $SAIDA/resultado-verificacao.json"

if [ "$BUILD" = "APROVADO" ] && [ "$UNITARIOS" = "APROVADO" ] \
   && [ "$INTEGRACAO" = "APROVADO" ] && [ "$ARQUITETURA" = "APROVADO" ]; then
  exit 0
fi
exit 1
