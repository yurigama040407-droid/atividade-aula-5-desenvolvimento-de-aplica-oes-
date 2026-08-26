package br.com.gerenciamento.dto;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;

public class AlunoResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String matricula;
    private Curso curso;
    private Status status;
    private Turno turno;
    private Double notaEnade;

    public AlunoResponseDTO() {
    }

    public AlunoResponseDTO(Long id, String nome, String email, String matricula, Curso curso, Status status, Turno turno, Double notaEnade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.curso = curso;
        this.status = status;
        this.turno = turno;
        this.notaEnade = notaEnade;
    }

    public static AlunoResponseDTO fromEntity(Aluno aluno) {
        if (aluno == null) {
            return null;
        }
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getMatricula(),
                aluno.getCurso(),
                aluno.getStatus(),
                aluno.getTurno(),
                aluno.getNotaEnade()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Double getNotaEnade() {
        return notaEnade;
    }

    public void setNotaEnade(Double notaEnade) {
        this.notaEnade = notaEnade;
    }
}
