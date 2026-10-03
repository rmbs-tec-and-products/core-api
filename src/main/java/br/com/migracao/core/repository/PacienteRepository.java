package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    @Query("""
            SELECT p
            FROM Paciente p
            WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :pesquisa, '%'))
               OR p.telefone LIKE CONCAT('%', :pesquisa, '%')
               OR p.celular LIKE CONCAT('%', :pesquisa, '%')
               OR p.cpf LIKE CONCAT('%', :pesquisa, '%')
            ORDER BY p.nome
            """)
    List<Paciente> pesquisar(
            @Param("pesquisa") String pesquisa
    );
}