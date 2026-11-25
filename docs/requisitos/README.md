# Requisitos e Escopo do Desafio 03 - Agenda do Salão-Escola

Este documento registra formalmente o escopo, objetivos, persona e requisitos levantados para o projeto **Agenda do Salão-Escola** no âmbito do **Cloud Challenge (Desafio 03)**.

---

## 🎯 1. Objetivo e Contexto

O objetivo principal é eliminar conflitos de agendamento manual no Salão-Escola, onde atendimentos sobrepostos geravam transtornos para clientes e profissionais. A aplicação automatiza a validação de disponibilidade, cálculos de duração e restrições de funcionamento.

---

## 👤 2. Usuária e Persona Principal

- **Perfil**: **Recepcionista do Salão-Escola**
- **Papel**: Responsável por receber pedidos de agendamento (presenciais ou telefônicos), consultar a disponibilidade na grade diária de cada profissional, registrar novos horários e efetuar cancelamentos quando solicitados pelos clientes.
- **Necessidades principais**:
  - Saber instantaneamente se uma profissional está livre ou ocupada em um horário.
  - Não permitir agendamentos com sobreposição de horário por engano.
  - Visualizar a agenda do dia de cada profissional de forma clara e objetiva.
  - Cancelar agendamentos com liberação imediata do horário na grade.

---

## 📌 3. Nível de Implantação na Azure (Status & Pendência)

> [!IMPORTANT]
> **Status da Definição de Nível**: **PENDENTE DE CONFIRMAÇÃO COM O PROFESSOR**
> O grupo aguarda a atribuição oficial do professor referente ao nível de implantação no Azure App Service.

### Opções do Desafio:

| Nível | Descrição do Escopo de Nuvem | Status Atual |
|---|---|---|
| **Nível A** | Deploy básico da API e Frontend no Azure App Service + Banco Relacional persistente + `DATABASE_URL`. | Em análise |
| **Nível B** | Nível A + Automação de CI/CD via GitHub Actions + Configurações de variáveis de ambiente gerenciadas via Azure CLI/Portal. | Em análise |
| **Nível C** | Nível B + Monitoramento com Azure Application Insights / Health Metrics + Resiliência e logs centralizados. | Em análise |

*A decisão final será atualizada neste documento e no `README.md` assim que o professor confirmar a distribuição.*

---

## 📂 4. Estrutura da Documentação de Requisitos

- **[Requisitos Funcionais (RF)](requisitos-funcionais.md)**: Detalhamento de cada funcionalidade com regras de negócio e fluxos.
- **[Requisitos Não Funcionais (RNF)](requisitos-nao-funcionais.md)**: Critérios técnicos de segurança, cloud, persistência e performance.
- **[Critérios de Aceitação e Modelos (CA1-CA5)](modelos-e-criterios.md)**: Mapeamento dos cenários de teste, diagramas de estados, ERD e fluxos de agendamento.
