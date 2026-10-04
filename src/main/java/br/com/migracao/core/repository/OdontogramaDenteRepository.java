package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.OdontogramaDente;
import br.com.migracao.core.domain.entity.OdontogramaDenteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OdontogramaDenteRepository
        extends JpaRepository<OdontogramaDente, OdontogramaDenteId> {

    List<OdontogramaDente> findByOdontograma_CodigoOrderById_DenteAsc(
            Integer odontogramaCodigo
    );
}