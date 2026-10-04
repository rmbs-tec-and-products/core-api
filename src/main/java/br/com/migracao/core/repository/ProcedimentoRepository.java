package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcedimentoRepository
        extends JpaRepository<Procedimento, Integer> {

    List<Procedimento> findAllByOrderByNomeAsc();

    List<Procedimento> findByNomeContainingIgnoreCaseOrderByNomeAsc(
            String nome
    );
}