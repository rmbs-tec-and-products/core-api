package br.com.migracao.core.dto.odontograma;

import br.com.migracao.core.domain.enums.FaceDente;
import br.com.migracao.core.domain.enums.StatusProcedimentoOdontograma;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OdontogramaProcedimentoResponse(

        Integer codigo,

        Integer procedimentoCodigo,
        String procedimentoNome,

        Integer dente,

        StatusProcedimentoOdontograma status,
        String statusDescricao,

        BigDecimal valor,

        String face,
        List<FaceDente> faces,

        String observacao,
        LocalDateTime data

) {
}