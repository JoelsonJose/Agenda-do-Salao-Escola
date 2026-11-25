# Implantação do nível A — Azure

Este roteiro registra os passos pedidos para o Desafio 03: aplicação no Azure App Service e banco relacional gerenciado pelo grupo. Use dados fictícios. Não coloque senhas, chaves ou connection strings com senha neste repositório.

## Recursos existentes

| Recurso | Valor observado |
|---|---|
| Grupo de recursos | `rg-agenda-salao-escola` |
| App Service | `agenda-salao-escola-jj` (Linux, Java SE 21, plano F1) |
| URL pública | `https://agenda-salao-escola-jj-hwdra6czavawczht.brazilsouth-01.azurewebsites.net` |
| Servidor SQL | `agenda-salao-jj-20261125-southafrica.database.windows.net` (South Africa North) |
| Banco SQL | `free-sql-db-6668541` |

Estado conferido em 25/11/2026: o App Service está com o JAR desta aplicação publicado e inicia com `java -jar /home/site/wwwroot/app.jar`. A URL raiz respondeu HTTP 200; `/api/health` respondeu `UP`, `banco=conectado` e incluiu `iniciado_em`. A autenticação SQL está habilitada, e os nomes de configuração `DATABASE_USERNAME`, `DATABASE_PASSWORD` e `APP_PASSWORD` estão no App Service (os valores permanecem fora do repositório). O banco estava `Online` após o teste, com `AutoPause`; a oferta gratuita mostrava excedentes desativados. As regras `AllowAzureServices` (0.0.0.0) e do IP cliente foram confirmadas. A consulta SQL direta de leitura foi realizada; seu resultado sem credenciais está em `../evidencias/consulta-sql.txt`.

## 1. Banco Azure SQL

1. Confira no portal que o banco está na oferta gratuita e com cobrança de excedentes desativada.
2. Em **Networking**, use o endpoint público conforme a atividade, habilite **Allow Azure services and resources to access this server** e adicione o IP atual para o acesso de teste solicitado. As duas regras foram confirmadas no firewall.
3. O perfil `azure` aplica, ao iniciar, os scripts idempotentes `banco/schema-azure-sql.sql` e `banco/dados-iniciais-azure-sql.sql`. Confirme que as tabelas `servicos`, `profissionais` e `agendamentos` existem e que os catálogos fictícios foram carregados.

O perfil Azure usa autenticação SQL. Cadastre usuário e senha somente em **App Service → Configuration → Application settings**, nunca em arquivos versionados.

## 2. Configurar o App Service

Em **Configuration → Application settings**, configure:

| Nome | Valor |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `azure` |
| `DATABASE_URL` | `jdbc:sqlserver://agenda-salao-jj-20261125-southafrica.database.windows.net:1433;databaseName=free-sql-db-6668541;encrypt=true;trustServerCertificate=false;loginTimeout=30;` |
| `DATABASE_USERNAME` | usuário de autenticação SQL do servidor/banco |
| `DATABASE_PASSWORD` | senha correspondente, guardada apenas no portal |
| `APP_USERNAME` | usuário HTTP Basic de demonstração |
| `APP_PASSWORD` | senha HTTP Basic de demonstração, guardada apenas no portal |

Salve as configurações e aguarde o reinício do App Service. O frontend compilado é servido pelo mesmo JAR e endereço da API.

## 3. Compilar e publicar

Na raiz do repositório:

```powershell
cd frontend
npm ci
npm test
npm run build
cd ..
mvn -f backend/pom.xml clean verify
```

O artefato esperado é `backend/target/agenda-salao-escola-0.1.0-SNAPSHOT.jar`. Publique-o no App Service como aplicação Java SE 21 (`app.jar`) usando o método de deploy habilitado para o recurso. Confirme no portal que o deployment terminou sem erro e reinicie o App Service.

## 4. Verificações exigidas

Abra `https://agenda-salao-escola-jj-hwdra6czavawczht.brazilsouth-01.azurewebsites.net/` e use o login de demonstração definido nas configurações Azure. Na auditoria, a raiz respondeu HTTP 200. `/api/health` respondeu `UP`, `banco` como `conectado` e incluiu `iniciado_em`.

Os casos de aceite do Desafio 03 foram executados com dados fictícios; os resultados observados estão em `docs/testes.md`:

| Caso | Resultado esperado |
|---|---|
| Ana, Corte de 30 min, 14:00 | Criado (`201`) |
| Ana, mesmo dia, 14:15 | Conflito (`409`) |
| Ana, mesmo dia, 14:30 | Criado (`201`) |
| Bruno, mesmo dia, 14:15 | Criado (`201`) |
| Cancelar o atendimento inicial e reservar Ana às 14:00 | Cancelamento e novo agendamento aceitos |
| Escova de 45 min às 19:45 | Rejeitado (`400`), pois ultrapassa 20:00 |
| Agendamento no domingo | Rejeitado (`400`) |

Para provar persistência, foi criado um registro com `GXX-PALAVRA`, confirmado na lista, e o App Service foi reiniciado. O campo `iniciado_em` mudou e o mesmo registro continuou presente. A consulta SQL direta e as capturas reais com data/hora ainda faltam em `evidencias/`; não marque essas duas etapas como concluídas antes de anexá-las.

## Estado de entrega

Estado: publicação, inicialização do backend, conexão Azure SQL, catálogo, casos de aceite, persistência após restart e consulta SQL direta foram confirmados. Faltam as capturas da execução com data/hora e o vídeo de apresentação.
