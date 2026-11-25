import { useEffect, useState } from 'react'
import StatusBadge from '../../components/StatusBadge.jsx'
import { requisicao } from '../../lib/api.js'
import AgendamentoForm from './AgendamentoForm.jsx'
import LoginCard from './LoginCard.jsx'
import { hojeLocal, useAgenda } from './useAgenda.js'

function dataPorExtenso(data) {
  return new Intl.DateTimeFormat('pt-BR', {
    weekday: 'long',
    day: 'numeric',
    month: 'long',
    timeZone: 'UTC',
  }).format(new Date(data + 'T12:00:00Z'))
}

function horaBrasilia(valor) {
  return new Intl.DateTimeFormat('pt-BR', {
    hour: '2-digit',
    minute: '2-digit',
    timeZone: 'America/Sao_Paulo',
  }).format(new Date(valor))
}

function AgendaDashboard({ credential, usuario, onLogout }) {
  const [data, setData] = useState(hojeLocal())
  const [health, setHealth] = useState('verificando')
  const { servicos, profissionais, agendamentos, carregando, erro, atualizar, cancelar, criar } =
    useAgenda(credential, data)

  useEffect(() => {
    requisicao('/health').then(() => setHealth('conectada')).catch(() => setHealth('indisponível'))
  }, [])

  const ativos = agendamentos.filter((item) => item.status === 'ATIVO')
  const cancelados = agendamentos.length - ativos.length

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <a href="/" className="brand" aria-label="Agenda do Salão-Escola, início">
          <span className="brand-mark" aria-hidden="true">S</span>
          <span className="brand-name">salão<span>escola</span></span>
        </a>
        <div className="sidebar-divider" />
        <p className="sidebar-label">Operação</p>
        <a href="#agenda" className="nav-link nav-link--active">
          <span aria-hidden="true" className="nav-icon">▦</span>
          Agenda diária
        </a>
        <div className="sidebar-note">
          <span className="sidebar-note__spark" aria-hidden="true">✳</span>
          <strong>Atendimento organizado</strong>
          <span>Confira os horários antes de confirmar um novo serviço.</span>
        </div>
        <div className="sidebar-user">
          <div className="avatar" aria-hidden="true">{usuario.slice(0, 1).toUpperCase()}</div>
          <div className="sidebar-user__details">
            <strong>{usuario}</strong>
            <span>Recepção</span>
          </div>
          <button className="icon-button" onClick={onLogout} aria-label="Sair da conta" title="Sair">
            ↗
          </button>
        </div>
      </aside>

      <main className="main-content" id="agenda">
        <header className="topbar">
          <div className="breadcrumb">Salão-Escola <span>/</span> <strong>Agenda</strong></div>
          <div className="connection-pill">
            <span className={'connection-dot connection-dot--' + health} />
            Banco {health === 'conectada' ? 'conectado' : health === 'indisponível' ? 'indisponível' : 'conectando'}
          </div>
        </header>

        <section className="page-intro">
          <div>
            <p className="eyebrow">Visão da recepção</p>
            <h1>Agenda do dia</h1>
            <p className="page-intro__date">{dataPorExtenso(data)}</p>
          </div>
          <label className="date-picker" htmlFor="data-agenda">
            <span aria-hidden="true">▦</span>
            <input id="data-agenda" type="date" value={data}
              onChange={(event) => setData(event.target.value)} />
          </label>
        </section>

        <section className="summary-grid" aria-label="Resumo do dia">
          <article className="summary-card summary-card--green">
            <div className="summary-card__top"><span>Atendimentos ativos</span><span aria-hidden="true">◷</span></div>
            <strong>{ativos.length.toString().padStart(2, '0')}</strong>
            <span className="summary-card__foot">para esta data</span>
          </article>
          <article className="summary-card">
            <div className="summary-card__top"><span>Profissionais</span><span aria-hidden="true">♙</span></div>
            <strong>{profissionais.length.toString().padStart(2, '0')}</strong>
            <span className="summary-card__foot">na equipe fictícia</span>
          </article>
          <article className="summary-card">
            <div className="summary-card__top"><span>Serviços</span><span aria-hidden="true">✳</span></div>
            <strong>{servicos.length.toString().padStart(2, '0')}</strong>
            <span className="summary-card__foot">no catálogo inicial</span>
          </article>
          <article className="summary-card">
            <div className="summary-card__top"><span>Cancelados</span><span aria-hidden="true">↺</span></div>
            <strong>{cancelados.toString().padStart(2, '0')}</strong>
            <span className="summary-card__foot">histórico preservado</span>
          </article>
        </section>

        <div className="workspace-grid">
          <section className="panel agenda-panel" aria-labelledby="appointments-title">
            <div className="panel-heading panel-heading--agenda">
              <div>
                <p className="eyebrow">Programação</p>
                <h2 id="appointments-title">Atendimentos</h2>
              </div>
              <button className="button button--secondary" onClick={atualizar} disabled={carregando}>
                <span aria-hidden="true">↻</span> Atualizar
              </button>
            </div>
            {erro && <p className="notice notice--error" role="alert">{erro}</p>}
            {carregando ? (
              <div className="empty-state"><span className="loader" />Carregando a agenda…</div>
            ) : agendamentos.length === 0 ? (
              <div className="empty-state">
                <span className="empty-state__icon" aria-hidden="true">◷</span>
                <strong>Nenhum atendimento nesta data</strong>
                <span>Quando a recepção criar um horário, ele aparecerá aqui.</span>
              </div>
            ) : (
              <div className="appointment-list">
                {agendamentos.map((item) => (
                  <article className={'appointment-card' + (item.status === 'CANCELADO' ? ' appointment-card--cancelled' : '')}
                    key={item.id}>
                    <div className="appointment-time">
                      <strong>{horaBrasilia(item.dataHoraInicio)}</strong>
                      <span>{horaBrasilia(item.dataHoraFim)}</span>
                    </div>
                    <div className="appointment-line" aria-hidden="true" />
                    <div className="appointment-details">
                      <div className="appointment-details__title">
                        <strong>{item.cliente}</strong>
                        <StatusBadge status={item.status} />
                      </div>
                      <span>{item.servico} · {item.duracaoMinutos} min</span>
                      <span>Profissional: {item.profissional}</span>
                    </div>
                    {item.status === 'ATIVO' && (
                      <button className="text-button" onClick={() => cancelar(item.id)}>
                        Cancelar
                      </button>
                    )}
                  </article>
                ))}
              </div>
            )}
            <div className="agenda-footnote">
              <span aria-hidden="true">◷</span> Expediente: segunda a sábado, das 08h às 20h · Horário de Brasília
            </div>
          </section>
          <AgendamentoForm data={data} servicos={servicos} profissionais={profissionais} onSubmit={criar} />
        </div>
        <footer className="page-footer">
          <span>Agenda do Salão-Escola</span>
          <span>Cloud Challenge · Desafio 03</span>
        </footer>
      </main>
    </div>
  )
}

export default function AgendaPage() {
  const [sessao, setSessao] = useState(null)
  if (!sessao) return <LoginCard onLogin={(credential, usuario) => setSessao({ credential, usuario })} />
  return (
    <AgendaDashboard
      credential={sessao.credential}
      usuario={sessao.usuario}
      onLogout={() => setSessao(null)}
    />
  )
}
