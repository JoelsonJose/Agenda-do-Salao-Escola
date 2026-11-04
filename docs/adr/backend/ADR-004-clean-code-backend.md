# ADR-004: Diretrizes e Padrões de Clean Code no Backend

- **Status**: Aceito
- **Data**: 04/11/2026
- **Decisores**: Equipe de Desenvolvimento
- **Área**: Backend

---

## 1. Contexto

A manutenção do código backend do projeto **Agenda do Salão-Escola** requer legibilidade, coesão e baixo acoplamento para que a equipe (desenvolvedores, QA e revisores) consiga evoluir as funcionalidades com segurança entre as Sprints 0, 1 e 2. Práticas inadequadas em Java/Spring (como injeção de dependência via atributos privados com `@Autowired`, métodos de negócio gigantescos com múltiplos `if/else`, ausência de tipos específicos e exceções genéricas) geram fragilidade e tornam os testes unitários custosos.

---

## 2. Decisão

Estabelecer padrões de **Clean Code** e boas práticas de engenharia de software para todo o código Java / Spring Boot do projeto:

### 2.1. Injeção de Dependências via Construtor
- É **proibida** a injeção por campo direto (`@Autowired` em campo privado).
- Todas as dependências devem ser declaradas como `final` e injetadas através do construtor da classe (usando construtor explícito ou anotação `@RequiredArgsConstructor` do Lombok).
- **Vantagem**: Garante imutabilidade dos componentes e simplifica a instanciação em testes unitários sem necessidade de carregar o contexto inteiro do Spring (`@SpringBootTest`).

```java
// ✅ Recomendado: Injeção por construtor e imutabilidade
@Service
@RequiredArgsConstructor
public class AgendamentoService {
    private final AgendamentoRepository agendamentoRepository;
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;
    // métodos de negócio...
}
```

### 2.2. Princípios SOLID Aplicados
- **Single Responsibility Principle (SRP)**:
  - Controllers cuidam do protocolo HTTP e do mapeamento de rotas.
  - Services coordenam regras de negócio e transações (`@Transactional`).
  - Repositories lidam com queries e persistência.
- **Open/Closed Principle (OCP)**: Regras de validação (ex: conflito de horário, limite de atendimento) devem ser organizadas em validadores modulares quando a complexidade crescer.
- **Dependency Inversion Principle (DIP)**: O Service depende de abstrações (interfaces de Repositories), facilitando a criação de mocks para testes unitários com Mockito.

### 2.3. Exceções Expressivas de Domínio
- Não lançar exceções genéricas (`throw new RuntimeException("erro")`).
- Criar exceções que representam conceitos reais de negócio e mapeá-las para respostas HTTP com semântica correta no `@RestControllerAdvice`:
  - `ConflitoHorarioException` → HTTP 409 Conflict (conforme requisito de aceite do edital).
  - `HorarioForaDeExpedienteException` → HTTP 400 Bad Request.
  - `RecursoNaoEncontradoException` → HTTP 404 Not Found.

### 2.4. Nomenclatura Semântica e Código Autoexplicativo
- Métodos devem ser verbos no infinitivo com nomes descritivos:
  - `verificarSobreposicaoDeHorario(idProfissional, inicio, fim)`
  - `calcularHorarioFinal(inicio, duracaoMinutos)`
  - `cancelarAgendamento(idAgendamento)`
- Evitar comentários que apenas repetem o que o código faz. Usar comentários apenas para explicar o "porquê" de decisões complexas de negócio.

### 2.5. Pirâmide de Testes Automatizados
- **Testes Unitários (JUnit 5 + Mockito)**: Cobrir 100% dos cenários de conflito de horário do `AgendamentoService` (Ana das 14:00 às 14:30 vs novo às 14:15 [409], novo às 14:30 [aceito], Bruno às 14:15 [aceito], cancelamento liberando horário [aceito]).
- **Testes de Integração (`@WebMvcTest` ou `MockMvc`)**: Validar se as rotas retornam os status codes e formatos JSON esperados.

---

## 3. Justificativa

1. **Facilidade de Teste**: Injeção por construtor permite testes unitários puros que rodam em milissegundos sem depender do ciclo de vida pesado do Spring.
2. **Conformidade com os Critérios de Aceitação**: A regra de recusa com 409 para sobreposição é central e fica isolada, clara e testada.
3. **Clareza para Toda a Equipe**: Facilita a revisão por Nicolas e Morgana (QA/Documentação) e colaboração direta entre Joelson, Tayana, César e Homero.

---

## 4. Consequências

### Positivas
- Código legível, altamente modular e de fácil diagnóstico em caso de incidentes.
- Eliminação de bugs silenciosos e acoplamentos espaguete.
- Documentação viva através de testes unitários claros.

### Negativas / Mitigações
- Exige disciplina da equipe nas revisões de pull request para evitar atalhos rápidos (como usar `@Autowired` direto no campo).

