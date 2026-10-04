package br.com.migracao.core.controller;

import br.com.migracao.core.dto.dentista.DentistaRequest;
import br.com.migracao.core.dto.dentista.DentistaResponse;
import br.com.migracao.core.service.DentistaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dentistas")
@RequiredArgsConstructor
public class DentistaController {

    private final DentistaService dentistaService;

    @PostMapping
    public ResponseEntity<DentistaResponse> cadastrar(
            @Valid @RequestBody DentistaRequest request
    ) {
        DentistaResponse response =
                dentistaService.cadastrar(request);

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
    public ResponseEntity<List<DentistaResponse>> listar(
            @RequestParam(required = false) String pesquisa
    ) {
        return ResponseEntity.ok(
                dentistaService.listar(pesquisa)
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<DentistaResponse> buscarPorCodigo(
            @PathVariable Integer codigo
    ) {
        return ResponseEntity.ok(
                dentistaService.buscarPorCodigo(codigo)
        );
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<DentistaResponse> atualizar(
            @PathVariable Integer codigo,
            @Valid @RequestBody DentistaRequest request
    ) {
        return ResponseEntity.ok(
                dentistaService.atualizar(
                        codigo,
                        request
                )
        );
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(
            @PathVariable Integer codigo
    ) {
        dentistaService.excluir(codigo);

        return ResponseEntity
                .noContent()
                .build();
    }
}