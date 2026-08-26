package br.com.gerenciamento.exception;

public class AlunoNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AlunoNotFoundException(String message) {
        super(message);
    }

    public AlunoNotFoundException(Long id) {
        super("Aluno não encontrado com o ID: " + id);
    }
}
