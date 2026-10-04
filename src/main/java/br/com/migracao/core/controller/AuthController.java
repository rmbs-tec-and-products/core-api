package br.com.migracao.core.controller;

import br.com.migracao.core.dto.usuario.UsuarioAutenticadoResponse;
import br.com.migracao.core.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioAutenticadoResponse> me(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                usuarioService.buscarAutenticado(
                        authentication.getName()
                )
        );
    }
}