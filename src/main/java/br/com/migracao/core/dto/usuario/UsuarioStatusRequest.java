package br.com.migracao.core.dto.usuario;

import jakarta.validation.constraints.NotNull;

public record UsuarioStatusRequest(

        @NotNull(message = "Status é obrigatório")
        Boolean ativo

) {
}