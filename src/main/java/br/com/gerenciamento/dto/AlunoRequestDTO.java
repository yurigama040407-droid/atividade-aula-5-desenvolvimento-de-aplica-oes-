package br.com.gerenciamento.dto;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;

import javax.validation.constraints.*;

public class AlunoRequestDTO {

    @NotBlank(message = "O nome não pode ser vazio")
    @Size(min = 5, max = 50, message = "O Nome deve conter entre 5 a 50 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail não pode ser vazio")
    @Email(message = "Informe um e-mail válido")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
    private String email;

    @NotBlank(message = "A matrícula é obrigatória")
    @Size(min = 3, message = "É necessário gerar o número de matrícula")
    private String matricula;

    @NotNull(message = "O curso é obrigatório")
    private Curso curso;

    @NotNull(message = "O status é obrigatório")
    private Status status;

    @NotNull(message = "O turno é obrigatório")
    private Turno turno;

    @NotNull(message = "A nota do ENADE é obrigatória")
    @DecimalMin(value = "0.0", inclusive = true, message = "A nota do ENADE não pode ser menor que 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "A nota do ENADE não pode ser maior que 10.0")
    private Double notaEnade;

    public AlunoRequestDTO() {
    }

    public AlunoRequestDTO(String nome, String email, String matricula, Curso curso, Status status, Turno turno, Double notaEnade) {
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.curso = curso;
        this.status = status;
        this.turno = turno;
        this.notaEnade = notaEnade;
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
