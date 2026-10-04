package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.Usuario;
import br.com.migracao.core.domain.enums.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByLoginIgnoreCase(
            String login
    );

    Optional<Usuario> findByEmailIgnoreCase(
            String email
    );

    boolean existsByLoginIgnoreCase(
            String login
    );

    boolean existsByLoginIgnoreCaseAndCodigoNot(
            String login,
            Integer codigo
    );

    boolean existsByEmailIgnoreCase(
            String email
    );

    boolean existsByEmailIgnoreCaseAndCodigoNot(
            String email,
            Integer codigo
    );

    List<Usuario> findAllByOrderByNomeAsc();

    List<Usuario> findByNomeContainingIgnoreCaseOrLoginContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByNomeAsc(
            String nome,
            String login,
            String email
    );

    long countByPerfilAndAtivoTrue(
            PerfilUsuario perfil
    );
}