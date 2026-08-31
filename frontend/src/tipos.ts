/** Representacoes dos recursos devolvidos pela API REST. */

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
  percentual: number | null
  valorFixo: number | null
  valorDevido: number
  pago: boolean
  dataPagamento: string | null
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
  numeroParcela: number | null
  totalParcelas: number | null
  status: StatusLancamento
  participacoes: Participacao[]
}

export interface Morador {
  id: number
  nome: string
  apelido: string
  republicaId: number | null
  semTeto: boolean
}

export interface Notificacao {
  id: number
  moradorId: number
  republicaId: number | null
  tipo: TipoNotificacao
  mensagem: string
  lida: boolean
  dataCriacao: string
  lancamentoId: number | null
}
