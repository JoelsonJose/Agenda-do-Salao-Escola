import { useCallback, useEffect, useState } from 'react'
import { requisicao } from '../../lib/api.js'

export function hojeLocal() {
  const data = new Date()
  const ano = data.getFullYear()
  const mes = String(data.getMonth() + 1).padStart(2, '0')
  const dia = String(data.getDate()).padStart(2, '0')
  return ano + '-' + mes + '-' + dia
}

export function useAgenda(credential, data) {
  const [servicos, setServicos] = useState([])
  const [profissionais, setProfissionais] = useState([])
  const [agendamentos, setAgendamentos] = useState([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')

  const atualizar = useCallback(async () => {
    setCarregando(true)
    setErro('')
    try {
      const [listaServicos, listaProfissionais, listaAgenda] = await Promise.all([
        requisicao('/servicos', { credential }),
        requisicao('/profissionais', { credential }),
        requisicao('/agendamentos?data=' + encodeURIComponent(data), { credential }),
      ])
      setServicos(listaServicos)
      setProfissionais(listaProfissionais)
      setAgendamentos(listaAgenda)
    } catch (error) {
      setErro(error.message)
    } finally {
      setCarregando(false)
    }
  }, [credential, data])

  useEffect(() => {
    atualizar()
  }, [atualizar])

  const cancelar = async (id) => {
    await requisicao('/agendamentos/' + id + '/cancelamento', { method: 'PATCH', credential })
    await atualizar()
  }

  const criar = async (payload) => {
    await requisicao('/agendamentos', {
      method: 'POST',
      credential,
      body: JSON.stringify(payload),
    })
    await atualizar()
  }

  return { servicos, profissionais, agendamentos, carregando, erro, atualizar, cancelar, criar }
}
