package br.com.gerenciamento.model;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    @Size(min = 5, max = 50, message = "O Nome deve conter entre 5 a 50 caracteres")
    @NotBlank(message = "O nome não pode ser vazio")
    @NotNull(message = "O nome é obrigatório")
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @NotBlank(message = "O e-mail não pode ser vazio")
    @Email(message = "Informe um e-mail válido")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
    private String email;

    @Column(name = "matricula", nullable = false)
    @NotNull(message = "A matrícula é obrigatória")
    @Size(min = 3, message = "É necessário gerar o número de matrícula")
    private String matricula;

    @Column(name = "curso", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "O curso é obrigatório")
    private Curso curso;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status é obrigatório")
    private Status status;

    @Column(name = "turno", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "O turno é obrigatório")
    private Turno turno;

    @Column(name = "nota_enade", nullable = false)
    @NotNull(message = "A nota do ENADE é obrigatória")
    @DecimalMin(value = "0.0", inclusive = true, message = "A nota do ENADE não pode ser menor que 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "A nota do ENADE não pode ser maior que 10.0")
    private Double notaEnade;

    public Aluno() {
    }

    public Aluno(Long id, String nome, String email, String matricula, Curso curso, Status status, Turno turno, Double notaEnade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.curso = curso;
        this.status = status;
        this.turno = turno;
        this.notaEnade = notaEnade;
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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aluno aluno = (Aluno) o;
        return Objects.equals(id, aluno.id) && Objects.equals(email, aluno.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", matricula='" + matricula + '\'' +
                ", curso=" + curso +
                ", status=" + status +
                ", turno=" + turno +
                ", notaEnade=" + notaEnade +
                '}';
    }
}
