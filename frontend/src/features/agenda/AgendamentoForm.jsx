import { useState } from 'react'

function dataHoraBrasilia(data, hora) {
  return data + 'T' + hora + ':00-03:00'
}

export default function AgendamentoForm({ data, servicos, profissionais, onSubmit }) {
  const [cliente, setCliente] = useState('')
  const [servicoId, setServicoId] = useState('')
  const [profissionalId, setProfissionalId] = useState('')
  const [hora, setHora] = useState('09:00')
  const [erro, setErro] = useState('')
  const [salvando, setSalvando] = useState(false)

  async function salvar(event) {
    event.preventDefault()
    setErro('')
    setSalvando(true)
    try {
      await onSubmit({
        cliente: cliente.trim(),
        servicoId: Number(servicoId),
        profissionalId: Number(profissionalId),
        dataHoraInicio: dataHoraBrasilia(data, hora),
      })
      setCliente('')
      setServicoId('')
      setProfissionalId('')
      setHora('09:00')
    } catch (error) {
      setErro(error.message)
    } finally {
      setSalvando(false)
    }
  }

  return (
    <section className="panel booking-panel" aria-labelledby="booking-title">
      <div className="panel-heading">
        <div>
          <p className="eyebrow">Novo atendimento</p>
          <h2 id="booking-title">Criar agendamento</h2>
        </div>
        <span className="panel-icon" aria-hidden="true">＋</span>
      </div>
      <form onSubmit={salvar} className="booking-form">
        <label htmlFor="cliente">Nome fictício da cliente</label>
        <input id="cliente" maxLength="100" placeholder="Ex.: Ana Souza" value={cliente}
          onChange={(event) => setCliente(event.target.value)} required />

        <div className="form-grid">
          <div>
            <label htmlFor="servico">Serviço</label>
            <select id="servico" value={servicoId}
              onChange={(event) => setServicoId(event.target.value)} required>
              <option value="">Selecione</option>
              {servicos.map((servico) => (
                <option key={servico.id} value={servico.id}>
                  {servico.nome} · {servico.duracaoMinutos} min
                </option>
              ))}
            </select>
          </div>
          <div>
            <label htmlFor="profissional">Profissional</label>
            <select id="profissional" value={profissionalId}
              onChange={(event) => setProfissionalId(event.target.value)} required>
              <option value="">Selecione</option>
              {profissionais.map((profissional) => (
                <option key={profissional.id} value={profissional.id}>{profissional.nome}</option>
              ))}
            </select>
          </div>
        </div>

        <div>
          <label htmlFor="hora">Horário de início</label>
          <input id="hora" type="time" min="08:00" max="19:59" value={hora}
            onChange={(event) => setHora(event.target.value)} required />
          <p className="field-hint">De segunda a sábado, das 08h às 20h. O sistema calcula o fim.</p>
        </div>
        {erro && <p className="form-error" role="alert">{erro}</p>}
        <button className="button button--primary button--wide" disabled={salvando}>
          {salvando ? 'Salvando…' : 'Confirmar agendamento'}
        </button>
      </form>
    </section>
  )
}
