package br.com.migracao.core.dto.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PacienteRequest(

        @NotBlank(
                message = "Nome é obrigatório"
        )
        @Size(
                max = 300,
                message = "Nome deve possuir no máximo 300 caracteres"
        )
        String nome,

        @Size(
                max = 15,
                message = "Telefone deve possuir no máximo 15 caracteres"
        )
        String telefone,

        @NotBlank(
                message = "Celular é obrigatório"
        )
        @Size(
                max = 15,
                message = "Celular deve possuir no máximo 15 caracteres"
        )
        String celular,

        @Size(
                max = 18,
                message = "CPF/CNPJ deve possuir no máximo 18 caracteres"
        )
        String cpf,

        LocalDateTime dataNascimento,

        @Size(
                max = 10,
                message = "Sexo deve possuir no máximo 10 caracteres"
        )
        String sexo,

        @Size(
                max = 200,
                message = "Endereço deve possuir no máximo 200 caracteres"
        )
        String endereco,

        @Size(
                max = 20,
                message = "Número deve possuir no máximo 20 caracteres"
        )
        String numero,

        @Size(
                max = 10,
                message = "CEP deve possuir no máximo 10 caracteres"
        )
        String cep,

        @Size(
                max = 100,
                message = "Cidade deve possuir no máximo 100 caracteres"
        )
        String cidade,

        @Size(
                max = 2,
                message = "Estado deve possuir no máximo 2 caracteres"
        )
        String estado,

        @Size(
                max = 50,
                message = "Bairro deve possuir no máximo 50 caracteres"
        )
        String bairro,

        @Valid
        @NotNull(
                message = "Anamnese é obrigatória"
        )
        AnamneseRequest anamnese

) {
}