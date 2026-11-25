# Plano de testes e critérios de aceite

## Escopo da atividade — execução manual na nuvem

Os casos abaixo foram executados na URL Azure em 25/11/2026, entre 16:03 e 16:10 (America/Sao_Paulo), com dados fictícios. O App Service serviu a aplicação e `/api/health` respondeu `UP`, `banco=conectado` e `iniciado_em`.

| Cenário | Esperado pelo enunciado | Resultado Azure |
|---|---|---|
| Ana, Corte 30 min, início 14:00 | `201` | `201` — passou |
| Ana, novo início 14:15 no mesmo dia | `409` por sobreposição | `409` — passou |
| Ana, novo início 14:30 no mesmo dia | `201` (horários contíguos) | `201` — passou |
| Bruno, início 14:15 | `201` | `201` — passou |
| Cancelar o primeiro atendimento e reutilizar Ana às 14:00 | Cancelamento e nova reserva aceitos | `200` no cancelamento e `201` na nova reserva — passou |
| Escova 45 min às 19:45 | `400`, excede o expediente | `400` — passou |
| Reserva no domingo | `400` | `400` — passou |

Foi usado o nome fictício `GXX-PALAVRA`. Após reiniciar o App Service, `iniciado_em` mudou de `16:08` para `16:10`, `/api/health` voltou `UP` com o banco conectado, e o mesmo agendamento continuou na consulta diária (3 registros retornados). A consulta SQL direta e as capturas de tela com data/hora ainda precisam ser anexadas em `evidencias/`.

## Testes automatizados do backend

`backend/src/test/java/br/edu/cloudchallenge/agenda/AgendamentoApiTest.java` verifica o contrato HTTP com banco H2 isolado:

- health e conexão ao banco;
- catálogos fictícios de serviço e profissional;
- criação, cálculo do horário final e horários contíguos;
- conflito para a mesma profissional e aceitação para profissional diferente;
- cancelamento, liberação do horário e manutenção do histórico;
- domingo e atendimento que ultrapassa 20h;
- validação de campos, IDs inexistentes e agenda diária filtrada.

Comando: `mvn -f backend/pom.xml verify`.

## Testes automatizados do frontend

Os testes do componente de login verificam entrada após credenciais aceitas e mensagem de erro diante de HTTP 401.

Comando: `cd frontend; npm ci; npm test`.

## Demonstração manual e evidências

Execute a aplicação com H2 em arquivo e use dados fictícios. Capture também o processo de reiniciar a API e consultar o registro salvo; o teste automatizado em banco em memória não substitui essa evidência de persistência. O índice das capturas necessárias está em [`../evidencias/README.md`](../evidencias/README.md).

O backend passou nos 10 testes automatizados locais; o frontend passou nos 2 testes e no build Vite. O JAR foi empacotado em uma cópia temporária porque outro processo mantinha o diretório `backend/target` em uso. O deploy e os testes Azure acima ocorreram neste branch local; ainda não há commit publicado associado a eles.

Registre também o commit e as capturas reais quando forem produzidos. Os resultados acima foram observados; as capturas não foram fabricadas.
