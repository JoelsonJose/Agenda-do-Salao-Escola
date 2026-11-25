-- Azure SQL / Microsoft SQL Server seed data. Safe to re-run.
IF NOT EXISTS (SELECT 1 FROM dbo.profissionais WHERE nome = N'Ana')
    INSERT INTO dbo.profissionais (nome) VALUES (N'Ana');
IF NOT EXISTS (SELECT 1 FROM dbo.profissionais WHERE nome = N'Bruno')
    INSERT INTO dbo.profissionais (nome) VALUES (N'Bruno');
IF NOT EXISTS (SELECT 1 FROM dbo.profissionais WHERE nome = N'Carla')
    INSERT INTO dbo.profissionais (nome) VALUES (N'Carla');

IF NOT EXISTS (SELECT 1 FROM dbo.servicos WHERE nome = N'Corte')
    INSERT INTO dbo.servicos (nome, duracao_minutos) VALUES (N'Corte', 30);
IF NOT EXISTS (SELECT 1 FROM dbo.servicos WHERE nome = N'Escova')
    INSERT INTO dbo.servicos (nome, duracao_minutos) VALUES (N'Escova', 45);
IF NOT EXISTS (SELECT 1 FROM dbo.servicos WHERE nome = N'Manicure')
    INSERT INTO dbo.servicos (nome, duracao_minutos) VALUES (N'Manicure', 40);
IF NOT EXISTS (SELECT 1 FROM dbo.servicos WHERE nome = N'Coloração')
    INSERT INTO dbo.servicos (nome, duracao_minutos) VALUES (N'Coloração', 120);

@@
