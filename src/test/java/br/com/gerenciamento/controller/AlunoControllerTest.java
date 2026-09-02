package br.com.gerenciamento.controller;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.service.AlunoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoController.class)
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoService alunoService;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno(
                1L,
                "Lucas Moura",
                "lucas@exemplo.com",
                "MAT099",
                Curso.ADMINISTRACAO,
                Status.ATIVO,
                Turno.MATUTINO,
                8.0
        );
    }

    @Test
    @DisplayName("Deve abrir a tela de formulário de inserção de alunos")
    void deveAbrirFormularioInsercao() throws Exception {
        mockMvc.perform(get("/inserirAlunos"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/formAluno"))
                .andExpect(model().attributeExists("aluno"));
    }

    @Test
    @DisplayName("Deve redirecionar para a lista de alunos ao inserir aluno válido")
    void deveInserirAlunoComSucesso() throws Exception {
        when(alunoService.salvar(any(Aluno.class))).thenReturn(aluno);

        mockMvc.perform(post("/InsertAlunos")
                        .param("nome", "Lucas Moura")
                        .param("email", "lucas@exemplo.com")
                        .param("matricula", "MAT099")
                        .param("curso", "ADMINISTRACAO")
                        .param("status", "ATIVO")
                        .param("turno", "MATUTINO")
                        .param("notaEnade", "8.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alunos-adicionados"));
    }

    @Test
    @DisplayName("Deve retornar para o formulário se houver erros de validação")
    void deveRetornarAoFormularioComErrosDeValidacao() throws Exception {
        mockMvc.perform(post("/InsertAlunos")
                        .param("nome", "")
                        .param("email", "email-invalido")
                        .param("matricula", "")
                        .param("notaEnade", "15.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/formAluno"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("Deve listar os alunos adicionados na view listAlunos")
    void deveListarAlunos() throws Exception {
        when(alunoService.listarTodos()).thenReturn(Arrays.asList(aluno));

        mockMvc.perform(get("/alunos-adicionados"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/listAlunos"))
                .andExpect(model().attributeExists("alunosList"));
    }

    @Test
    @DisplayName("Deve exibir a tela com a média do ENADE")
    void deveExibirTelaMediaEnade() throws Exception {
        when(alunoService.calcularMediaEnade()).thenReturn(8.0);
        when(alunoService.listarTodos()).thenReturn(Arrays.asList(aluno));

        mockMvc.perform(get("/media-enade"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/media-enade"))
                .andExpect(model().attributeExists("mediaEnade"))
                .andExpect(model().attributeExists("alunosList"));
    }
}
