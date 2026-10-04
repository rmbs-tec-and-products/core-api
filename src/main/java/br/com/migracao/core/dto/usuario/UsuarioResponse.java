package br.com.migracao.core.dto.usuario;

import br.com.migracao.core.domain.enums.PerfilUsuario;

import java.time.LocalDateTime;

public record UsuarioResponse(

        Integer codigo,
        String nome,
        String login,
        String email,
        PerfilUsuario perfil,
        boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm

) {
}