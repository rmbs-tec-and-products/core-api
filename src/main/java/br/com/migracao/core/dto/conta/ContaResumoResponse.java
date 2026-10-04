package br.com.migracao.core.dto.conta;

import java.math.BigDecimal;

public record ContaResumoResponse(

        BigDecimal totalPagarPendente,
        BigDecimal totalReceberPendente,

        BigDecimal totalPagarVencido,
        BigDecimal totalReceberVencido,

        BigDecimal totalPago,
        BigDecimal totalRecebido,

        long quantidadePendentes,
        long quantidadeVencidas

) {
}