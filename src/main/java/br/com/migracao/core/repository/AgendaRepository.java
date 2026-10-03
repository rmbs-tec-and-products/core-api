package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {
}