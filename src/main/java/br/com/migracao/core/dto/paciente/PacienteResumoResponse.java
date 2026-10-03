package br.com.migracao.core.dto.paciente;

public record PacienteResumoResponse(

        Integer codigo,
        String nome,
        String telefone,
        String celular
) {
}