package br.com.migracao.core.dto.conta;

import br.com.migracao.core.domain.enums.StatusConta;
import br.com.migracao.core.domain.enums.TipoConta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ContaResponse(

        Integer codigo,
        TipoConta tipo,
        String descricao,
        String formaPagamento,
        BigDecimal valor,
        LocalDate dataVencimento,
        LocalDateTime dataBaixa,
        String observacao,
        StatusConta status,
        boolean recorrente,
        boolean vencida

) {
}