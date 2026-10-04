package br.com.migracao.core.controller;

import br.com.migracao.core.dto.produto.ProdutoRequest;
import br.com.migracao.core.dto.produto.ProdutoResponse;
import br.com.migracao.core.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(
            @Valid @RequestBody ProdutoRequest request
    ) {
        ProdutoResponse response =
                produtoService.cadastrar(request);

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
    public ResponseEntity<List<ProdutoResponse>> listar(
            @RequestParam(required = false) String pesquisa
    ) {
        return ResponseEntity.ok(
                produtoService.listar(pesquisa)
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ProdutoResponse> buscarPorCodigo(
            @PathVariable Integer codigo
    ) {
        return ResponseEntity.ok(
                produtoService.buscarPorCodigo(codigo)
        );
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Integer codigo,
            @Valid @RequestBody ProdutoRequest request
    ) {
        return ResponseEntity.ok(
                produtoService.atualizar(
                        codigo,
                        request
                )
        );
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(
            @PathVariable Integer codigo
    ) {
        produtoService.excluir(codigo);

        return ResponseEntity
                .noContent()
                .build();
    }
}