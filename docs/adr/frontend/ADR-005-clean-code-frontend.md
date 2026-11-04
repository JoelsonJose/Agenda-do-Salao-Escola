# ADR-005: Diretrizes e Padrões de Clean Code no Frontend

- **Status**: Aceito
- **Data**: 04/11/2026
- **Decisores**: Equipe de Desenvolvimento
- **Área**: Frontend

---

## 1. Contexto

Para assegurar a longevidade, legibilidade e facilidade de manutenção do código frontend ao longo das Sprints do projeto, é fundamental estabelecer convenções e princípios de **Clean Code** que sejam seguidos por todos os desenvolvedores. Sem diretrizes claras, o código tende a acumular débitos técnicos, componentes com excesso de responsabilidades, prop drilling excessivo e nomes ambíguos.

---

## 2. Decisão

Adotar um conjunto obrigatório de práticas e diretrizes de **Clean Code** orientadas ao ecossistema React/JavaScript moderno:

### 2.1. Princípio da Responsabilidade Única (SRP)
- **Componentes de Apresentação (View)**: Devem apenas renderizar elementos visuais a partir de propriedades (`props`). Não devem conter regras de negócio complexas ou chamadas diretas de I/O.
- **Custom Hooks (ViewModel)**: Cada hook deve ser responsável por um fluxo ou estado específico (ex: `useCreateAppointment`, `useDailySchedule`).
- **Funções Utilitárias**: Funções de formatação e cálculo (ex: cálculo de horário final a partir da duração) devem ser **funções puras** (sem efeitos colaterais).

### 2.2. Convenções de Nomenclatura
- **Componentes**: `PascalCase` representando substantivos claros (`AppointmentCard`, `TimeSlotPicker`).
- **Hooks**: Prefixo `use` seguido de camelCase (`useAgendamentoViewModel`).
- **Manipuladores de Eventos**:
  - Na View/Componente: prefixo `handle` (ex: `handleSubmit`, `handleCancelClick`).
  - Nas Props repassadas: prefixo `on` (ex: `onConfirm`, `onStatusChange`).
- **Variáveis Booleanas**: Devem indicar predicado claro com prefixos (`isLoading`, `hasConflict`, `isCancelled`, `isOpen`).

### 2.3. Gestão Explícita de Estados de Interface
Evitar flags booleanas conflitantes (ex: `isLoading && isError`). O estado da interface deve seguir uma máquina de estados finita ou enumerada:
- `IDLE`: Inicial / aguardando ação.
- `LOADING`: Requisição em andamento (exibe spinner/skeleton).
- `SUCCESS`: Dados recebidos e renderizados.
- `ERROR`: Falha capturada com mensagem amigável e código de erro correspondente (ex: 409 Conflito de Horário).

### 2.4. Composição sobre Herança e Evitar Prop Drilling
- Priorizar a **composição de componentes** (passando `children` ou slots) para evitar passar props desnecessárias por múltiplos níveis de árvore.
- Se necessário compartilhar dados globais leves (ex: tema, notificações toast), utilizar React Context de forma pontual.

### 2.5. Automação e Qualidade
- Utilizar **ESLint** com regras recomendadas para React e Hooks (`eslint-plugin-react-hooks`).
- Utilizar **Prettier** para formatação consistente de código.
- Eliminar código morto, logs de debug (`console.log`) e código comentado antes de abrir pull requests.

---

## 3. Justificativa

1. **Leitura Fluida**: O código se torna autoexplicativo, reduzindo a dependência de comentários redundantes.
2. **Onboarding Rápido**: Novos membros ou revisores (QA) conseguem inspecionar e compreender o comportamento da interface rapidamente.
3. **Redução de Regressões**: Funções puras e componentes desacoplados facilitam a escrita de testes unitários confiáveis.

---

## 4. Consequências

### Positivas
- Padrão arquitetural uniforme em todo o repositório.
- Redução de bugs causados por efeitos colaterais e estados inconsistentes.
- Facilidade nas revisões de código (Code Reviews).

### Negativas / Mitigações
- **Curva de adaptação**: Desenvolvedores menos experientes podem precisar de orientação nos primeiros Pull Requests.
- **Mitigação**: Checklist de revisão de código baseado neste ADR.

