package br.com.migracao.core.dto.procedimento;

import java.math.BigDecimal;
import java.util.List;

public record ProcedimentoResponse(

        Integer codigo,
        String nome,
        BigDecimal valor,
        List<ProcedimentoProdutoResponse> produtos

) {
}