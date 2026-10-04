package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.CaixaHistorico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaixaHistoricoRepository
        extends JpaRepository<CaixaHistorico, Integer> {

    List<CaixaHistorico> findByCaixa_CodigoOrderByDataDesc(
            Integer caixaCodigo
    );
}