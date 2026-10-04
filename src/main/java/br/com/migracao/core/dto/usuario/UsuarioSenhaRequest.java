package br.com.migracao.core.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioSenhaRequest(

        @NotBlank(message = "Senha é obrigatória")
        @Size(
                min = 8,
                max = 100,
                message = "Senha deve possuir entre 8 e 100 caracteres"
        )
        String senha

) {
}