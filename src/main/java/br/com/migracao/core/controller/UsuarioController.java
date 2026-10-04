package br.com.migracao.core.controller;

import br.com.migracao.core.dto.usuario.*;
import br.com.migracao.core.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid
            @RequestBody
            UsuarioCadastroRequest request
    ) {
        UsuarioResponse response =
                usuarioService.cadastrar(
                        request
                );

        URI location =
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{codigo}")
                        .buildAndExpand(
                                response.codigo()
                        )
                        .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar(
            @RequestParam(required = false)
            String pesquisa
    ) {
        return ResponseEntity.ok(
                usuarioService.listar(
                        pesquisa
                )
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<UsuarioResponse> buscarPorCodigo(
            @PathVariable
            Integer codigo
    ) {
        return ResponseEntity.ok(
                usuarioService.buscarPorCodigo(
                        codigo
                )
        );
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable
            Integer codigo,

            @Valid
            @RequestBody
            UsuarioAtualizacaoRequest request
    ) {
        return ResponseEntity.ok(
                usuarioService.atualizar(
                        codigo,
                        request
                )
        );
    }

    @PatchMapping("/{codigo}/status")
    public ResponseEntity<UsuarioResponse> alterarStatus(
            @PathVariable
            Integer codigo,

            @Valid
            @RequestBody
            UsuarioStatusRequest request,

            Authentication authentication
    ) {
        return ResponseEntity.ok(
                usuarioService.alterarStatus(
                        codigo,
                        request,
                        authentication.getName()
                )
        );
    }

    @PatchMapping("/{codigo}/senha")
    public ResponseEntity<Void> redefinirSenha(
            @PathVariable
            Integer codigo,

            @Valid
            @RequestBody
            UsuarioSenhaRequest request
    ) {
        usuarioService.redefinirSenha(
                codigo,
                request
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}