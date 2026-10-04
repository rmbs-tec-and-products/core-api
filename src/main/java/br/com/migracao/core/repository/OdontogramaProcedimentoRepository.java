package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.OdontogramaProcedimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OdontogramaProcedimentoRepository
        extends JpaRepository<OdontogramaProcedimento, Integer> {

    List<OdontogramaProcedimento>
    findByOdontograma_CodigoOrderByCodigoAsc(
            Integer odontogramaCodigo
    );

    Optional<OdontogramaProcedimento>
    findByCodigoAndOdontograma_Codigo(
            Integer codigo,
            Integer odontogramaCodigo
    );
}