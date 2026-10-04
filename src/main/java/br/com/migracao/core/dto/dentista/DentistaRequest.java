package br.com.migracao.core.dto.dentista;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DentistaRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve possuir no máximo 100 caracteres")
        String nome

) {
}