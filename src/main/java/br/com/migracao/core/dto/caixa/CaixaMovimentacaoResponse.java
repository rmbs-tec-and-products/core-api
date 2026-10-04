package br.com.migracao.core.dto.caixa;

import br.com.migracao.core.domain.enums.TipoMovimentacaoCaixa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CaixaMovimentacaoResponse(

        Integer codigo,
        TipoMovimentacaoCaixa tipo,
        String descricao,
        BigDecimal valor,
        String observacao,
        LocalDateTime data

) {
}