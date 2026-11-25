-- =====================================================================
-- PROJETO: Agenda do Salão-Escola (Cloud Challenge)
-- SCRIPT: banco/dados-iniciais.sql
-- BANCO PADRÃO: H2 Database
-- COMPATIBILIDADE: H2, PostgreSQL, Azure SQL
-- =====================================================================
-- Este script realiza a carga inicial fictícia (seed data) necessária
-- para a execução da aplicação e testes dos critérios de aceitação (CA1 a CA5).
-- Utiliza padrão idempotente (INSERT ... WHERE NOT EXISTS) para evitar
-- duplicatas caso seja executado repetidamente.
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1. CARGA DE PROFISSIONAIS FICTÍCIOS
-- Obrigatórios para os testes: 'Ana' e 'Bruno'
-- ---------------------------------------------------------------------
INSERT INTO profissionais (nome)
SELECT 'Ana'
WHERE NOT EXISTS (
    SELECT 1 FROM profissionais WHERE nome = 'Ana'
);

INSERT INTO profissionais (nome)
SELECT 'Bruno'
WHERE NOT EXISTS (
    SELECT 1 FROM profissionais WHERE nome = 'Bruno'
);

INSERT INTO profissionais (nome)
SELECT 'Carla'
WHERE NOT EXISTS (
    SELECT 1 FROM profissionais WHERE nome = 'Carla'
);

-- ---------------------------------------------------------------------
-- 2. CARGA DE SERVIÇOS FICTÍCIOS
-- Obrigatórios para os testes:
--   - 'Corte' com 30 minutos (permite agendamentos ágeis de 30m)
--   - 'Escova' com 45 minutos (usado no teste CA5 de extrapolação às 19:45)
-- Serviços complementares dentro da faixa regulamentar (15 a 240 minutos):
--   - 'Manicure' com 40 minutos
--   - 'Coloração' com 120 minutos
-- ---------------------------------------------------------------------
INSERT INTO servicos (nome, duracao_minutos)
SELECT 'Corte', 30
WHERE NOT EXISTS (
    SELECT 1 FROM servicos WHERE nome = 'Corte'
);

INSERT INTO servicos (nome, duracao_minutos)
SELECT 'Escova', 45
WHERE NOT EXISTS (
    SELECT 1 FROM servicos WHERE nome = 'Escova'
);

INSERT INTO servicos (nome, duracao_minutos)
SELECT 'Manicure', 40
WHERE NOT EXISTS (
    SELECT 1 FROM servicos WHERE nome = 'Manicure'
);

INSERT INTO servicos (nome, duracao_minutos)
SELECT 'Coloração', 120
WHERE NOT EXISTS (
    SELECT 1 FROM servicos WHERE nome = 'Coloração'
);

