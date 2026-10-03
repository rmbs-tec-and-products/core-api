package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Anamnese;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnamneseRepository extends JpaRepository<Anamnese, Integer> {
}