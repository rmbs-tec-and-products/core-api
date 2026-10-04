package br.com.migracao.core.controller;

import br.com.migracao.core.dto.agenda.AgendaRequest;
import br.com.migracao.core.dto.agenda.AgendaResponse;
import br.com.migracao.core.service.AgendaService;
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
@RequestMapping("/api/v1/agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @PostMapping
    public ResponseEntity<AgendaResponse> cadastrar(
            @Valid @RequestBody AgendaRequest request
    ) {
        AgendaResponse response =
                agendaService.cadastrar(request);

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
    public ResponseEntity<List<AgendaResponse>> listar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data,

            @RequestParam(required = false)
            Integer dentistaCodigo
    ) {
        return ResponseEntity.ok(
                agendaService.listar(
                        data,
                        dentistaCodigo
                )
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<AgendaResponse> buscarPorCodigo(
            @PathVariable Integer codigo
    ) {
        return ResponseEntity.ok(
                agendaService.buscarPorCodigo(codigo)
        );
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<AgendaResponse> atualizar(
            @PathVariable Integer codigo,
            @Valid @RequestBody AgendaRequest request
    ) {
        return ResponseEntity.ok(
                agendaService.atualizar(
                        codigo,
                        request
                )
        );
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(
            @PathVariable Integer codigo
    ) {
        agendaService.excluir(codigo);

        return ResponseEntity
                .noContent()
                .build();
    }
}