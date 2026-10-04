package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DentistaRepository
        extends JpaRepository<Dentista, Integer> {

    List<Dentista> findAllByOrderByNomeAsc();

    List<Dentista> findByNomeContainingIgnoreCaseOrderByNomeAsc(
            String nome
    );
}