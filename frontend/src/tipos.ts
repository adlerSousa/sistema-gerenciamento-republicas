/**
 * Representacoes dos recursos devolvidos pela API REST.
 *
 * A API omite do corpo da resposta os campos sem valor. Por isso, todo campo
 * opcional no dominio e declarado aqui como opcional, e nao apenas como
 * anulavel: em tempo de execucao ele chega como `undefined`, e nao como `null`.
 */

export type TipoLancamento = 'RECEITA' | 'DESPESA'

export type Periodicidade = 'UNICA' | 'SEMANAL' | 'MENSAL'

export type FormaRateio = 'PERCENTUAL' | 'VALOR_FIXO'

export type StatusLancamento = 'PENDENTE' | 'PAGO' | 'ESTORNADO'

export type TipoNotificacao =
  | 'VENCIMENTO_PROXIMO'
  | 'SOLICITACAO_MORADIA'
  | 'CONVITE_RECEBIDO'

export interface Republica {
  id: number
  nome: string
  dataFundacao: string
  bairro: string
  logradouro: string
  vantagens: string
  despesasMediasPorMorador: number
  totalVagas: number
  vagasOcupadas: number
  vagasDisponiveis: number
}

export interface Participacao {
  id: number
  moradorId: number
  percentual?: number
  valorFixo?: number
  valorDevido: number
  pago: boolean
  dataPagamento?: string
}

export interface Lancamento {
  id: number
  republicaId: number
  tipo: TipoLancamento
  descricao: string
  valor: number
  dataVencimento: string
  dataCadastro: string
  periodicidade: Periodicidade
  formaRateio: FormaRateio
  numeroParcela?: number
  totalParcelas?: number
  status: StatusLancamento
  participacoes: Participacao[]
}

export interface Morador {
  id: number
  nome: string
  apelido: string
  republicaId?: number
  semTeto: boolean
}

export interface Notificacao {
  id: number
  moradorId: number
  republicaId?: number
  tipo: TipoNotificacao
  mensagem: string
  lida: boolean
  dataCriacao: string
  lancamentoId?: number
}
