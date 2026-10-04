package br.com.migracao.core.dto.odontograma;

import br.com.migracao.core.domain.enums.StatusOdontograma;
import br.com.migracao.core.domain.enums.TipoOdontograma;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OdontogramaResumoResponse(

        Integer codigo,

        TipoOdontograma tipo,
        String tipoDescricao,

        BigDecimal valor,
        LocalDateTime data,

        StatusOdontograma status,
        String statusDescricao

) {
}