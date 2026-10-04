package br.com.migracao.core.dto.odontograma;

import jakarta.validation.constraints.NotNull;

public record OdontogramaRequest(

        @NotNull(message = "Paciente é obrigatório")
        Integer pacienteCodigo

) {
}