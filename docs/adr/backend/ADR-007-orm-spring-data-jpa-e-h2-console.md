# ADR-007: Adoção do Spring Data JPA (Hibernate) como ORM e H2 Console para Inspeção Visual do Banco

- **Status**: Aceito
- **Data**: 04/11/2026
- **Decisores**: Equipe de Desenvolvimento
- **Área**: Backend / Banco de Dados

---

## 1. Contexto

Com as tabelas do banco relacional definidas em `banco/schema.sql` e a carga inicial em `banco/dados-iniciais.sql`, a equipe precisava definir:
1. Como a camada de serviço Java manipulará os dados de forma produtiva, tipada e segura.
2. Como os desenvolvedores, membros de QA (Nicolas e Morgana) e os professores avaliadores poderão inspecionar visualmente a estrutura das tabelas, chaves e dados persistidos sem depender exclusivamente de linhas de comando.

---

## 2. Decisão

### 2.1. Adoção do Spring Data JPA (Hibernate) como ORM
Adotar o **Spring Data JPA** com **Hibernate** como provedor de persistência e mapeamento objeto-relacional (ORM):
- As classes Java `@Entity` (`Servico`, `Profissional`, `Agendamento`) espelham estritamente o schema definido em `banco/schema.sql`.
- **Prevenção Nativa contra SQL Injection**: O ORM e as interfaces `JpaRepository` utilizam consultas parametrizadas (`PreparedStatement`) nativamente, atendendo a um dos requisitos centrais do desafio.
- **Validação de Schema**: A propriedade `spring.jpa.hibernate.ddl-auto=validate` garante que o Hibernate valide a correspondência das entidades com o `banco/schema.sql`, mantendo o script SQL como a fonte oficial da verdade.

### 2.2. H2 Console para Inspeção Gráfica Visual
Habilitar a interface web interativa **H2 Console** no Spring Boot:
- **Caminho de Acesso**: `/h2-console` (ex: `http://localhost:8080/h2-console`).
- **Recursos Visuais**:
  - Árvore lateral exibindo visualmente as tabelas `SERVICOS`, `PROFISSIONAIS` e `AGENDAMENTOS`.
  - Exibição de tipos de dados, chaves primárias (PK) e restrições de chaves estrangeiras (FK).
  - Execução interativa de consultas SQL com retorno formatado em grade de dados.
- **Integração com Spring Security**: A rota `/h2-console/**` é permitida e os frames são liberados (`headers.frameOptions.sameOrigin()`) para permitir a renderização da interface web.

### 2.3. Suporte a Clientes SQL Externos (DBeaver, VS Code)
Configurar a persistência do H2 em arquivo (`jdbc:h2:file:./dados/agenda;DB_CLOSE_DELAY=-1`), permitindo:
- Sobrevivência dos dados a reinicializações da aplicação.
- Conexão e visualização de diagramas relacionais diretamente por ferramentas externas como DBeaver, IntelliJ Database Tools ou extensões SQL do VS Code.

---

## 3. Justificativa

1. **Produtividade e Tipagem Forte**: O ORM reduz código boilerplate JDBC e mapeia relacionamentos `@ManyToOne` de forma idiomática em Java.
2. **Evidências Visuais para Avaliação**: Permite capturar telas do H2 Console demonstrando a criação dos agendamentos, o cálculo do horário final e a alteração de status para `CANCELADO` para o pitch e relatórios de evidências.
3. **Segurança por Padrão**: Elimina o risco de concatenação de SQL insegura.

---

## 4. Consequências

### Positivas
- Código limpo e desacoplado na camada de repositório (`AgendamentoRepository`).
- Facilidade de diagnóstico e auditoria visual dos dados em tempo real.
- Compatibilidade transparente caso a aplicação seja apontada para Azure SQL ou PostgreSQL em homologação.

### Negativas / Mitigações
- **Overhead do ORM**: Hibernate adiciona camadas de abstração e cache de primeiro nível.
- **Mitigação**: O volume da Agenda do Salão-Escola é perfeitamente adequado para JPA, e as consultas complexas utilizam JPQL/queries parametrizadas objetivas.

