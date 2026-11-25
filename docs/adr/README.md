# Registro de Decisões de Arquitetura (ADRs) - Agenda do Salão-Escola

Este diretório contém os **Architecture Decision Records (ADRs)** do projeto **Agenda do Salão-Escola**, documentando decisões arquiteturais, justificativas, padrões adotados e trade-offs para as camadas de Frontend e Backend.

---

## 📌 O que é um ADR?
Um **Architecture Decision Record (ADR)** é um documento que captura uma decisão arquitetural relevante tomada pela equipe, acompanhada de seu contexto, consequências e alternativas consideradas.

---

## 📂 Estrutura de ADRs

```text
docs/
└── adr/
    ├── README.md (Este índice)
    ├── frontend/
    │   ├── ADR-001-stack-react-vite.md
    │   ├── ADR-002-arquitetura-mvvm.md
    │   ├── ADR-003-design-atomico.md
    │   ├── ADR-004-organizacao-package-by-feature.md
    │   ├── ADR-005-clean-code-frontend.md
    │   └── ADR-006-security-by-design-frontend.md
    └── backend/
        ├── ADR-001-stack-spring-boot.md
        ├── ADR-002-arquitetura-em-camadas.md
        ├── ADR-003-spring-security-security-by-design.md
        ├── ADR-004-clean-code-backend.md
        └── ADR-005-documentacao-openapi-swagger.md
```

---

## 📋 Índice de Decisões

### 🖥️ Frontend

| Código | Título | Status | Data |
|---|---|---|---|
| [ADR-001](frontend/ADR-001-stack-react-vite.md) | Adoção de React + React DOM + Vite | Aceito | 04/11/2026 |
| [ADR-002](frontend/ADR-002-arquitetura-mvvm.md) | Padrão Arquitetural MVVM (Model-View-ViewModel) | Aceito | 04/11/2026 |
| [ADR-003](frontend/ADR-003-design-atomico.md) | Metodologia Atomic Design para Interface e Componentes | Aceito | 04/11/2026 |
| [ADR-004](frontend/ADR-004-organizacao-package-by-feature.md) | Estruturação de Diretórios por Package by Feature | Aceito | 04/11/2026 |
| [ADR-005](frontend/ADR-005-clean-code-frontend.md) | Diretrizes e Padrões de Clean Code no Frontend | Aceito | 04/11/2026 |
| [ADR-006](frontend/ADR-006-security-by-design-frontend.md) | Princípios de Security by Design no Frontend | Aceito | 04/11/2026 |

### ⚙️ Backend

| Código | Título | Status | Data |
|---|---|---|---|
| [ADR-001](backend/ADR-001-stack-spring-boot.md) | Adoção do Spring Boot como Plataforma Backend | Aceito | 04/11/2026 |
| [ADR-002](backend/ADR-002-arquitetura-em-camadas.md) | Arquitetura em Camadas (Layered Architecture) | Aceito | 04/11/2026 |
| [ADR-003](backend/ADR-003-spring-security-security-by-design.md) | Segurança com Spring Security e Security by Design | Aceito | 04/11/2026 |
| [ADR-004](backend/ADR-004-clean-code-backend.md) | Diretrizes e Padrões de Clean Code no Backend | Aceito | 04/11/2026 |
| [ADR-005](backend/ADR-005-documentacao-openapi-swagger.md) | Documentação de Contratos de API com OpenAPI 3 e Swagger UI | Aceito | 04/11/2026 |

