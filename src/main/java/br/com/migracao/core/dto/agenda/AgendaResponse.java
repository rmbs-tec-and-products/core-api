package br.com.migracao.core.dto.agenda;

import java.time.LocalDateTime;

public record AgendaResponse(

        Integer codigo,
        LocalDateTime data,

        Integer pacienteCodigo,
        String pacienteNome,
        String pacienteCelular,

        Integer dentistaCodigo,
        String dentistaNome

) {
}