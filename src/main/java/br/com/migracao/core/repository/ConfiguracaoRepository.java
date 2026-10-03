package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Integer> {
}