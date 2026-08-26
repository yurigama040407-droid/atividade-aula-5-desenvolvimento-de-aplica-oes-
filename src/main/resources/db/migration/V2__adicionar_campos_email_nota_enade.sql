-- Script de migração para adicionar os campos de e-mail e nota do ENADE à tabela aluno

ALTER TABLE aluno 
    ADD COLUMN email VARCHAR(100) NOT NULL UNIQUE,
    ADD COLUMN nota_enade DOUBLE NOT NULL DEFAULT 0.0;

-- Criando índice para garantir otimização de buscas por e-mail
CREATE UNIQUE INDEX idx_aluno_email ON aluno(email);
