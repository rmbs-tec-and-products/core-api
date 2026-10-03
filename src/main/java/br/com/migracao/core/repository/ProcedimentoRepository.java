package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedimentoRepository extends JpaRepository<Procedimento, Integer> {
}