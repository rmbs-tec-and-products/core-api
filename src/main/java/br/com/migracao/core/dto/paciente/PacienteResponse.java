package br.com.migracao.core.dto.paciente;

import java.time.LocalDateTime;

public record PacienteResponse(

        Integer codigo,
        String nome,
        String telefone,
        String celular,
        String cpf,
        LocalDateTime dataNascimento,
        String sexo,
        String endereco,
        String numero,
        String cep,
        String cidade,
        String estado,
        String bairro,
        AnamneseResponse anamnese
) {
}