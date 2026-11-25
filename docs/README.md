# Documentação do Projeto - Agenda do Salão-Escola

Bem-vindo à documentação técnica e arquitetural do projeto **Agenda do Salão-Escola** (Cloud Challenge).

---

## 📚 Conteúdo da Documentação

- **[Requisitos e Escopo (Desafio 03)](requisitos/README.md)**:
  - Usuária e Persona: **Recepcionista do Salão-Escola**
  - [Requisitos Funcionais (RF-01 a RF-07)](requisitos/requisitos-funcionais.md)
  - [Requisitos Não Funcionais (RNF-01 a RNF-08)](requisitos/requisitos-nao-funcionais.md)
  - [Critérios de Aceitação (CA1 a CA5) e Modelos Visuais (ERD, Casos de Uso, Sequência)](requisitos/modelos-e-criterios.md)
  - Status do Nível de Implantação Azure (A, B ou C): *Pendente de confirmação do professor*

- **[Decisões de Arquitetura (ADRs)](adr/README.md)**: Registros formais de decisões arquiteturais para o Frontend e Backend.
  - **[Frontend ADRs](adr/frontend/)**:
    - [ADR-001: React + React DOM + Vite](adr/frontend/ADR-001-stack-react-vite.md)
    - [ADR-002: Padrão Arquitetural MVVM](adr/frontend/ADR-002-arquitetura-mvvm.md)
    - [ADR-003: Design Atômico (Atomic Design)](adr/frontend/ADR-003-design-atomico.md)
    - [ADR-004: Modularização Package by Feature](adr/frontend/ADR-004-organizacao-package-by-feature.md)
    - [ADR-005: Diretrizes de Clean Code no Frontend](adr/frontend/ADR-005-clean-code-frontend.md)
    - [ADR-006: Security by Design no Frontend](adr/frontend/ADR-006-security-by-design-frontend.md)
  - **[Backend ADRs](adr/backend/)**:
    - [ADR-001: Plataforma Spring Boot](adr/backend/ADR-001-stack-spring-boot.md)
    - [ADR-002: Arquitetura em Camadas (Layered Architecture)](adr/backend/ADR-002-arquitetura-em-camadas.md)
    - [ADR-003: Spring Security e Security by Design](adr/backend/ADR-003-spring-security-security-by-design.md)
    - [ADR-004: Diretrizes de Clean Code no Backend](adr/backend/ADR-004-clean-code-backend.md)
    - [ADR-005: Documentação de Contratos com OpenAPI 3 e Swagger UI](adr/backend/ADR-005-documentacao-openapi-swagger.md)

---

## 🎯 Resumo da Arquitetura Escolhida

```mermaid
flowchart LR
    subgraph Frontend["Frontend SPA"]
        F1["React + Vite"]
        F2["MVVM + Atomic Design"]
        F3["Package by Feature"]
        F4["Security by Design & Clean Code"]
    end

    subgraph Backend["Backend API"]
        B1["Spring Boot"]
        B2["Arquitetura em Camadas"]
        B3["Spring Security & Security by Design"]
        B4["Clean Code & Validation"]
    end

    subgraph Database["Persistência"]
        DB[(Banco Relacional / DATABASE_URL)]
    end

    Frontend -->|HTTPS / REST JSON| Backend
    Backend -->|Queries Parametrizadas| Database
```

