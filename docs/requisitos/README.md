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
> **Nível escolhido pelo grupo**: **A**. A aplicação foi publicada no App Service e conectada ao Azure SQL gerenciado. Os testes de aceite e a persistência após reinício foram verificados em 25/11/2026; capturas e consulta SQL direta ainda faltam como evidência anexada.

### Opções do Desafio:

| Nível | Descrição do Escopo de Nuvem | Status Atual |
|---|---|---|
| **Nível A** | Aplicação no Azure App Service e banco relacional criado/gerenciado pelo grupo. | Escolhido; publicação, conexão e cenários de aceite verificados |
| **Nível B** | Aplicação no App Service com o banco compartilhado disponibilizado pelo professor, conforme o enunciado. | Não escolhido |
| **Nível C** | Execução local da aplicação, conforme o enunciado. | Não escolhido |

*Detalhes da implantação e itens de evidência restantes em `../deploy-azure.md`.*

---

## 📂 4. Estrutura da Documentação de Requisitos

- **[Requisitos Funcionais (RF)](requisitos-funcionais.md)**: Detalhamento de cada funcionalidade com regras de negócio e fluxos.
- **[Requisitos Não Funcionais (RNF)](requisitos-nao-funcionais.md)**: Critérios técnicos de segurança, cloud, persistência e performance.
- **[Critérios de Aceitação e Modelos (CA1-CA5)](modelos-e-criterios.md)**: Mapeamento dos cenários de teste, diagramas de estados, ERD e fluxos de agendamento.
