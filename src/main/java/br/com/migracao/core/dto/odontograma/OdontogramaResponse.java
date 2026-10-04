package br.com.migracao.core.dto.odontograma;

import br.com.migracao.core.domain.enums.StatusOdontograma;
import br.com.migracao.core.domain.enums.TipoOdontograma;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OdontogramaResponse(

        Integer codigo,

        TipoOdontograma tipo,
        String tipoDescricao,

        Integer pacienteCodigo,
        String pacienteNome,
        String pacienteTelefone,

        BigDecimal valor,
        LocalDateTime data,

        StatusOdontograma status,
        String statusDescricao,

        Boolean odontopediatria,

        List<Integer> dentesExcluidos,

        List<OdontogramaProcedimentoResponse> procedimentos

) {
}