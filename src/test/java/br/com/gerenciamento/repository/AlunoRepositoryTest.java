package br.com.gerenciamento.repository;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno alunoAtivo;
    private Aluno alunoInativo;

    @BeforeEach
    void setUp() {
        alunoRepository.deleteAll();

        alunoAtivo = new Aluno(
                null,
                "Carlos Alberto",
                "carlos@teste.com",
                "MAT001",
                Curso.ADMINISTRACAO,
                Status.ATIVO,
                Turno.MATUTINO,
                7.5
        );

        alunoInativo = new Aluno(
                null,
                "Beatriz Ramos",
                "beatriz@teste.com",
                "MAT002",
                Curso.DIREITO,
                Status.INATIVO,
                Turno.NOTURNO,
                9.2
        );

        alunoRepository.save(alunoAtivo);
        alunoRepository.save(alunoInativo);
    }

    @Test
    @DisplayName("Deve encontrar aluno pelo e-mail")
    void deveEncontrarAlunoPorEmail() {
        Optional<Aluno> resultado = alunoRepository.findByEmail("carlos@teste.com");

        assertTrue(resultado.isPresent());
        assertEquals("Carlos Alberto", resultado.get().getNome());
        assertEquals(7.5, resultado.get().getNotaEnade());
    }

    @Test
    @DisplayName("Deve verificar se existe aluno cadastrado com determinado e-mail")
    void deveVerificarExistenciaPorEmail() {
        assertTrue(alunoRepository.existsByEmail("carlos@teste.com"));
        assertFalse(alunoRepository.existsByEmail("inexistente@teste.com"));
    }

    @Test
    @DisplayName("Deve encontrar apenas alunos com status ATIVO")
    void deveEncontrarAlunosAtivos() {
        List<Aluno> ativos = alunoRepository.findByStatusAtivo();

        assertEquals(1, ativos.size());
        assertEquals(Status.ATIVO, ativos.get(0).getStatus());
        assertEquals("Carlos Alberto", ativos.get(0).getNome());
    }

    @Test
    @DisplayName("Deve encontrar apenas alunos com status INATIVO")
    void deveEncontrarAlunosInativos() {
        List<Aluno> inativos = alunoRepository.findByStatusInativo();

        assertEquals(1, inativos.size());
        assertEquals(Status.INATIVO, inativos.get(0).getStatus());
        assertEquals("Beatriz Ramos", inativos.get(0).getNome());
    }

    @Test
    @DisplayName("Deve pesquisar alunos por parte do nome ignorando case")
    void devePesquisarPorNomeIgnoreCase() {
        List<Aluno> resultado = alunoRepository.findByNomeContainingIgnoreCase("carlos");

        assertEquals(1, resultado.size());
        assertEquals("Carlos Alberto", resultado.get(0).getNome());
    }
}
