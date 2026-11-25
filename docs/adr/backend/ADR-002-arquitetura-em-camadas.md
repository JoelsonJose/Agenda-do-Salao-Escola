# ADR-002: Arquitetura em Camadas (Layered Architecture) no Backend

- **Status**: Aceito
- **Data**: 04/11/2026
- **Decisores**: Equipe de Desenvolvimento
- **Área**: Backend

---

## 1. Contexto

A API da **Agenda do Salão-Escola** precisa processar fluxos de agendamento que envolvem validações de entrada, regras de negócio críticas (sobreposição de horários para o mesmo profissional gerando HTTP 409, intervalo permitido de 08:00 às 20:00 de segunda a sábado, cálculo do término baseado na duração do serviço) e persistência em banco relacional.

Sem uma arquitetura estruturada, é comum ver regras de banco vazando para os controladores ou regras de negócio misturadas com queries SQL, dificultando testes unitários, manutenção e colaboração entre múltiplos desenvolvedores (Joelson, Tayana, Homero, César).

---

## 2. Decisão

Adotar a clássica e consagrada **Arquitetura em Camadas (Layered Architecture)**, estabelecendo limites bem definidos e comunicação unidirecional entre as camadas:

```mermaid
flowchart TD
    Client["Cliente HTTP (Frontend / Postman)"] -->|JSON / HTTPS| Controller["Camada de Apresentação / API<br/>(Controllers & Global Exception Handler)"]
    Controller -->|DTOs Validados (@Valid)| Service["Camada de Negócio<br/>(Services & Regras de Domínio)"]
    Service -->|Entidades de Domínio| Repository["Camada de Acesso a Dados<br/>(Repositories / Spring Data)"]
    Repository -->|SQL Parametrizado| DB[(Banco de Dados Relacional)]
```

### Detalhamento das Camadas:

1. **Camada de Apresentação (`controller`)**:
   - Expõe os endpoints REST (`@RestController`).
   - Recebe as requisições HTTP, valida o payload de entrada via Bean Validation (`@Valid`, `@NotNull`, `@FutureOrPresent`).
   - Mapeia códigos de status HTTP apropriados (`201 Created`, `200 OK`, `204 No Content`).
   - Nunca contém regras de negócio ou lógica SQL.
   - Centraliza o tratamento de erros via `@RestControllerAdvice` (ex: intercepta `ConflitoHorarioException` e retorna HTTP 409).

2. **Camada de Transferência de Dados (`dto`)**:
   - `AgendamentoRequestDTO`: payload enviado pelo cliente (idServico, idProfissional, cliente, dataHoraInicio).
   - `AgendamentoResponseDTO`: payload higienizado retornado ao cliente.
   - Evita a exposição direta da entidade do banco na API pública.

3. **Camada de Serviço / Negócio (`service`)**:
   - Onde residem todas as regras de negócio da aplicação.
   - **Regra de Conflito**: Consulta o repositório para verificar se já existe agendamento ativo que se sobreponha para o mesmo profissional. Em caso afirmativo, lança exceção que gera HTTP 409.
   - **Regra de Horário**: Valida se o início e o término calculado cabem inteiramente entre 08:00 e 20:00, e se o dia da semana é de segunda a sábado (rejeitando domingos com HTTP 400).
   - **Cálculo de Término**: Busca a duração do serviço selecionado e soma aos minutos do início.
   - **Cancelamento**: Altera o status do agendamento para `CANCELADO`, liberando o horário para novas marcações.

4. **Camada de Domínio (`model` / `entity`)**:
   - Entidades JPA representando as tabelas do banco: `Servico`, `Profissional`, `Agendamento`.
   - Enum para status: `StatusAgendamento` (`ATIVO`, `CANCELADO`).

5. **Camada de Persistência (`repository`)**:
   - Interfaces estendendo `JpaRepository` ou `CrudRepository`.
   - Métodos de busca parametrizados e seguros contra SQL Injection (ex: `findSobreposicoesAtivas(...)`).

---

## 3. Justificativa

1. **Separação de Preocupações (SoC)**: Cada componente possui uma única responsabilidade clara.
2. **Alta Testabilidade**: É possível testar os Services com mocks de repositório (testes unitários rápidos) e os Controllers com `MockMvc` (testes de integração de API).
3. **Divisão de Trabalho**: Facilita a distribuição das tarefas do time:
   - Tayana: regras de conflito e cálculo no `Service`.
   - Joelson: controladores, rotas e DTOs.
   - Homero e César: mapeamento das entidades e queries no `Repository`.

---

## 4. Consequências

### Positivas
- Código organizado, padronizado e com baixo atrito de manutenção.
- Facilidade em manter a regra estrita de conflito HTTP 409 e validações do desafio.
- Facilidade de auditoria e evolução para novas regras de negócio.

### Negativas / Mitigações
- **Mapeamento DTO <-> Entidade**: Criação de classes extras de DTO.
- **Mitigação**: Utilizar métodos utilitários estáticos ou mappers simples para converter entre Entidade e DTO com clareza e sem overhead.

