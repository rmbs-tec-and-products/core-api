package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.ProcedimentoProduto;
import br.com.migracao.core.domain.entity.ProcedimentoProdutoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcedimentoProdutoRepository
        extends JpaRepository<ProcedimentoProduto, ProcedimentoProdutoId> {

    boolean existsByProduto_Codigo(Integer codigo);

    List<ProcedimentoProduto> findByProcedimento_Codigo(
            Integer procedimentoCodigo
    );
}