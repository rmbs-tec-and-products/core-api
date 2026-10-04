package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendaRepository
        extends JpaRepository<Agenda, Integer> {

    List<Agenda> findByDataGreaterThanEqualAndDataLessThanOrderByDataAsc(
            LocalDateTime inicio,
            LocalDateTime fim
    );

    List<Agenda> findByDataGreaterThanEqualAndDataLessThanAndDentista_CodigoOrderByDataAsc(
            LocalDateTime inicio,
            LocalDateTime fim,
            Integer dentistaCodigo
    );

    boolean existsByDentista_CodigoAndData(
            Integer dentistaCodigo,
            LocalDateTime data
    );

    boolean existsByDentista_CodigoAndDataAndCodigoNot(
            Integer dentistaCodigo,
            LocalDateTime data,
            Integer codigo
    );

    boolean existsByPaciente_CodigoAndDataGreaterThanEqualAndDataLessThan(
            Integer pacienteCodigo,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    boolean existsByPaciente_CodigoAndDataGreaterThanEqualAndDataLessThanAndCodigoNot(
            Integer pacienteCodigo,
            LocalDateTime inicio,
            LocalDateTime fim,
            Integer codigo
    );
}