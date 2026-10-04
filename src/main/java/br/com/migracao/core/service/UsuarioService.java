package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Usuario;
import br.com.migracao.core.domain.enums.PerfilUsuario;
import br.com.migracao.core.dto.usuario.*;
import br.com.migracao.core.exception.BusinessRuleException;
import br.com.migracao.core.exception.ResourceInUseException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponse cadastrar(
            UsuarioCadastroRequest request
    ) {
        String login =
                normalizarLogin(
                        request.login()
                );

        String email =
                normalizarEmail(
                        request.email()
                );

        if (usuarioRepository
                .existsByLoginIgnoreCase(login)) {

            throw new ResourceInUseException(
                    "Já existe um usuário com o login informado."
            );
        }

        if (usuarioRepository
                .existsByEmailIgnoreCase(email)) {

            throw new ResourceInUseException(
                    "Já existe um usuário com o e-mail informado."
            );
        }

        Usuario usuario =
                new Usuario();

        usuario.setNome(
                request.nome().trim()
        );

        usuario.setLogin(
                login
        );

        usuario.setEmail(
                email
        );

        usuario.setSenhaHash(
                passwordEncoder.encode(
                        request.senha()
                )
        );

        usuario.setPerfil(
                request.perfil()
        );

        usuario.setAtivo(
                true
        );

        Usuario salvo =
                usuarioRepository.save(
                        usuario
                );

        return toResponse(
                salvo
        );
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar(
            String pesquisa
    ) {
        List<Usuario> usuarios;

        if (pesquisa == null
                || pesquisa.isBlank()) {

            usuarios =
                    usuarioRepository
                            .findAllByOrderByNomeAsc();

        } else {

            String termo =
                    pesquisa.trim();

            usuarios =
                    usuarioRepository
                            .findByNomeContainingIgnoreCaseOrLoginContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByNomeAsc(
                                    termo,
                                    termo,
                                    termo
                            );
        }

        return usuarios.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorCodigo(
            Integer codigo
    ) {
        return toResponse(
                buscarEntidade(codigo)
        );
    }

    @Transactional(readOnly = true)
    public UsuarioAutenticadoResponse buscarAutenticado(
            String login
    ) {
        Usuario usuario =
                usuarioRepository
                        .findByLoginIgnoreCase(
                                login
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuário não encontrado."
                                )
                        );

        return new UsuarioAutenticadoResponse(
                usuario.getCodigo(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getPerfil()
        );
    }

    @Transactional
    public UsuarioResponse atualizar(
            Integer codigo,
            UsuarioAtualizacaoRequest request
    ) {
        Usuario usuario =
                buscarEntidade(
                        codigo
                );

        String login =
                normalizarLogin(
                        request.login()
                );

        String email =
                normalizarEmail(
                        request.email()
                );

        if (usuarioRepository
                .existsByLoginIgnoreCaseAndCodigoNot(
                        login,
                        codigo
                )) {

            throw new ResourceInUseException(
                    "Já existe outro usuário com o login informado."
            );
        }

        if (usuarioRepository
                .existsByEmailIgnoreCaseAndCodigoNot(
                        email,
                        codigo
                )) {

            throw new ResourceInUseException(
                    "Já existe outro usuário com o e-mail informado."
            );
        }

        validarAlteracaoPerfilAdministrador(
                usuario,
                request.perfil()
        );

        usuario.setNome(
                request.nome().trim()
        );

        usuario.setLogin(
                login
        );

        usuario.setEmail(
                email
        );

        usuario.setPerfil(
                request.perfil()
        );

        Usuario salvo =
                usuarioRepository.save(
                        usuario
                );

        return toResponse(
                salvo
        );
    }

    @Transactional
    public UsuarioResponse alterarStatus(
            Integer codigo,
            UsuarioStatusRequest request,
            String loginAutenticado
    ) {
        Usuario usuario =
                buscarEntidade(
                        codigo
                );

        boolean novoStatus =
                request.ativo();

        if (!novoStatus
                && usuario.getLogin()
                .equalsIgnoreCase(
                        loginAutenticado
                )) {

            throw new BusinessRuleException(
                    "Você não pode desativar o próprio usuário."
            );
        }

        if (!novoStatus
                && usuario.isAtivo()
                && usuario.getPerfil()
                == PerfilUsuario.ADMIN) {

            validarExisteOutroAdministrador();
        }

        usuario.setAtivo(
                novoStatus
        );

        Usuario salvo =
                usuarioRepository.save(
                        usuario
                );

        return toResponse(
                salvo
        );
    }

    @Transactional
    public void redefinirSenha(
            Integer codigo,
            UsuarioSenhaRequest request
    ) {
        Usuario usuario =
                buscarEntidade(
                        codigo
                );

        usuario.setSenhaHash(
                passwordEncoder.encode(
                        request.senha()
                )
        );

        usuarioRepository.save(
                usuario
        );
    }

    private void validarAlteracaoPerfilAdministrador(
            Usuario usuario,
            PerfilUsuario novoPerfil
    ) {
        if (usuario.getPerfil()
                != PerfilUsuario.ADMIN) {

            return;
        }

        if (novoPerfil
                == PerfilUsuario.ADMIN) {

            return;
        }

        if (!usuario.isAtivo()) {
            return;
        }

        validarExisteOutroAdministrador();
    }

    private void validarExisteOutroAdministrador() {
        long administradoresAtivos =
                usuarioRepository
                        .countByPerfilAndAtivoTrue(
                                PerfilUsuario.ADMIN
                        );

        if (administradoresAtivos <= 1) {

            throw new BusinessRuleException(
                    "O sistema deve possuir pelo menos um administrador ativo."
            );
        }
    }

    private Usuario buscarEntidade(
            Integer codigo
    ) {
        return usuarioRepository
                .findById(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário não encontrado. Código: "
                                        + codigo
                        )
                );
    }

    private String normalizarLogin(
            String login
    ) {
        return login
                .trim()
                .toLowerCase(
                        Locale.ROOT
                );
    }

    private String normalizarEmail(
            String email
    ) {
        return email
                .trim()
                .toLowerCase(
                        Locale.ROOT
                );
    }

    private UsuarioResponse toResponse(
            Usuario usuario
    ) {
        return new UsuarioResponse(
                usuario.getCodigo(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.isAtivo(),
                usuario.getCriadoEm(),
                usuario.getAtualizadoEm()
        );
    }
}