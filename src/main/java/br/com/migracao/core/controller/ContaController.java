package br.com.migracao.core.controller;

import br.com.migracao.core.domain.enums.StatusConta;
import br.com.migracao.core.domain.enums.TipoConta;
import br.com.migracao.core.dto.conta.ContaRequest;
import br.com.migracao.core.dto.conta.ContaResponse;
import br.com.migracao.core.dto.conta.ContaResumoResponse;
import br.com.migracao.core.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @PostMapping
    public ResponseEntity<ContaResponse> cadastrar(
            @Valid
            @RequestBody
            ContaRequest request
    ) {
        ContaResponse response =
                contaService.cadastrar(
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
    public ResponseEntity<List<ContaResponse>> listar(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate inicio,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate fim,

            @RequestParam(required = false)
            TipoConta tipo,

            @RequestParam(required = false)
            StatusConta status,

            @RequestParam(required = false)
            String pesquisa
    ) {
        return ResponseEntity.ok(
                contaService.listar(
                        inicio,
                        fim,
                        tipo,
                        status,
                        pesquisa
                )
        );
    }

    @GetMapping("/resumo")
    public ResponseEntity<ContaResumoResponse> resumo(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate inicio,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate fim
    ) {
        return ResponseEntity.ok(
                contaService.resumo(
                        inicio,
                        fim
                )
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ContaResponse> buscarPorCodigo(
            @PathVariable
            Integer codigo
    ) {
        return ResponseEntity.ok(
                contaService.buscarPorCodigo(
                        codigo
                )
        );
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<ContaResponse> atualizar(
            @PathVariable
            Integer codigo,

            @Valid
            @RequestBody
            ContaRequest request
    ) {
        return ResponseEntity.ok(
                contaService.atualizar(
                        codigo,
                        request
                )
        );
    }

    @PostMapping("/{codigo}/baixar")
    public ResponseEntity<ContaResponse> baixar(
            @PathVariable
            Integer codigo
    ) {
        return ResponseEntity.ok(
                contaService.baixar(
                        codigo
                )
        );
    }

    @PostMapping("/{codigo}/cancelar")
    public ResponseEntity<ContaResponse> cancelar(
            @PathVariable
            Integer codigo
    ) {
        return ResponseEntity.ok(
                contaService.cancelar(
                        codigo
                )
        );
    }
}