package br.com.gerenciamento.dto;

import br.com.gerenciamento.model.Aluno;

public final class AlunoMapper {

    private AlunoMapper() {
    }

    public static Aluno toEntity(AlunoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setEmail(dto.getEmail());
        aluno.setMatricula(dto.getMatricula());
        aluno.setCurso(dto.getCurso());
        aluno.setStatus(dto.getStatus());
        aluno.setTurno(dto.getTurno());
        aluno.setNotaEnade(dto.getNotaEnade());
        return aluno;
    }

    public static void copyToEntity(AlunoRequestDTO dto, Aluno aluno) {
        if (dto == null || aluno == null) {
            return;
        }
        aluno.setNome(dto.getNome());
        aluno.setEmail(dto.getEmail());
        aluno.setMatricula(dto.getMatricula());
        aluno.setCurso(dto.getCurso());
        aluno.setStatus(dto.getStatus());
        aluno.setTurno(dto.getTurno());
        aluno.setNotaEnade(dto.getNotaEnade());
    }

    public static AlunoResponseDTO toDTO(Aluno aluno) {
        return AlunoResponseDTO.fromEntity(aluno);
    }
}
