-- =========================================================================
-- DDL Completo das Tabelas do Sistema de Gerenciamento de Alunos
-- =========================================================================

-- Tabela de Usuários (Login/Autenticação)
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

-- Tabela de Alunos (com os novos campos: email e nota_enade)
CREATE TABLE IF NOT EXISTS aluno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    matricula VARCHAR(50) NOT NULL,
    curso VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    turno VARCHAR(20) NOT NULL,
    nota_enade DOUBLE NOT NULL
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_aluno_nome ON aluno(nome);
CREATE UNIQUE INDEX IF NOT EXISTS idx_aluno_email ON aluno(email);
