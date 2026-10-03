package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Odontograma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OdontogramaRepository extends JpaRepository<Odontograma, Integer> {
}