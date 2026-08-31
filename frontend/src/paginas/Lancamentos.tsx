import { useEffect, useState } from 'react'
import { api, ErroDaApi } from '../api/cliente'
import type { Lancamento, Morador } from '../tipos'

interface Props {
  republicaId: number
}

const formatadorDeMoeda = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
})

function formatarData(data: string): string {
  const [ano, mes, dia] = data.split('-')
  return `${dia}/${mes}/${ano}`
}

/**
 * Consulta das receitas e despesas da republica e registro de pagamento das
 * parcelas devidas pelos moradores participantes (UC4, UC9 e UC10).
 */
export function Lancamentos({ republicaId }: Props) {
  const [lancamentos, setLancamentos] = useState<Lancamento[]>([])
  const [moradores, setMoradores] = useState<Morador[]>([])
  const [erro, setErro] = useState<string | null>(null)
  const [carregando, setCarregando] = useState(true)

  async function carregar() {
    setCarregando(true)
    setErro(null)
    try {
      const [lancamentosCarregados, moradoresCarregados] = await Promise.all([
        api.listarLancamentosDaRepublica(republicaId),
        api.listarMoradoresDaRepublica(republicaId),
      ])
      setLancamentos(lancamentosCarregados)
      setMoradores(moradoresCarregados)
    } catch (excecao) {
      setErro(excecao instanceof ErroDaApi ? excecao.message : 'Falha ao carregar os lancamentos.')
    } finally {
      setCarregando(false)
    }
  }

  useEffect(() => {
    void carregar()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [republicaId])

  async function pagar(lancamentoId: number, moradorId: number) {
    setErro(null)
    try {
      await api.registrarPagamento(lancamentoId, moradorId)
      await carregar()
    } catch (excecao) {
      setErro(excecao instanceof ErroDaApi ? excecao.message : 'Falha ao registrar o pagamento.')
    }
  }

  function nomeDoMorador(moradorId: number): string {
    return moradores.find((morador) => morador.id === moradorId)?.apelido ?? `Morador ${moradorId}`
  }

  if (carregando) {
    return <p className="vazio">Carregando os lancamentos...</p>
  }

  return (
    <section>
      {erro && <div className="mensagem erro">{erro}</div>}

      {lancamentos.length === 0 && (
        <p className="vazio">Nao ha receitas e despesas cadastradas.</p>
      )}

      {lancamentos.map((lancamento) => (
        <article className="cartao" key={lancamento.id}>
          <h3>
            {lancamento.descricao}{' '}
            <span className={`etiqueta ${lancamento.status.toLowerCase()}`}>
              {lancamento.status}
            </span>
          </h3>
          <p style={{ margin: '0 0 12px', color: 'var(--cor-texto-suave)', fontSize: '0.88rem' }}>
            {lancamento.tipo === 'DESPESA' ? 'Despesa' : 'Receita'} de{' '}
            {formatadorDeMoeda.format(lancamento.valor)} com vencimento em{' '}
            {formatarData(lancamento.dataVencimento)} · rateio por{' '}
            {lancamento.formaRateio === 'PERCENTUAL' ? 'percentual' : 'valor fixo'}
            {lancamento.numeroParcela !== undefined &&
              ` · parcela ${lancamento.numeroParcela} de ${lancamento.totalParcelas}`}
          </p>

          <table>
            <thead>
              <tr>
                <th>Morador</th>
                <th>Participacao</th>
                <th>Valor devido</th>
                <th>Situacao</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {lancamento.participacoes.map((participacao) => (
                <tr key={participacao.id}>
                  <td>{nomeDoMorador(participacao.moradorId)}</td>
                  <td>
                    {participacao.percentual !== undefined
                      ? `${participacao.percentual}%`
                      : formatadorDeMoeda.format(participacao.valorFixo ?? 0)}
                  </td>
                  <td>{formatadorDeMoeda.format(participacao.valorDevido)}</td>
                  <td>
                    {participacao.pago && participacao.dataPagamento
                      ? `Pago em ${formatarData(participacao.dataPagamento)}`
                      : participacao.pago
                        ? 'Pago'
                        : 'Pendente'}
                  </td>
                  <td>
                    <button
                      className="acao"
                      disabled={participacao.pago}
                      onClick={() => void pagar(lancamento.id, participacao.moradorId)}
                    >
                      Registrar pagamento
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </article>
      ))}
    </section>
  )
}
