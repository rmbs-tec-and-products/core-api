package br.com.migracao.core.dto.caixa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CaixaResumoResponse(

        Integer codigo,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        boolean aberto,

        BigDecimal totalEntrada,
        BigDecimal totalSaida,
        BigDecimal total

) {
}