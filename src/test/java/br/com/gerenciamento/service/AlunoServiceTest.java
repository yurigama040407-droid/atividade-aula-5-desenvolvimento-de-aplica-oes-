package br.com.gerenciamento.service;

import br.com.gerenciamento.dto.AlunoRequestDTO;
import br.com.gerenciamento.dto.AlunoResponseDTO;
import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.exception.AlunoNotFoundException;
import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService alunoService;

    private Aluno aluno;
    private AlunoRequestDTO alunoRequestDTO;

    @BeforeEach
    void setUp() {
        aluno = new Aluno(
                1L,
                "João da Silva",
                "joao.silva@exemplo.com",
                "MAT12345",
                Curso.ADMINISTRACAO,
                Status.ATIVO,
                Turno.MATUTINO,
                8.5
        );

        alunoRequestDTO = new AlunoRequestDTO(
                "João da Silva",
                "joao.silva@exemplo.com",
                "MAT12345",
                Curso.ADMINISTRACAO,
                Status.ATIVO,
                Turno.MATUTINO,
                8.5
        );
    }

    @Test
    @DisplayName("Deve salvar aluno com sucesso quando o e-mail não existir")
    void deveSalvarAlunoComSucesso() {
        when(alunoRepository.findByEmail("joao.silva@exemplo.com")).thenReturn(Optional.empty());
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        Aluno salvo = alunoService.salvar(aluno);

        assertNotNull(salvo);
        assertEquals("joao.silva@exemplo.com", salvo.getEmail());
        assertEquals(8.5, salvo.getNotaEnade());
        verify(alunoRepository, times(1)).findByEmail("joao.silva@exemplo.com");
        verify(alunoRepository, times(1)).save(aluno);
    }

    @Test
    @DisplayName("Deve lançar EmailExistsException ao tentar salvar aluno com e-mail já existente")
    void deveLancarExcecaoAoSalvarAlunoComEmailExistente() {
        when(alunoRepository.findByEmail("joao.silva@exemplo.com")).thenReturn(Optional.of(aluno));

        EmailExistsException exception = assertThrows(
                EmailExistsException.class,
                () -> alunoService.salvar(aluno)
        );

        assertTrue(exception.getMessage().contains("Já existe um aluno cadastrado com o e-mail"));
        verify(alunoRepository, times(1)).findByEmail("joao.silva@exemplo.com");
        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    @DisplayName("Deve criar aluno a partir de DTO com sucesso")
    void deveCriarAlunoComDTO() {
        when(alunoRepository.findByEmail("joao.silva@exemplo.com")).thenReturn(Optional.empty());
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        AlunoResponseDTO responseDTO = alunoService.criarComDTO(alunoRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("joao.silva@exemplo.com", responseDTO.getEmail());
        assertEquals(8.5, responseDTO.getNotaEnade());
    }

    @Test
    @DisplayName("Deve atualizar aluno com sucesso")
    void deveAtualizarAlunoComSucesso() {
        Aluno alunoAtualizado = new Aluno(
                1L,
                "João Silva Editado",
                "joao.novo@exemplo.com",
                "MAT12345",
                Curso.DIREITO,
                Status.ATIVO,
                Turno.NOTURNO,
                9.0
        );

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.findByEmailAndIdNot("joao.novo@exemplo.com", 1L)).thenReturn(Optional.empty());
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoAtualizado);

        Aluno resultado = alunoService.atualizar(1L, alunoAtualizado);

        assertNotNull(resultado);
        assertEquals("João Silva Editado", resultado.getNome());
        assertEquals("joao.novo@exemplo.com", resultado.getEmail());
        assertEquals(9.0, resultado.getNotaEnade());
    }

    @Test
    @DisplayName("Deve lançar EmailExistsException ao atualizar aluno com e-mail pertencente a outro aluno")
    void deveLancarExcecaoAoAtualizarComEmailDeOutroAluno() {
        Aluno outroAluno = new Aluno(
                2L,
                "Maria Souza",
                "maria@exemplo.com",
                "MAT67890",
                Curso.ENFERMAGEM,
                Status.ATIVO,
                Turno.MATUTINO,
                7.0
        );

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.findByEmailAndIdNot("maria@exemplo.com", 1L)).thenReturn(Optional.of(outroAluno));

        Aluno dadosParaAtualizar = new Aluno(
                1L,
                "João Atualizado",
                "maria@exemplo.com",
                "MAT12345",
                Curso.ADMINISTRACAO,
                Status.ATIVO,
                Turno.MATUTINO,
                8.0
        );

        assertThrows(
                EmailExistsException.class,
                () -> alunoService.atualizar(1L, dadosParaAtualizar)
        );

        verify(alunoRepository, never()).save(any(Aluno.class));
    }

    @Test
    @DisplayName("Deve buscar aluno por ID com sucesso")
    void deveBuscarAlunoPorIdComSucesso() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        Aluno encontrado = alunoService.buscarPorId(1L);

        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getId());
        assertEquals("João da Silva", encontrado.getNome());
    }

    @Test
    @DisplayName("Deve lançar AlunoNotFoundException ao buscar ID inexistente")
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(alunoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                AlunoNotFoundException.class,
                () -> alunoService.buscarPorId(999L)
        );
    }

    @Test
    @DisplayName("Deve listar todos os alunos")
    void deveListarTodosOsAlunos() {
        when(alunoRepository.findAll()).thenReturn(Arrays.asList(aluno));

        List<Aluno> lista = alunoService.listarTodos();

        assertEquals(1, lista.size());
        assertEquals(aluno.getNome(), lista.get(0).getNome());
    }

    @Test
    @DisplayName("Deve deletar aluno por ID")
    void deveDeletarAlunoPorId() {
        when(alunoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(alunoRepository).deleteById(1L);

        assertDoesNotThrow(() -> alunoService.deletarPorId(1L));
        verify(alunoRepository, times(1)).deleteById(1L);
    }
}
