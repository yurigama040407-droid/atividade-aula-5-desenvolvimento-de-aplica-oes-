package br.com.gerenciamento.controller;

import br.com.gerenciamento.dto.AlunoRequestDTO;
import br.com.gerenciamento.dto.AlunoResponseDTO;
import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.exception.AlunoNotFoundException;
import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.exception.RestExceptionHandler;
import br.com.gerenciamento.service.AlunoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoRestController.class)
@Import(RestExceptionHandler.class)
public class AlunoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoService alunoService;

    @Autowired
    private ObjectMapper objectMapper;

    private AlunoRequestDTO alunoRequestDTO;
    private AlunoResponseDTO alunoResponseDTO;

    @BeforeEach
    void setUp() {
        alunoRequestDTO = new AlunoRequestDTO(
                "Fernanda Lima",
                "fernanda@exemplo.com",
                "MAT9988",
                Curso.ADMINISTRACAO,
                Status.ATIVO,
                Turno.MATUTINO,
                9.5
        );

        alunoResponseDTO = new AlunoResponseDTO(
                10L,
                "Fernanda Lima",
                "fernanda@exemplo.com",
                "MAT9988",
                Curso.ADMINISTRACAO,
                Status.ATIVO,
                Turno.MATUTINO,
                9.5
        );
    }

    @Test
    @DisplayName("Deve cadastrar aluno com sucesso retornando status 201 Created")
    void deveCadastrarAlunoComSucesso() throws Exception {
        when(alunoService.criarComDTO(any(AlunoRequestDTO.class))).thenReturn(alunoResponseDTO);

        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alunoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Fernanda Lima"))
                .andExpect(jsonPath("$.email").value("fernanda@exemplo.com"))
                .andExpect(jsonPath("$.notaEnade").value(9.5));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar cadastrar aluno com campos inválidos")
    void deveRetornar400AoCadastrarComCamposInvalidos() throws Exception {
        AlunoRequestDTO dtoInvalido = new AlunoRequestDTO(
                "", // nome vazio
                "email-invalido", // formato inválido
                null,
                null,
                null,
                null,
                15.0 // nota > 10.0
        );

        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Erro de Validação"))
                .andExpect(jsonPath("$.fieldErrors.email").exists())
                .andExpect(jsonPath("$.fieldErrors.notaEnade").exists());
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict ao tentar cadastrar aluno com e-mail já existente")
    void deveRetornar409AoCadastrarEmailExistente() throws Exception {
        when(alunoService.criarComDTO(any(AlunoRequestDTO.class)))
                .thenThrow(new EmailExistsException("Já existe um aluno cadastrado com o e-mail: fernanda@exemplo.com"));

        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alunoRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflito de Dados"));
    }

    @Test
    @DisplayName("Deve listar todos os alunos retornando 200 OK")
    void deveListarTodosAlunos() throws Exception {
        when(alunoService.listarTodosDTO()).thenReturn(Arrays.asList(alunoResponseDTO));

        mockMvc.perform(get("/api/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].email").value("fernanda@exemplo.com"));
    }

    @Test
    @DisplayName("Deve buscar aluno por ID com status 200 OK")
    void deveBuscarAlunoPorId() throws Exception {
        when(alunoService.buscarDTOPorId(10L)).thenReturn(alunoResponseDTO);

        mockMvc.perform(get("/api/alunos/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Fernanda Lima"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao buscar ID inexistente")
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        when(alunoService.buscarDTOPorId(999L)).thenThrow(new AlunoNotFoundException(999L));

        mockMvc.perform(get("/api/alunos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Deve atualizar aluno com status 200 OK")
    void deveAtualizarAluno() throws Exception {
        when(alunoService.atualizarComDTO(eq(10L), any(AlunoRequestDTO.class))).thenReturn(alunoResponseDTO);

        mockMvc.perform(put("/api/alunos/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alunoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    @DisplayName("Deve remover aluno com status 204 No Content")
    void deveRemoverAluno() throws Exception {
        doNothing().when(alunoService).deletarPorId(10L);

        mockMvc.perform(delete("/api/alunos/10"))
                .andExpect(status().isNoContent());
    }
}
