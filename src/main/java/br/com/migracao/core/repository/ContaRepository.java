package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Integer> {
}