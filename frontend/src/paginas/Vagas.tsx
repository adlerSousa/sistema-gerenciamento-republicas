import type { Republica } from '../tipos'

interface Props {
  republicas: Republica[]
}

const formatadorDeMoeda = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
})

/**
 * Quadro de vagas das republicas cadastradas (BR07).
 */
export function Vagas({ republicas }: Props) {
  if (republicas.length === 0) {
    return <p className="vazio">Nao ha republicas cadastradas.</p>
  }

  return (
    <table>
      <thead>
        <tr>
          <th>Republica</th>
          <th>Bairro</th>
          <th>Despesa media por morador</th>
          <th>Total de vagas</th>
          <th>Vagas ocupadas</th>
          <th>Vagas disponiveis</th>
        </tr>
      </thead>
      <tbody>
        {republicas.map((republica) => (
          <tr key={republica.id}>
            <td>{republica.nome}</td>
            <td>{republica.bairro}</td>
            <td>{formatadorDeMoeda.format(republica.despesasMediasPorMorador)}</td>
            <td>{republica.totalVagas}</td>
            <td>{republica.vagasOcupadas}</td>
            <td>
              <span
                className={`etiqueta ${republica.vagasDisponiveis > 0 ? 'pago' : 'estornado'}`}
              >
                {republica.vagasDisponiveis}
              </span>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}
