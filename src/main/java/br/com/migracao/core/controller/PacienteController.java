package br.com.migracao.core.controller;

import br.com.migracao.core.dto.paciente.PacienteRequest;
import br.com.migracao.core.dto.paciente.PacienteResponse;
import br.com.migracao.core.dto.paciente.PacienteResumoResponse;
import br.com.migracao.core.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<PacienteResponse> cadastrar(
            @Valid @RequestBody PacienteRequest request
    ) {

        PacienteResponse response =
                pacienteService.cadastrar(request);

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
    public ResponseEntity<List<PacienteResumoResponse>> listar(
            @RequestParam(required = false) String pesquisa
    ) {

        return ResponseEntity.ok(
                pacienteService.listar(pesquisa)
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<PacienteResponse> buscarPorCodigo(
            @PathVariable Integer codigo
    ) {

        return ResponseEntity.ok(
                pacienteService.buscarPorCodigo(codigo)
        );
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<PacienteResponse> atualizar(
            @PathVariable Integer codigo,
            @Valid @RequestBody PacienteRequest request
    ) {

        return ResponseEntity.ok(
                pacienteService.atualizar(
                        codigo,
                        request
                )
        );
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(
            @PathVariable Integer codigo
    ) {

        pacienteService.excluir(codigo);

        return ResponseEntity
                .noContent()
                .build();
    }
}