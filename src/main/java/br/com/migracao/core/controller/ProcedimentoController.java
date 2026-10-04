package br.com.migracao.core.controller;

import br.com.migracao.core.dto.procedimento.ProcedimentoRequest;
import br.com.migracao.core.dto.procedimento.ProcedimentoResponse;
import br.com.migracao.core.service.ProcedimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    @PostMapping
    public ResponseEntity<ProcedimentoResponse> cadastrar(
            @Valid @RequestBody ProcedimentoRequest request
    ) {
        ProcedimentoResponse response =
                procedimentoService.cadastrar(request);

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
    public ResponseEntity<List<ProcedimentoResponse>> listar(
            @RequestParam(required = false) String pesquisa
    ) {
        return ResponseEntity.ok(
                procedimentoService.listar(pesquisa)
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ProcedimentoResponse> buscarPorCodigo(
            @PathVariable Integer codigo
    ) {
        return ResponseEntity.ok(
                procedimentoService.buscarPorCodigo(codigo)
        );
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<ProcedimentoResponse> atualizar(
            @PathVariable Integer codigo,
            @Valid @RequestBody ProcedimentoRequest request
    ) {
        return ResponseEntity.ok(
                procedimentoService.atualizar(
                        codigo,
                        request
                )
        );
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(
            @PathVariable Integer codigo
    ) {
        procedimentoService.excluir(codigo);

        return ResponseEntity
                .noContent()
                .build();
    }
}