package br.com.migracao.core.dto.usuario;

import br.com.migracao.core.domain.enums.PerfilUsuario;

public record UsuarioAutenticadoResponse(

        Integer codigo,
        String nome,
        String login,
        PerfilUsuario perfil

) {
}