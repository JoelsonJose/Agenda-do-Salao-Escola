# Requisitos Funcionais (RF) - Agenda do Salão-Escola

Os requisitos funcionais descrevem o comportamento observável e as operações de negócio disponibilizadas pelo sistema para a **Recepcionista**.

---

## 📋 Lista de Requisitos Funcionais

| ID | Nome | Descrição | Ator | Prioridade |
|---|---|---|---|---|
| **RF-01** | Carga Inicial de Dados | Carregar profissionais e serviços fictícios através de script SQL na inicialização do banco. | Sistema | Alta |
| **RF-02** | Criar Agendamento | Permitir à recepcionista agendar um serviço informando: nome do cliente, serviço, profissional e data/hora de início. | Recepcionista | Alta |
| **RF-03** | Calcular Horário Final | O sistema deve calcular automaticamente o horário de término somando a duração do serviço selecionado à hora de início. | Sistema | Alta |
| **RF-04** | Validação de Conflitos | Impedir agendamentos ativos sobrepostos para o mesmo profissional no mesmo intervalo, recusando com **HTTP 409 Conflict**. | Sistema | Alta |
| **RF-05** | Validação de Expediente | Validar se o agendamento completo cabe no expediente comercial (Segunda a Sábado, das 08:00 às 20:00). Recusar domingos ou extrapolações com **HTTP 400 Bad Request**. | Sistema | Alta |
| **RF-06** | Cancelar Agendamento | Permitir à recepcionista cancelar um agendamento, alterando seu status para `CANCELADO` e liberando imediatamente o horário na agenda. | Recepcionista | Alta |
| **RF-07** | Consultar Agenda Diária | Permitir consultar todos os agendamentos de uma data específica agrupados ou filtrados por profissional. | Recepcionista | Alta |

---

## 🔍 Detalhamento das Regras de Negócio

### RN-01: Duração dos Serviços
- Todo serviço deve possuir duração mínima de **15 minutos** e máxima de **240 minutos** (4 horas).

### RN-02: Cálculo do Término
$$\text{dataHoraFim} = \text{dataHoraInicio} + \text{duracaoMinutos(servico)}$$

### RN-03: Detecção de Conflitos de Horário (Critério de Sobreposição)
Dois agendamentos para o **mesmo profissional** entram em conflito se ambos estiverem com status `ATIVO` e houver interseção temporal estrita entre seus intervalos:
$$\text{NovoInicio} < \text{ExistenteFim} \quad \text{E} \quad \text{NovoFim} > \text{ExistenteInicio}$$

> [!NOTE]
> Encostar no horário final não é conflito (ex: existente até 14:30 e novo iniciando às 14:30 é permitido).

### RN-04: Horário de Atendimento e Dias Úteis
- **Dias permitidos**: Segunda-feira a Sábado (Domingos são rejeitados).
- **Horário de abertura**: 08:00:00.
- **Horário de encerramento**: 20:00:00.
- O atendimento inteiro deve estar contido entre 08:00 e 20:00. Exemplo: um serviço de 45 minutos iniciando às 19:45 terminaria às 20:30, extrapolando as 20:00; deve ser recusado com erro 400.

### RN-05: Mudança de Status e Liberação de Horário
- Um agendamento nunca é apagado fisicamente do banco de dados (exclusão lógica / histórico mantido).
- Ao ser cancelado, o status passa de `ATIVO` para `CANCELADO`.
- Agendamentos com status `CANCELADO` são desconsiderados na verificação de conflitos da RN-03.
