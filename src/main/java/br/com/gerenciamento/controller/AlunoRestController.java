package br.com.gerenciamento.controller;

import br.com.gerenciamento.dto.AlunoMapper;
import br.com.gerenciamento.dto.AlunoRequestDTO;
import br.com.gerenciamento.dto.AlunoResponseDTO;
import br.com.gerenciamento.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoRestController {

    private final AlunoService alunoService;

    @Autowired
    public AlunoRestController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> cadastrar(@Valid @RequestBody AlunoRequestDTO requestDTO) {
        AlunoResponseDTO responseDTO = alunoService.criarComDTO(requestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarTodos() {
        List<AlunoResponseDTO> alunos = alunoService.listarTodosDTO();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        AlunoResponseDTO responseDTO = alunoService.buscarDTOPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AlunoResponseDTO> buscarPorEmail(@PathVariable String email) {
        AlunoResponseDTO responseDTO = AlunoMapper.toDTO(alunoService.buscarPorEmail(email));
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/status/ativos")
    public ResponseEntity<List<AlunoResponseDTO>> listarAtivos() {
        List<AlunoResponseDTO> lista = alunoService.listarAtivos()
                .stream()
                .map(AlunoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/status/inativos")
    public ResponseEntity<List<AlunoResponseDTO>> listarInativos() {
        List<AlunoResponseDTO> lista = alunoService.listarInativos()
                .stream()
                .map(AlunoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AlunoResponseDTO>> buscarPorNome(@RequestParam(required = false) String nome) {
        List<AlunoResponseDTO> lista = alunoService.buscarPorNome(nome)
                .stream()
                .map(AlunoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AlunoRequestDTO requestDTO) {
        AlunoResponseDTO responseDTO = alunoService.atualizarComDTO(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        alunoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
