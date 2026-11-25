# Evidências do desafio

Capturar durante a execução real do projeto e adicionar aqui. As evidências de tela devem exibir data e hora; remova dados pessoais e segredos. Este arquivo é checklist e não comprova execução.

1. Aplicação publicada mostrando agenda e conexão do banco.
2. Agendamento salvo e exibido na consulta da agenda.
3. Tentativa de conflito para a mesma profissional e resposta HTTP 409.
4. Cancelamento e novo agendamento no intervalo liberado.
5. Código de grupo na reserva, reinício do App Service e confirmação de persistência no banco (a consulta direta está registrada em [`consulta-sql.txt`](consulta-sql.txt)).
6. Tabela de execução dos critérios de aceite CA1–CA5 e captura da agenda diária.
7. URL pública e resultado de `/api/health` com `iniciado_em` e banco conectado.
8. Resultado do pipeline CI no commit entregue e link do vídeo de apresentação, conforme o PDF.

Execução observada em 25/11/2026: o site respondeu, `/api/health` indicou aplicação ativa e banco conectado, os sete cenários acima passaram, o registro fictício continuou disponível depois do reinício do App Service e uma consulta direta confirmou os registros no Azure SQL. As evidências de tela ainda precisam ser capturadas pelo grupo; não há imagens anexadas neste checkout.
