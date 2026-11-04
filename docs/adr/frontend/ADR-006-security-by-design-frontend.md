# ADR-006: Princípios de Security by Design no Frontend

- **Status**: Aceito
- **Data**: 04/11/2026
- **Decisores**: Equipe de Desenvolvimento
- **Área**: Frontend

---

## 1. Contexto

Aplicações web modernas expõem interfaces ricas no navegador dos clientes, tornando o frontend um vetor comum para ataques como Cross-Site Scripting (XSS), vazamento de credenciais, injeção de scripts maliciosos e Clickjacking.

No projeto **Agenda do Salão-Escola**, os dados inseridos (nomes de clientes, serviços e horários) trafegam pela interface e são consumidos por profissionais e administradores. A segurança não deve ser um recurso adicionado ao final do desenvolvimento, mas concebida desde o primeiro momento da arquitetura (**Security by Design**).

---

## 2. Decisão

Incorporar os seguintes pilares de **Security by Design** no ciclo de desenvolvimento do Frontend:

### 2.1. Prevenção Contra Cross-Site Scripting (XSS)
- **Escape Automático do React**: Usar interpolação padrão do JSX (`{variavel}`), que sanitiza automaticamente strings contra XSS.
- **Proibição Estrita de `dangerouslySetInnerHTML`**: O uso de injeção crua de HTML é estritamente proibido no repositório.
- **Sanitização de URLs**: Links dinâmicos devem ser validados contra esquemas permitidos (`http:`, `https:`), bloqueando esquemas maliciosos como `javascript:`.

### 2.2. Gestão Segura de Variáveis de Ambiente e Segredos
- **Separação de Segredos**: Qualquer variável exposta ao frontend (ex: `VITE_API_BASE_URL`) é pública no bundle cliente.
- **Proibição de Credenciais no Client**: Nunca inserir chaves secretas de banco de dados, senhas de serviço ou tokens mestres em arquivos `.env` do frontend.
- O arquivo `.env` nunca deve ser comitado ao Git; apenas um `.env.example` sem valores sensíveis é versionado.

### 2.3. Validação de Entrada como Defesa em Camadas (Defense-in-Depth)
- O frontend executa validação antecipada de inputs (horário entre 08:00 e 20:00, dias de segunda a sábado, duração de 15 a 240 minutos) para proporcionar **feedback imediato ao usuário (UX)**.
- **Premissa de Confiança Zero (Zero Trust)**: A validação do frontend é puramente cosmética; o backend nunca confia nos dados enviados pelo cliente e sempre executa a validação definitiva.

### 2.4. Comunicação Segura e CORS
- Todas as chamadas para a API REST backend devem trafegar exclusivamente sobre **HTTPS** em ambientes de homologação e produção.
- Configurar cabeçalhos adequados e respeitar as políticas de **CORS (Cross-Origin Resource Sharing)** definidas pelo backend.

### 2.5. Tratamento Seguro de Erros e Mensagens
- Erros retornados pelo backend (ex: erro 500 ou falha de conexão) devem ser transformados em mensagens seguras e compreensíveis para o usuário comum.
- Não expor stacktraces, detalhes internos de banco de dados ou estrutura de servidores no console do navegador ou na tela da aplicação.

---

## 3. Justificativa

1. **Proteção de Dados**: Garante a integridade e privacidade das informações dos clientes e profissionais do Salão-Escola.
2. **Conformidade com Requisitos de Nuvem**: Atende às diretrizes de conformidade de segurança exigidas na implantação da Azure.
3. **Prevenção Proativa de Vulnerabilidades**: Mitiga riscos conhecidos da lista OWASP Top 10 para aplicações web.

---

## 4. Consequências

### Positivas
- Código frontend robusto contra vetores de ataque comuns.
- Zero vazamento acidental de chaves de infraestrutura ou segredos de banco de dados.
- Postura profissional de engenharia de software alinhada com as melhores práticas de mercado.

### Negativas / Mitigações
- Necessidade de cuidados constantes em revisões de PR para impedir inclusão de segredos ou dependências vulneráveis (mitigado com `npm audit`).

