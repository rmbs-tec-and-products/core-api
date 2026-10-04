package br.com.migracao.core.dto.odontograma;

import br.com.migracao.core.domain.enums.StatusOdontograma;
import jakarta.validation.constraints.NotNull;

public record OdontogramaStatusRequest(

        @NotNull(message = "Status é obrigatório")
        StatusOdontograma status

) {
}