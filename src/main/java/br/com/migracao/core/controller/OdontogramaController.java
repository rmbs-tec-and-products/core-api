package br.com.migracao.core.controller;

import br.com.migracao.core.dto.odontograma.OdontogramaProcedimentoRequest;
import br.com.migracao.core.dto.odontograma.OdontogramaProcedimentoResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaRequest;
import br.com.migracao.core.dto.odontograma.OdontogramaResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaResumoResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaStatusRequest;
import br.com.migracao.core.service.OdontogramaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OdontogramaController {

    private final OdontogramaService odontogramaService;

    @PostMapping("/odontogramas")
    public ResponseEntity<OdontogramaResponse> cadastrar(
            @Valid
            @RequestBody
            OdontogramaRequest request
    ) {
        OdontogramaResponse response =
                odontogramaService.cadastrar(
                        request
                );

        URI location =
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path(
                                "/api/v1/odontogramas/{codigo}"
                        )
                        .buildAndExpand(
                                response.codigo()
                        )
                        .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping("/odontogramas/{codigo}")
    public ResponseEntity<OdontogramaResponse> buscarPorCodigo(
            @PathVariable
            Integer codigo
    ) {
        return ResponseEntity.ok(
                odontogramaService
                        .buscarPorCodigo(codigo)
        );
    }

    @GetMapping("/pacientes/{pacienteCodigo}/odontogramas")
    public ResponseEntity<List<OdontogramaResumoResponse>> listarPorPaciente(
            @PathVariable
            Integer pacienteCodigo
    ) {
        return ResponseEntity.ok(
                odontogramaService
                        .listarPorPaciente(
                                pacienteCodigo
                        )
        );
    }

    @PatchMapping("/odontogramas/{codigo}/status")
    public ResponseEntity<OdontogramaResponse> alterarStatus(
            @PathVariable
            Integer codigo,

            @Valid
            @RequestBody
            OdontogramaStatusRequest request
    ) {
        return ResponseEntity.ok(
                odontogramaService
                        .alterarStatus(
                                codigo,
                                request
                        )
        );
    }

    @PostMapping("/odontogramas/{codigo}/dentes/{dente}/excluir")
    public ResponseEntity<OdontogramaResponse> excluirDente(
            @PathVariable
            Integer codigo,

            @PathVariable
            Integer dente
    ) {
        return ResponseEntity.ok(
                odontogramaService
                        .excluirDente(
                                codigo,
                                dente
                        )
        );
    }

    @DeleteMapping("/odontogramas/{codigo}/dentes/{dente}/exclusao")
    public ResponseEntity<OdontogramaResponse> restaurarDente(
            @PathVariable
            Integer codigo,

            @PathVariable
            Integer dente
    ) {
        return ResponseEntity.ok(
                odontogramaService
                        .restaurarDente(
                                codigo,
                                dente
                        )
        );
    }

    @PostMapping("/odontogramas/{codigo}/procedimentos")
    public ResponseEntity<OdontogramaProcedimentoResponse> adicionarProcedimento(
            @PathVariable
            Integer codigo,

            @Valid
            @RequestBody
            OdontogramaProcedimentoRequest request
    ) {
        OdontogramaProcedimentoResponse response =
                odontogramaService
                        .adicionarProcedimento(
                                codigo,
                                request
                        );

        URI location =
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path(
                                "/api/v1/odontogramas/{codigo}/procedimentos/{itemCodigo}"
                        )
                        .buildAndExpand(
                                codigo,
                                response.codigo()
                        )
                        .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping("/odontogramas/{codigo}/procedimentos/{itemCodigo}")
    public ResponseEntity<OdontogramaProcedimentoResponse> alterarProcedimento(
            @PathVariable
            Integer codigo,

            @PathVariable
            Integer itemCodigo,

            @Valid
            @RequestBody
            OdontogramaProcedimentoRequest request
    ) {
        return ResponseEntity.ok(
                odontogramaService
                        .alterarProcedimento(
                                codigo,
                                itemCodigo,
                                request
                        )
        );
    }

    @DeleteMapping("/odontogramas/{codigo}/procedimentos/{itemCodigo}")
    public ResponseEntity<Void> excluirProcedimento(
            @PathVariable
            Integer codigo,

            @PathVariable
            Integer itemCodigo
    ) {
        odontogramaService
                .excluirProcedimento(
                        codigo,
                        itemCodigo
                );

        return ResponseEntity
                .noContent()
                .build();
    }
}