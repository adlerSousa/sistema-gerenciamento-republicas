import { useEffect, useState } from 'react'
import { api, ErroDaApi } from '../api/cliente'
import type { Morador, Notificacao } from '../tipos'

interface Props {
  republicaId: number
}

function formatarData(data: string): string {
  const [ano, mes, dia] = data.split('-')
  return `${dia}/${mes}/${ano}`
}

/**
 * Avisos de lancamentos com vencimento proximo, apurados a cada acesso do
 * morador ao sistema (RF01).
 */
export function Notificacoes({ republicaId }: Props) {
  const [moradores, setMoradores] = useState<Morador[]>([])
  const [moradorSelecionado, setMoradorSelecionado] = useState<number | null>(null)
  const [avisos, setAvisos] = useState<Notificacao[]>([])
  const [erro, setErro] = useState<string | null>(null)
  const [carregando, setCarregando] = useState(false)

  useEffect(() => {
    async function carregarMoradores() {
      setErro(null)
      try {
        const carregados = await api.listarMoradoresDaRepublica(republicaId)
        setMoradores(carregados)
        setMoradorSelecionado(carregados[0]?.id ?? null)
        setAvisos([])
      } catch (excecao) {
        setErro(excecao instanceof ErroDaApi ? excecao.message : 'Falha ao carregar os moradores.')
      }
    }
    void carregarMoradores()
  }, [republicaId])

  async function apurarAvisos() {
    if (moradorSelecionado === null) {
      return
    }
    setCarregando(true)
    setErro(null)
    try {
      setAvisos(await api.gerarAvisosDeVencimento(moradorSelecionado))
    } catch (excecao) {
      setErro(excecao instanceof ErroDaApi ? excecao.message : 'Falha ao apurar os avisos.')
    } finally {
      setCarregando(false)
    }
  }

  return (
    <section>
      {erro && <div className="mensagem erro">{erro}</div>}

      <div className="seletor">
        <label htmlFor="morador">Morador:</label>
        <select
          id="morador"
          value={moradorSelecionado ?? ''}
          onChange={(evento) => setMoradorSelecionado(Number(evento.target.value))}
        >
          {moradores.map((morador) => (
            <option key={morador.id} value={morador.id}>
              {morador.nome}
            </option>
          ))}
        </select>
        <button className="acao" onClick={() => void apurarAvisos()} disabled={carregando}>
          {carregando ? 'Apurando...' : 'Apurar avisos de vencimento'}
        </button>
      </div>

      {avisos.length === 0 ? (
        <p className="vazio">
          Nenhum aviso novo. Os avisos ja emitidos anteriormente nao sao repetidos.
        </p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Tipo</th>
              <th>Mensagem</th>
              <th>Data</th>
            </tr>
          </thead>
          <tbody>
            {avisos.map((aviso) => (
              <tr key={aviso.id}>
                <td>
                  <span className="etiqueta pendente">{aviso.tipo}</span>
                </td>
                <td>{aviso.mensagem}</td>
                <td>{formatarData(aviso.dataCriacao)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </section>
  )
}
