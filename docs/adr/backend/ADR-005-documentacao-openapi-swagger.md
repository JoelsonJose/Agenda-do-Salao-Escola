# ADR-005: Documentação de Contratos de API com OpenAPI 3 e Swagger UI

- **Status**: Aceito
- **Data**: 04/11/2026
- **Decisores**: Equipe de Desenvolvimento
- **Área**: Backend

---

## 1. Contexto

A API REST da **Agenda do Salão-Escola** é consumida pelo frontend SPA e por ferramentas de teste (Postman/Curl). Durante as Sprints do desafio, múltiplos papéis colaboram simultaneamente:
- **DEV (Joelson e Tayana)**: Desenvolvem e ajustam rotas, validações de horário e regra de conflito (HTTP 409).
- **QA e Documentação (Nicolas e Morgana)**: Precisam executar chamadas, validar critérios de aceitação e registrar evidências para a entrega e o pitch.
- **Nuvem (Lucas)**: Precisa validar a integridade dos endpoints após o deploy na Azure App Service.

Manter coleções manuais de Postman ou arquivos Markdown desatualizados gera desalinhamento de contratos (mudança de nomes de campos, tipos de dados e códigos de status HTTP). Era necessário adotar uma especificação de documentação padronizada, automatizada e interativa.

---

## 2. Decisão

Adotar o **OpenAPI 3.0** através da biblioteca **`springdoc-openapi-starter-webmvc-ui`** para geração automática de especificações e disponibilização da interface interativa **Swagger UI**.

### 2.1. Rotas Expostas
- Interface gráfica: `/swagger-ui.html` e `/swagger-ui/**`
- Especificação JSON do OpenAPI 3: `/v3/api-docs` e `/v3/api-docs/**`

### 2.2. Integração com o Spring Security
As rotas de documentação da API serão explicitamente permitidas no `SecurityFilterChain` (junto ao `/health`), garantindo que tanto a equipe quanto os professores consigam inspecionar e testar a aplicação na nuvem:

```java
// Configuração no SecurityFilterChain
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/health").permitAll()
        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
        .anyRequest().authenticated() // ou regras específicas da API
    );
```

### 2.3. Documentação Rica com Anotações
Os controladores utilizarão anotações declarativas para documentar regras de negócio e códigos de erro essenciais do desafio:
- `@Operation`: Descrição do objetivo do endpoint.
- `@ApiResponse(responseCode = "409", description = "Conflito: profissional já possui agendamento no horário")`
- `@ApiResponse(responseCode = "400", description = "Requisição inválida: serviço fora de 15-240 min ou fora do expediente 08:00-20:00")`
- `@ApiResponse(responseCode = "200" / "201", description = "Sucesso")`

---

## 3. Justificativa

1. **Documentação Viva e Sincronizada (Code-First)**: A documentação é gerada automaticamente a partir do código Java e dos DTOs, eliminando o risco de contratos defasados.
2. **Facilidade para QA e Avaliação**: Nicolas, Morgana e os avaliadores do desafio podem testar os fluxos de sucesso e de conflito (409) diretamente pelo navegador sem necessidade de instalar softwares adicionais.
3. **Agilidade no Desenvolvimento Frontend**: Serve como referência imediata para a construção dos modelos e serviços no cliente React.
4. **Alinhamento com Security by Design**: A liberação de acesso às rotas do Swagger é configurada de forma explícita e controlada no Spring Security.

---

## 4. Consequências

### Positivas
- Redução de ruído de comunicação entre as duplas de backend, frontend e QA.
- Facilidade na produção de evidências de testes com capturas do Swagger UI.
- Exportação simplificada da especificação OpenAPI JSON/YAML.

### Negativas / Mitigações
- **Pequeno acréscimo de tempo de inicialização da aplicação**: O Springdoc analisa anotações e classes durante a inicialização.
- **Mitigação**: O overhead é desprezível no ecossistema Spring Boot moderno e amplamente compensado pelo ganho de produtividade da equipe.
