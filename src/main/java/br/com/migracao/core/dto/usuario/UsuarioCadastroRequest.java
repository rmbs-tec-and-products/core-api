package br.com.migracao.core.dto.usuario;

import br.com.migracao.core.domain.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioCadastroRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(
                max = 100,
                message = "Nome deve possuir no máximo 100 caracteres"
        )
        String nome,

        @NotBlank(message = "Login é obrigatório")
        @Size(
                min = 3,
                max = 80,
                message = "Login deve possuir entre 3 e 80 caracteres"
        )
        String login,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "Informe um e-mail válido")
        @Size(
                max = 150,
                message = "E-mail deve possuir no máximo 150 caracteres"
        )
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(
                min = 8,
                max = 100,
                message = "Senha deve possuir entre 8 e 100 caracteres"
        )
        String senha,

        @NotNull(message = "Perfil é obrigatório")
        PerfilUsuario perfil

) {
}