package br.com.migracao.core.dto.agenda;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendaRequest(

        @NotNull(message = "Data e horário são obrigatórios")
        LocalDateTime data,

        @NotNull(message = "Paciente é obrigatório")
        Integer pacienteCodigo,

        @NotNull(message = "Dentista é obrigatório")
        Integer dentistaCodigo

) {
}