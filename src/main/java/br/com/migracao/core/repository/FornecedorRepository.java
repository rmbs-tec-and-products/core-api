package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FornecedorRepository
        extends JpaRepository<Fornecedor, Integer> {

    List<Fornecedor> findAllByOrderByNomeAsc();

    List<Fornecedor> findByNomeContainingIgnoreCaseOrTelefoneContainingIgnoreCaseOrderByNomeAsc(
            String nome,
            String telefone
    );
}