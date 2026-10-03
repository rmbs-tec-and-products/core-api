package br.com.migracao.core.dto.paciente;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnamneseRequest(

        boolean algumTratamento,

        boolean hospitalizado,

        @NotNull
        String motivo,

        boolean alergiaMedicamentoAnestesico,

        @NotNull
        String qualCirurgia,

        boolean transfusaoSangue,

        boolean bebidaAlcoolica,

        boolean problemaCardiaco,

        boolean febreReumatica,

        @NotNull
        String especialidade,

        @NotNull
        @Size(max = 100)
        String quandoHospitalizado,

        boolean tomaMedicamento,

        @NotNull
        String alergia,

        boolean sangraMuito,

        boolean fumante,

        Boolean desmaioTontura,

        boolean hiv,

        boolean tuberculose,

        @NotNull
        @Size(max = 100)
        String nomeMedico,

        @NotNull
        @Size(max = 100)
        String tempo,

        @NotNull
        @Size(max = 100)
        String medicamento,

        boolean cirurgia,

        boolean cirurgiaBucal,

        Integer cigarroDia,

        boolean respiratorio,

        boolean hepatite,

        boolean demoraCicatrizar,

        boolean depressao,

        boolean diabete,

        boolean hipertensao,

        boolean reumatismo,

        boolean renal,

        boolean problemaNervoso,

        boolean anemia,

        Boolean epilepsia
) {
}