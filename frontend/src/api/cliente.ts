import type { Lancamento, Morador, Notificacao, Republica } from '../tipos'

/**
 * Corpo padronizado de erro devolvido pela API.
 */
interface RespostaErro {
  status: number
  erro: string
  mensagem: string
}

/**
 * Erro originado em uma resposta da API.
 */
export class ErroDaApi extends Error {
  readonly status: number

  constructor(status: number, mensagem: string) {
    super(mensagem)
    this.name = 'ErroDaApi'
    this.status = status
  }
}

async function requisitar<T>(caminho: string, init?: RequestInit): Promise<T> {
  const resposta = await fetch(`/api${caminho}`, {
    headers: { 'Content-Type': 'application/json' },
    ...init,
  })

  if (!resposta.ok) {
    let mensagem = `A requisicao falhou com o codigo ${resposta.status}.`
    try {
      const corpo = (await resposta.json()) as RespostaErro
      mensagem = corpo.mensagem ?? mensagem
    } catch {
      // A resposta pode nao possuir corpo em JSON; a mensagem padrao e mantida.
    }
    throw new ErroDaApi(resposta.status, mensagem)
  }

  if (resposta.status === 204) {
    return undefined as T
  }
  return (await resposta.json()) as T
}

export const api = {
  listarRepublicas: () => requisitar<Republica[]>('/republicas'),

  listarMoradoresDaRepublica: (republicaId: number) =>
    requisitar<Morador[]>(`/moradores?republicaId=${republicaId}`),

  listarLancamentosDaRepublica: (republicaId: number) =>
    requisitar<Lancamento[]>(`/lancamentos?republicaId=${republicaId}`),

  registrarPagamento: (lancamentoId: number, moradorId: number) =>
    requisitar<Lancamento>(`/lancamentos/${lancamentoId}/pagamentos`, {
      method: 'POST',
      body: JSON.stringify({ moradorId }),
    }),

  gerarAvisosDeVencimento: (moradorId: number) =>
    requisitar<Notificacao[]>(`/moradores/${moradorId}/notificacoes/avisos-vencimento`, {
      method: 'POST',
    }),
}
