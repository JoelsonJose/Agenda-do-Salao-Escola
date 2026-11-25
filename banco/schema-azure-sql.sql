-- Azure SQL / Microsoft SQL Server schema for the Desafio 03 app.
-- Run once in the team's Azure SQL database before deploying the app.

IF OBJECT_ID(N'dbo.servicos', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.servicos (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_servicos PRIMARY KEY,
        nome NVARCHAR(100) NOT NULL,
        duracao_minutos INT NOT NULL,
        CONSTRAINT chk_servico_duracao CHECK (duracao_minutos >= 15 AND duracao_minutos <= 240)
    );
END;

IF OBJECT_ID(N'dbo.profissionais', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.profissionais (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_profissionais PRIMARY KEY,
        nome NVARCHAR(100) NOT NULL
    );
END;

IF OBJECT_ID(N'dbo.agendamentos', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.agendamentos (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_agendamentos PRIMARY KEY,
        cliente NVARCHAR(100) NOT NULL,
        servico_id BIGINT NOT NULL,
        profissional_id BIGINT NOT NULL,
        data_hora_inicio DATETIMEOFFSET(0) NOT NULL,
        data_hora_fim DATETIMEOFFSET(0) NOT NULL,
        status VARCHAR(20) NOT NULL CONSTRAINT df_agendamento_status DEFAULT 'ATIVO',
        CONSTRAINT fk_agendamento_servico FOREIGN KEY (servico_id)
            REFERENCES dbo.servicos(id) ON DELETE NO ACTION,
        CONSTRAINT fk_agendamento_profissional FOREIGN KEY (profissional_id)
            REFERENCES dbo.profissionais(id) ON DELETE NO ACTION,
        CONSTRAINT chk_agendamento_horario CHECK (data_hora_fim > data_hora_inicio),
        CONSTRAINT chk_agendamento_status CHECK (status IN ('ATIVO', 'CANCELADO'))
    );
END;

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
    WHERE name = N'idx_agendamento_conflito'
      AND object_id = OBJECT_ID(N'dbo.agendamentos')
)
    CREATE INDEX idx_agendamento_conflito
        ON dbo.agendamentos (profissional_id, status, data_hora_inicio, data_hora_fim);

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
    WHERE name = N'idx_agendamento_data'
      AND object_id = OBJECT_ID(N'dbo.agendamentos')
)
    CREATE INDEX idx_agendamento_data ON dbo.agendamentos (data_hora_inicio);

@@
