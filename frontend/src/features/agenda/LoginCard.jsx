import { useState } from 'react'
import { criarCredencial, requisicao } from '../../lib/api.js'

export default function LoginCard({ onLogin }) {
  const [usuario, setUsuario] = useState('')
  const [senha, setSenha] = useState('')
  const [erro, setErro] = useState('')
  const [entrando, setEntrando] = useState(false)

  async function entrar(event) {
    event.preventDefault()
    setEntrando(true)
    setErro('')
    const credential = criarCredencial(usuario, senha)
    try {
      await requisicao('/servicos', { credential })
      onLogin(credential, usuario)
    } catch (error) {
      setErro(error.message)
    } finally {
      setEntrando(false)
    }
  }

  return (
    <main className="login-screen">
      <section className="login-card" aria-labelledby="login-title">
        <div className="brand-mark brand-mark--large" aria-hidden="true">S</div>
        <p className="eyebrow">Salão-Escola</p>
        <h1 id="login-title">Entre na sua agenda</h1>
        <p className="login-card__copy">Use as credenciais de recepção configuradas no ambiente.</p>
        <form onSubmit={entrar} className="login-form">
          <label htmlFor="usuario">Usuário</label>
          <input id="usuario" autoComplete="username" value={usuario}
            onChange={(event) => setUsuario(event.target.value)} required />
          <label htmlFor="senha">Senha</label>
          <input id="senha" type="password" autoComplete="current-password" value={senha}
            onChange={(event) => setSenha(event.target.value)} required />
          {erro && <p className="form-error" role="alert">{erro}</p>}
          <button className="button button--primary button--wide" disabled={entrando}>
            {entrando ? 'Verificando…' : 'Entrar'}
          </button>
        </form>
        <p className="login-card__privacy">A sessão fica somente nesta aba e é apagada ao sair.</p>
      </section>
    </main>
  )
}
