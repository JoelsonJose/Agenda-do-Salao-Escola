# ADR-001: Adoção do Spring Boot como Plataforma Backend

- **Status**: Aceito
- **Data**: 04/11/2026
- **Decisores**: Equipe de Desenvolvimento
- **Área**: Backend

---

## 1. Contexto

O projeto **Agenda do Salão-Escola** requer uma API RESTful escalável, estável e segura para gerenciamento de agendamentos, serviços e profissionais, com suporte a persistência relacional, validação rigorosa de regras de conflito de horário (HTTP 409), endpoint de verificação de integridade (`GET /health`) e facilidade de implantação na nuvem (Azure App Service).

A equipe precisava escolher uma plataforma de backend empresarial com forte suporte a injeção de dependência, segurança integrada, facilidade de conexão a bancos relacionais via string de conexão (`DATABASE_URL`) e vasta documentação.

---

## 2. Decisão

Adotar o **Spring Boot** (ecossistema Java / Spring Framework) como base para a construção da API RESTful do backend.

Componentes fundamentais da stack backend:
- **Java 17 / 21 (LTS)**: Linguagem fortemente tipada, moderna e performática.
- **Spring Boot Web**: Criação de endpoints REST com servidor Tomcat embutido.
- **Spring Data JPA / Hibernate (ou Spring JDBC)**: Mapeamento objeto-relacional, suporte a consultas parametrizadas contra SQL Injection e integração com banco de dados relacional (PostgreSQL / MySQL / Azure SQL).
- **Spring Boot Actuator / Web Health**: Disponibilização do endpoint de monitoramento `GET /health` exigido no desafio.
- **Spring Security**: Camada robusta de autenticação, autorização e filtros de segurança.
- **Maven**: Gerenciador de dependências e ciclo de vida de build.

---

## 3. Justificativa

1. **Padrão de Mercado Consolidado**: O ecossistema Spring Boot é o padrão de referência para desenvolvimento de serviços corporativos em Java, com maturidade comprovada.
2. **Prontidão para Nuvem (Cloud-Native)**: Gera um executável autocontido (`.jar`) que pode ser executado facilmente no Azure App Service (seja via Java SE runtime nativo ou contêiner Docker).
3. **Gerenciamento de Configuração**: Suporte nativo a leitura de variáveis de ambiente do sistema operacional (como `DATABASE_URL` e `PORT`), essencial para ambientes em nuvem onde credenciais nunca devem ser versionadas.
4. **Tratamento de Exceções Centralizado**: Facilidade de implementar `@ControllerAdvice` e `@ExceptionHandler` para padronizar respostas de erro (ex: 409 Conflict, 400 Bad Request) com mensagens claras e estruturadas.

---

## 4. Consequências

### Positivas
- Ecossistema rico com bibliotecas maduras e suporte a métricas.
- Injeção de dependências nativa (Inversion of Control - IoC).
- Facilidade de escrita de testes automatizados com Spring Boot Test, JUnit 5 e Mockito.
- Compatibilidade direta com o nível de implantação da Azure exigido pelo desafio.

### Negativas / Mitigações
- **Consumo de Memória Inicial**: Aplicações Java/JVM podem demandar mais memória na inicialização em comparação com runtimes mais leves (como Go ou Node.js).
- **Mitigação**: Configurar flags adequadas da JVM (`-Xmx` e `-Xms`) dimensionadas para a camada de App Service utilizada no desafio.

