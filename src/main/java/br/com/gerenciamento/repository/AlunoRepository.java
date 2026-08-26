package br.com.gerenciamento.repository;

import br.com.gerenciamento.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @Query("SELECT a FROM Aluno a WHERE a.status = 'ATIVO' ")
    List<Aluno> findByStatusAtivo();

    @Query("SELECT i FROM Aluno i WHERE i.status = 'INATIVO' ")
    List<Aluno> findByStatusInativo();

    List<Aluno> findByNomeContainingIgnoreCase(String nome);

    Optional<Aluno> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Aluno> findByEmailAndIdNot(String email, Long id);
}
