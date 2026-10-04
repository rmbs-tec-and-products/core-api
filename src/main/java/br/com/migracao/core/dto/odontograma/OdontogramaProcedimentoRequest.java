package br.com.migracao.core.dto.odontograma;

import br.com.migracao.core.domain.enums.FaceDente;
import br.com.migracao.core.domain.enums.StatusProcedimentoOdontograma;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record OdontogramaProcedimentoRequest(

        @NotNull(message = "Procedimento é obrigatório")
        Integer procedimentoCodigo,

        @NotNull(message = "Dente é obrigatório")
        Integer dente,

        @NotNull(message = "Status é obrigatório")
        StatusProcedimentoOdontograma status,

        @DecimalMin(
                value = "0.00",
                inclusive = true,
                message = "Valor não pode ser negativo"
        )
        BigDecimal valor,

        @Size(
                max = 8,
                message = "Quantidade de faces inválida"
        )
        List<FaceDente> faces,

        String observacao

) {
}