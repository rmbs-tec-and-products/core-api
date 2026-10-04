package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Odontograma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OdontogramaRepository
        extends JpaRepository<Odontograma, Integer> {

    List<Odontograma> findByPaciente_CodigoAndOdontopediatriaOrderByDataDesc(
            Integer pacienteCodigo,
            Boolean odontopediatria
    );
}