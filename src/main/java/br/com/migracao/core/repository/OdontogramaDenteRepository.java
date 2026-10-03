package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.OdontogramaDente;
import br.com.migracao.core.domain.entity.OdontogramaDenteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OdontogramaDenteRepository
        extends JpaRepository<OdontogramaDente, OdontogramaDenteId> {
}