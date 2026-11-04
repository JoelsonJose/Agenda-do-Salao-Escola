# ADR-004: Estruturação de Diretórios por Package by Feature no Frontend

- **Status**: Aceito
- **Data**: 04/11/2026
- **Decisores**: Equipe de Desenvolvimento
- **Área**: Frontend

---

## 1. Contexto

A organização tradicional em projetos frontend costuma adotar a divisão "Package by Layer" (ou "Package by Type"), onde todos os componentes ficam em `/components`, todos os hooks em `/hooks`, todos os tipos em `/types` e todas as chamadas de API em `/services`.

Conforme a aplicação cresce, essa abordagem causa:
- **Dispersão de Contexto**: Para alterar a funcionalidade de "Agendamentos", o desenvolvedor precisa navegar simultaneamente em 4 ou 5 pastas raízes diferentes.
- **Acoplamento Involuntário**: Facilita a mistura indevida de regras de diferentes domínios da aplicação.
- **Dificuldade de Remoção ou Refatoração**: Torna difícil saber quais arquivos são exclusivos de uma funcionalidade específica.

---

## 2. Decisão

Adotar a abordagem **Package by Feature** (modularização por domínio funcional) combinada com uma camada transversal (`shared`) para elementos genuinamente reutilizáveis em toda a aplicação.

### Estrutura de Diretórios Proposta:

```text
src/
├── app/                        # Configuração central da aplicação (Router, Providers, Theme)
│   ├── App.jsx
│   └── routes.jsx
├── shared/                     # Componentes e utilitários transversais (Cross-cutting)
│   ├── components/             # Atomic Design compartilhado
│   │   ├── atoms/              # Button, Input, Badge, Spinner
│   │   ├── molecules/          # FormField, Modal, NotificationToast
│   │   └── templates/          # BaseLayout, Header, Footer
│   ├── hooks/                  # Hooks utilitários globais (useDebounce, useToast)
│   ├── services/               # Configuração do cliente HTTP (apiClient/Axios/Fetch)
│   └── utils/                  # Formatadores de data, helpers de validação
├── features/                   # Módulos encapsulados por funcionalidade de negócio
│   ├── agendamentos/           # Feature de Agendamentos
│   │   ├── components/         # Organismos/Moléculas específicos da feature
│   │   │   ├── AppointmentForm.jsx
│   │   │   └── AppointmentList.jsx
│   │   ├── hooks/              # ViewModel da feature
│   │   │   └── useAgendamentoViewModel.js
│   │   ├── models/             # Tipos, contratos e DTOs de agendamento
│   │   │   └── agendamentoModel.js
│   │   ├── services/           # Chamadas HTTP específicas (/agendamentos)
│   │   │   └── agendamentoService.js
│   │   └── pages/              # Páginas completas da feature
│   │       ├── NovoAgendamentoPage.jsx
│   │       └── ListagemAgendamentosPage.jsx
│   ├── profissionais/          # Feature de Profissionais e Grade Diária
│   │   ├── components/
│   │   │   └── DailyScheduleGrid.jsx
│   │   ├── hooks/
│   │   │   └── useAgendaProfissionalViewModel.js
│   │   ├── services/
│   │   └── pages/
│   │       └── AgendaDiariaPage.jsx
│   └── servicos/               # Feature de Serviços e durações
│       ├── components/
│       ├── services/
│       └── models/
└── assets/                     # Estilos globais, imagens e ícones
```

---

## 3. Justificativa

1. **Alta Coesão e Baixo Acoplamento**: Tudo o que pertence à regra de agendamentos (model, view, viewModel, chamadas de serviço) reside no mesmo módulo de feature.
2. **Navegabilidade e Escalabilidade**: Desenvolvedores conseguem trabalhar em paralelo (ex: Tayana em `agendamentos` e Joelson em `profissionais`) sem conflitos constantes no Git.
3. **Fácil Exclusão/Substituição**: Se uma funcionalidade for depreciada ou substituída, basta excluir ou arquivar o diretório da respectiva feature.

---

## 4. Consequências

### Positivas
- Rápida localização de arquivos e regras de negócio.
- Clareza sobre o que é compartilhado (`shared/`) e o que é específico do domínio (`features/`).
- Integração natural com o padrão MVVM e o Atomic Design.

### Negativas / Mitigações
- **Dúvidas sobre onde alocar um componente novo**: O desenvolvedor pode ficar em dúvida se um componente é `shared` ou de uma `feature`.
- **Regra de Ouro**: O componente inicia dentro da `feature`. Se duas ou mais features distintas precisarem dele, ele é promovido para `shared/`.

