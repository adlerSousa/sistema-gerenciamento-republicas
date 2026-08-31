<#
    Sequencia canonica de verificacao do codigo gerado (versao PowerShell).

    Ordem (conforme a metodologia do experimento):
      1. build
      2. testes unitarios
      3. testes de integracao
      4. regras arquiteturais

    Regra importante: quando a falha de build impede a execucao das etapas
    seguintes, essas etapas sao registradas como NAO_EXECUTADO, e nao como
    falha, para evitar dupla contagem da mesma ocorrencia.

    Uso:
      .\scripts\verificar.ps1 [-Saida <diretorio>]
#>

param(
    [string]$Saida = ""
)

$ErrorActionPreference = "Continue"

$Raiz = Split-Path -Parent $PSScriptRoot
if ([string]::IsNullOrEmpty($Saida)) {
    $Saida = Join-Path $Raiz "target-verificacao"
}
if (-not (Test-Path $Saida)) {
    New-Item -ItemType Directory -Force -Path $Saida | Out-Null
}

$build       = "NAO_EXECUTADO"
$unitarios   = "NAO_EXECUTADO"
$integracao  = "NAO_EXECUTADO"
$arquitetura = "NAO_EXECUTADO"

$inicio = Get-Date

Push-Location $Raiz

Write-Host "==> Subindo o banco de dados do ambiente canonico"
docker compose up -d db *> (Join-Path $Saida "00-banco.log")
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO: nao foi possivel subir o banco de dados. Verifique se o Docker Desktop esta em execucao."
    Get-Content (Join-Path $Saida "00-banco.log")
    Pop-Location
    exit 2
}

# ------------------------------------------------------------------ 1. build
Write-Host "==> Etapa 1/4: build"
docker compose run --rm build mvn -B -DskipTests clean package *> (Join-Path $Saida "01-build.log")
if ($LASTEXITCODE -eq 0) {
    $build = "APROVADO"
    Write-Host "    build APROVADO"
} else {
    $build = "FALHOU"
    Write-Host "    build FALHOU - etapas seguintes registradas como NAO_EXECUTADO"
}

if ($build -eq "APROVADO") {

    # --------------------------------------------------- 2. testes unitarios
    Write-Host "==> Etapa 2/4: testes unitarios"
    docker compose run --rm build mvn -B test *> (Join-Path $Saida "02-testes-unitarios.log")
    if ($LASTEXITCODE -eq 0) {
        $unitarios = "APROVADO"
        Write-Host "    testes unitarios APROVADO"
    } else {
        $unitarios = "FALHOU"
        Write-Host "    testes unitarios FALHOU"
    }

    # ------------------------------------------------- 3. testes de integracao
    Write-Host "==> Etapa 3/4: testes de integracao"
    docker compose run --rm build mvn -B verify *> (Join-Path $Saida "03-testes-integracao.log")
    if ($LASTEXITCODE -eq 0) {
        $integracao = "APROVADO"
        Write-Host "    testes de integracao APROVADO"
    } else {
        $integracao = "FALHOU"
        Write-Host "    testes de integracao FALHOU"
    }

    # ------------------------------------------------- 4. regras arquiteturais
    Write-Host "==> Etapa 4/4: regras arquiteturais"
    docker compose run --rm build mvn -B -Parquitetura test *> (Join-Path $Saida "04-arquitetura.log")
    if ($LASTEXITCODE -eq 0) {
        $arquitetura = "APROVADO"
        Write-Host "    regras arquiteturais APROVADO"
    } else {
        $arquitetura = "FALHOU"
        Write-Host "    regras arquiteturais FALHOU"
    }
}

$fim = Get-Date
$duracao = [int]($fim - $inicio).TotalSeconds

$commit = (git rev-parse HEAD 2>$null)
if ([string]::IsNullOrEmpty($commit)) { $commit = "desconhecido" }
$ramo = (git rev-parse --abbrev-ref HEAD 2>$null)
if ([string]::IsNullOrEmpty($ramo)) { $ramo = "desconhecido" }

$resultado = [ordered]@{
    momento          = (Get-Date).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
    ramo             = $ramo
    commit           = $commit
    duracao_segundos = $duracao
    etapas           = [ordered]@{
        build                = $build
        testes_unitarios     = $unitarios
        testes_integracao    = $integracao
        regras_arquiteturais = $arquitetura
    }
}

$resultado | ConvertTo-Json -Depth 5 |
    Out-File -FilePath (Join-Path $Saida "resultado-verificacao.json") -Encoding utf8

Write-Host ""
Write-Host "===================== RESUMO ====================="
Write-Host "  build ................. $build"
Write-Host "  testes unitarios ...... $unitarios"
Write-Host "  testes de integracao .. $integracao"
Write-Host "  regras arquiteturais .. $arquitetura"
Write-Host "=================================================="
Write-Host "Resultado gravado em: $(Join-Path $Saida 'resultado-verificacao.json')"

Pop-Location

if ($build -eq "APROVADO" -and $unitarios -eq "APROVADO" -and
    $integracao -eq "APROVADO" -and $arquitetura -eq "APROVADO") {
    exit 0
}
exit 1
