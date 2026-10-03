package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.OdontogramaProcedimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OdontogramaProcedimentoRepository
        extends JpaRepository<OdontogramaProcedimento, Integer> {
}