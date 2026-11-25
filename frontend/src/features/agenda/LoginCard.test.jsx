import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import LoginCard from './LoginCard.jsx'

describe('LoginCard', () => {
  afterEach(() => {
    cleanup()
  })

  beforeEach(() => {
    vi.restoreAllMocks()
  })

  it('entra quando a API confirma as credenciais', async () => {
    const onLogin = vi.fn()
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ ok: true, json: async () => [] }))
    render(<LoginCard onLogin={onLogin} />)

    fireEvent.change(screen.getByLabelText('Usuário'), { target: { value: 'recepcao' } })
    fireEvent.change(screen.getByLabelText('Senha'), { target: { value: 'segredo' } })
    fireEvent.click(screen.getByRole('button', { name: 'Entrar' }))

    await waitFor(() => expect(onLogin).toHaveBeenCalledOnce())
    expect(onLogin.mock.calls[0][0]).toMatch(/^Basic /)
    expect(onLogin.mock.calls[0][1]).toBe('recepcao')
  })

  it('mostra erro quando o servidor rejeita o login', async () => {
    const onLogin = vi.fn()
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({
      ok: false,
      status: 401,
      json: async () => ({ mensagem: 'credenciais inválidas' }),
    }))
    render(<LoginCard onLogin={onLogin} />)

    fireEvent.change(screen.getByLabelText('Usuário'), { target: { value: 'recepcao' } })
    fireEvent.change(screen.getByLabelText('Senha'), { target: { value: 'errada' } })
    fireEvent.click(screen.getByRole('button', { name: 'Entrar' }))

    expect(await screen.findByRole('alert')).toHaveTextContent('Usuário ou senha inválidos.')
    expect(onLogin).not.toHaveBeenCalled()
  })
})
