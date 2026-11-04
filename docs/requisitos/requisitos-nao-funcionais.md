# Requisitos Não Funcionais (RNF) - Agenda do Salão-Escola

Os requisitos não funcionais definem as qualidades sistêmicas, restrições arquiteturais, critérios de segurança e conformidade operacional da aplicação.

---

## 🛡️ Lista de Requisitos Não Funcionais

| ID | Categoria | Descrição do Requisito | Métrica / Critério de Aceite |
|---|---|---|---|
| **RNF-01** | **Persistência** | Todos os dados de serviços, profissionais e agendamentos devem persistir em banco de dados relacional. | Os dados devem permanecer intactos após o reinício da aplicação na nuvem. |
| **RNF-02** | **Disponibilidade** | A API deve expor um endpoint público `GET /health`. | Retornar status HTTP 200 OK com status da conexão com o banco de dados. |
| **RNF-03** | **Segurança (Credenciais)** | A string de conexão do banco deve ser fornecida via variável de ambiente `DATABASE_URL`. | Proibido comitar senhas, tokens ou arquivos `.env` no repositório Git. |
| **RNF-04** | **Segurança (Injeção)** | Todas as operações de leitura e gravação no banco devem usar consultas estritamente parametrizadas. | Zero vulnerabilidades de SQL Injection comprovadas por auditoria de código. |
| **RNF-05** | **Confiabilidade & Validação** | Validação mandatória no backend via Bean Validation (`@Valid`) e sanitização de inputs. | Respostas HTTP estruturadas com códigos semânticos (200, 201, 400, 404, 409, 500) sem vazamento de stack traces. |
| **RNF-06** | **Hospedagem em Nuvem** | A aplicação deve ser implantada no Azure App Service conforme o nível atribuído (A, B ou C). | Aplicação acessível via URL pública HTTPS fornecida pela Azure. |
| **RNF-07** | **Arquitetura & Código** | Código estruturado em camadas no backend (Spring Boot) e MVVM + Atomic Design + Package by Feature no frontend (React). | Manutenibilidade comprovada e aderência aos ADRs aprovados pela equipe. |
| **RNF-08** | **Desempenho** | Tempo de resposta para consulta e agendamento deve ser adequado para uso em recepção. | 95% das requisições respondidas em menos de 800ms sob carga normal de uso. |

---

## 🔒 Detalhamento de Conformidade e Segurança

1. **Gestão de Segredos**:
   - Em desenvolvimento local: `.env` gerenciado individualmente e bloqueado no `.gitignore`.
   - Na Azure: Variáveis configuradas na seção *App Service Configuration* / *Application Settings*.
2. **Health Check Probes**:
   - O orquestrador da nuvem pode monitorar periodicamente a rota `/health` para garantir auto-recuperação (restart automático se a conexão cair).
3. **Defense in Depth**:
   - Validação dupla: frontend para melhor experiência da recepcionista, backend como autoridade intransponível.
