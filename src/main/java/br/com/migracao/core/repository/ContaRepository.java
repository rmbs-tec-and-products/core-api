package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Conta;
import br.com.migracao.core.domain.enums.StatusConta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ContaRepository
        extends JpaRepository<Conta, Integer> {

    @Query("""
            SELECT c
            FROM Conta c
            WHERE c.dataVencimento >= :inicio
              AND c.dataVencimento < :fim
              AND (:tipo IS NULL OR c.tipo = :tipo)
              AND (:status IS NULL OR c.status = :status)
              AND (
                    :pesquisa IS NULL
                    OR LOWER(c.descricao)
                       LIKE LOWER(CONCAT('%', :pesquisa, '%'))
                  )
            ORDER BY c.dataVencimento ASC, c.codigo ASC
            """)
    List<Conta> pesquisar(
            @Param("inicio")
            LocalDateTime inicio,

            @Param("fim")
            LocalDateTime fim,

            @Param("tipo")
            Integer tipo,

            @Param("status")
            StatusConta status,

            @Param("pesquisa")
            String pesquisa
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT c
            FROM Conta c
            WHERE c.codigo = :codigo
            """)
    Optional<Conta> buscarParaAtualizacao(
            @Param("codigo")
            Integer codigo
    );
}