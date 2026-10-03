package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaixaRepository extends JpaRepository<Caixa, Integer> {
}