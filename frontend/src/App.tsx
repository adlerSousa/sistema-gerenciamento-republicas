import { useEffect, useState } from 'react'
import { api, ErroDaApi } from './api/cliente'
import { Lancamentos } from './paginas/Lancamentos'
import { Notificacoes } from './paginas/Notificacoes'
import { Vagas } from './paginas/Vagas'
import type { Republica } from './tipos'

type Aba = 'lancamentos' | 'vagas' | 'notificacoes'

const ABAS: ReadonlyArray<{ chave: Aba; rotulo: string }> = [
  { chave: 'lancamentos', rotulo: 'Receitas e despesas' },
  { chave: 'vagas', rotulo: 'Vagas' },
  { chave: 'notificacoes', rotulo: 'Notificacoes' },
]

export function App() {
  const [republicas, setRepublicas] = useState<Republica[]>([])
  const [republicaSelecionada, setRepublicaSelecionada] = useState<number | null>(null)
  const [aba, setAba] = useState<Aba>('lancamentos')
  const [erro, setErro] = useState<string | null>(null)

  useEffect(() => {
    async function carregarRepublicas() {
      try {
        const carregadas = await api.listarRepublicas()
        setRepublicas(carregadas)
        setRepublicaSelecionada(carregadas[0]?.id ?? null)
      } catch (excecao) {
        setErro(
          excecao instanceof ErroDaApi ? excecao.message : 'Falha ao carregar as republicas.',
        )
      }
    }
    void carregarRepublicas()
  }, [])

  return (
    <main className="aplicacao">
      <header className="cabecalho">
        <h1>Gestao de Republicas</h1>
        <p>Sistema de gerenciamento de republicas estudantis</p>
      </header>

      {erro && <div className="mensagem erro">{erro}</div>}

      <div className="seletor">
        <label htmlFor="republica">Republica:</label>
        <select
          id="republica"
          value={republicaSelecionada ?? ''}
          onChange={(evento) => setRepublicaSelecionada(Number(evento.target.value))}
        >
          {republicas.map((republica) => (
            <option key={republica.id} value={republica.id}>
              {republica.nome}
            </option>
          ))}
        </select>
      </div>

      <nav className="abas" role="tablist">
        {ABAS.map(({ chave, rotulo }) => (
          <button
            key={chave}
            role="tab"
            aria-selected={aba === chave}
            onClick={() => setAba(chave)}
          >
            {rotulo}
          </button>
        ))}
      </nav>

      {aba === 'vagas' && <Vagas republicas={republicas} />}

      {republicaSelecionada !== null && aba === 'lancamentos' && (
        <Lancamentos republicaId={republicaSelecionada} />
      )}

      {republicaSelecionada !== null && aba === 'notificacoes' && (
        <Notificacoes republicaId={republicaSelecionada} />
      )}
    </main>
  )
}
