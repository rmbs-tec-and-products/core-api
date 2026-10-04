package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    List<Produto> findAllByOrderByNomeAsc();

    @Query("""
            SELECT p
            FROM Produto p
            LEFT JOIN p.fornecedor f
            WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :pesquisa, '%'))
               OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', :pesquisa, '%'))
               OR LOWER(f.nome) LIKE LOWER(CONCAT('%', :pesquisa, '%'))
            ORDER BY p.nome
            """)
    List<Produto> pesquisar(
            @Param("pesquisa") String pesquisa
    );
}