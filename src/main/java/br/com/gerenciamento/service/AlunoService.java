package br.com.gerenciamento.service;

import br.com.gerenciamento.dto.AlunoMapper;
import br.com.gerenciamento.dto.AlunoRequestDTO;
import br.com.gerenciamento.dto.AlunoResponseDTO;
import br.com.gerenciamento.exception.AlunoNotFoundException;
import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    @Autowired
    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public Aluno salvar(Aluno aluno) {
        if (aluno.getEmail() != null && alunoRepository.findByEmail(aluno.getEmail().trim()).isPresent()) {
            throw new EmailExistsException("Já existe um aluno cadastrado com o e-mail: " + aluno.getEmail());
        }
        return alunoRepository.save(aluno);
    }

    @Transactional
    public Aluno atualizar(Long id, Aluno dadosAtualizados) {
        Aluno alunoExistente = buscarPorId(id);

        if (dadosAtualizados.getEmail() != null &&
                alunoRepository.findByEmailAndIdNot(dadosAtualizados.getEmail().trim(), id).isPresent()) {
            throw new EmailExistsException("Já existe outro aluno cadastrado com o e-mail: " + dadosAtualizados.getEmail());
        }

        alunoExistente.setNome(dadosAtualizados.getNome());
        alunoExistente.setEmail(dadosAtualizados.getEmail());
        alunoExistente.setMatricula(dadosAtualizados.getMatricula());
        alunoExistente.setCurso(dadosAtualizados.getCurso());
        alunoExistente.setStatus(dadosAtualizados.getStatus());
        alunoExistente.setTurno(dadosAtualizados.getTurno());
        alunoExistente.setNotaEnade(dadosAtualizados.getNotaEnade());

        return alunoRepository.save(alunoExistente);
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorEmail(String email) {
        return alunoRepository.findByEmail(email)
                .orElseThrow(() -> new AlunoNotFoundException("Aluno não encontrado com o e-mail: " + email));
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarAtivos() {
        return alunoRepository.findByStatusAtivo();
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarInativos() {
        return alunoRepository.findByStatusInativo();
    }

    @Transactional(readOnly = true)
    public List<Aluno> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return alunoRepository.findAll();
        }
        return alunoRepository.findByNomeContainingIgnoreCase(nome.trim());
    }

    @Transactional
    public void deletarPorId(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new AlunoNotFoundException(id);
        }
        alunoRepository.deleteById(id);
    }

    // Métodos para integração REST com DTOs
    @Transactional
    public AlunoResponseDTO criarComDTO(AlunoRequestDTO dto) {
        Aluno aluno = AlunoMapper.toEntity(dto);
        Aluno alunoSalvo = salvar(aluno);
        return AlunoMapper.toDTO(alunoSalvo);
    }

    @Transactional
    public AlunoResponseDTO atualizarComDTO(Long id, AlunoRequestDTO dto) {
        Aluno alunoExistente = buscarPorId(id);

        if (dto.getEmail() != null &&
                alunoRepository.findByEmailAndIdNot(dto.getEmail().trim(), id).isPresent()) {
            throw new EmailExistsException("Já existe outro aluno cadastrado com o e-mail: " + dto.getEmail());
        }

        AlunoMapper.copyToEntity(dto, alunoExistente);
        Aluno alunoAtualizado = alunoRepository.save(alunoExistente);
        return AlunoMapper.toDTO(alunoAtualizado);
    }

    @Transactional(readOnly = true)
    public AlunoResponseDTO buscarDTOPorId(Long id) {
        return AlunoMapper.toDTO(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listarTodosDTO() {
        return alunoRepository.findAll()
                .stream()
                .map(AlunoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
