package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
}