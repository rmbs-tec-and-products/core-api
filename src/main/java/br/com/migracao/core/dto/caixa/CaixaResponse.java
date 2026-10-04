package br.com.migracao.core.dto.caixa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CaixaResponse(

        Integer codigo,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        boolean aberto,

        BigDecimal totalEntrada,
        BigDecimal totalSaida,
        BigDecimal total,

        List<CaixaMovimentacaoResponse> movimentacoes

) {
}