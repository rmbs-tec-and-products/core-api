package br.com.migracao.core.security;

import br.com.migracao.core.domain.entity.Usuario;
import br.com.migracao.core.domain.enums.PerfilUsuario;
import br.com.migracao.core.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioUserDetailsService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {

        Usuario usuario =
                usuarioRepository
                        .findByLoginIgnoreCase(
                                username.trim()
                        )
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuário não encontrado."
                                )
                        );

        String[] roles =
                usuario.getPerfil()
                        == PerfilUsuario.ADMIN
                        ? new String[]{
                        "USER",
                        "ADMIN"
                }
                        : new String[]{
                        "USER"
                };

        return User
                .withUsername(
                        usuario.getLogin()
                )
                .password(
                        usuario.getSenhaHash()
                )
                .roles(
                        roles
                )
                .disabled(
                        !usuario.isAtivo()
                )
                .build();
    }
}