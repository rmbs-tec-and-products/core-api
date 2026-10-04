package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CaixaRepository
        extends JpaRepository<Caixa, Integer> {

    Optional<Caixa> findFirstByDataFechamentoIsNullOrderByDataAberturaDesc();

    boolean existsByDataAberturaGreaterThanEqualAndDataAberturaLessThan(
            LocalDateTime inicio,
            LocalDateTime fim
    );

    List<Caixa> findByDataAberturaGreaterThanEqualAndDataAberturaLessThanOrderByDataAberturaDesc(
            LocalDateTime inicio,
            LocalDateTime fim
    );
}