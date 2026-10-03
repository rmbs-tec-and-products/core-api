package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistaRepository extends JpaRepository<Dentista, Integer> {
}