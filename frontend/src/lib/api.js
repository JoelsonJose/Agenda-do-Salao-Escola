const API_ROOT = import.meta.env.VITE_API_BASE_URL || '/api'

export function criarCredencial(usuario, senha) {
  return 'Basic ' + btoa(usuario + ':' + senha)
}

export async function requisicao(caminho, { credential, ...options } = {}) {
  const headers = new Headers(options.headers)
  if (credential) headers.set('Authorization', credential)
  if (options.body) headers.set('Content-Type', 'application/json')

  let response
  try {
    response = await fetch(API_ROOT + caminho, { ...options, headers })
  } catch {
    throw new Error('Não foi possível conectar à aplicação. Confira se a API está iniciada.')
  }

  if (!response.ok) {
    const payload = await response.json().catch(() => null)
    if (response.status === 401) throw new Error('Usuário ou senha inválidos.')
    throw new Error(payload?.mensagem || 'A solicitação falhou (' + response.status + ').')
  }

  if (response.status === 204) return null
  return response.json()
}
