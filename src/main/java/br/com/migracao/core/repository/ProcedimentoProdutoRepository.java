package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.ProcedimentoProduto;
import br.com.migracao.core.domain.entity.ProcedimentoProdutoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedimentoProdutoRepository
        extends JpaRepository<ProcedimentoProduto, ProcedimentoProdutoId> {
}