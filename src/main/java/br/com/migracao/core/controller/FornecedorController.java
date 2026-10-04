package br.com.migracao.core.controller;

import br.com.migracao.core.dto.fornecedor.FornecedorRequest;
import br.com.migracao.core.dto.fornecedor.FornecedorResponse;
import br.com.migracao.core.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @PostMapping
    public ResponseEntity<FornecedorResponse> cadastrar(
            @Valid @RequestBody FornecedorRequest request
    ) {
        FornecedorResponse response =
                fornecedorService.cadastrar(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{codigo}")
                .buildAndExpand(response.codigo())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponse>> listar(
            @RequestParam(required = false) String pesquisa
    ) {
        return ResponseEntity.ok(
                fornecedorService.listar(pesquisa)
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<FornecedorResponse> buscarPorCodigo(
            @PathVariable Integer codigo
    ) {
        return ResponseEntity.ok(
                fornecedorService.buscarPorCodigo(codigo)
        );
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<FornecedorResponse> atualizar(
            @PathVariable Integer codigo,
            @Valid @RequestBody FornecedorRequest request
    ) {
        return ResponseEntity.ok(
                fornecedorService.atualizar(
                        codigo,
                        request
                )
        );
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(
            @PathVariable Integer codigo
    ) {
        fornecedorService.excluir(codigo);

        return ResponseEntity
                .noContent()
                .build();
    }
}