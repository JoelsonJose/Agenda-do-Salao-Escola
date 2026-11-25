# Plano de testes e critérios de aceite

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

Registre no relatório o resultado observado, data, commit, ambiente, passos e captura. Não invente resultados esperados como se tivessem sido observados.
