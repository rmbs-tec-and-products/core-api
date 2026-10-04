package br.com.migracao.core.controller;

import br.com.migracao.core.dto.prontuario.ProntuarioArquivoDownload;
import br.com.migracao.core.dto.prontuario.ProntuarioArquivoResponse;
import br.com.migracao.core.service.ProntuarioArquivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(
        "/api/v1/pacientes/{pacienteCodigo}/prontuario"
)
@RequiredArgsConstructor
public class ProntuarioArquivoController {

    private final ProntuarioArquivoService
            prontuarioArquivoService;

    @GetMapping
    public ResponseEntity<List<ProntuarioArquivoResponse>>
    listar(
            @PathVariable
            Integer pacienteCodigo
    ) {
        return ResponseEntity.ok(
                prontuarioArquivoService.listar(
                        pacienteCodigo
                )
        );
    }

    @PostMapping(
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProntuarioArquivoResponse>
    enviar(
            @PathVariable
            Integer pacienteCodigo,

            @RequestParam
            String titulo,

            @RequestParam(required = false)
            String descricao,

            @RequestPart("arquivo")
            MultipartFile arquivo
    ) {
        ProntuarioArquivoResponse response =
                prontuarioArquivoService.enviar(
                        pacienteCodigo,
                        titulo,
                        descricao,
                        arquivo
                );

        URI location =
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path(
                                "/{codigo}"
                        )
                        .buildAndExpand(
                                response.codigo()
                        )
                        .toUri();

        return ResponseEntity
                .created(
                        location
                )
                .body(
                        response
                );
    }

    @GetMapping("/{arquivoCodigo}")
    public ResponseEntity<ProntuarioArquivoResponse>
    buscar(
            @PathVariable
            Integer pacienteCodigo,

            @PathVariable
            Integer arquivoCodigo
    ) {
        return ResponseEntity.ok(
                prontuarioArquivoService.buscar(
                        pacienteCodigo,
                        arquivoCodigo
                )
        );
    }

    @GetMapping("/{arquivoCodigo}/conteudo")
    public ResponseEntity<?> conteudo(
            @PathVariable
            Integer pacienteCodigo,

            @PathVariable
            Integer arquivoCodigo
    ) {
        ProntuarioArquivoDownload download =
                prontuarioArquivoService.baixar(
                        pacienteCodigo,
                        arquivoCodigo
                );

        MediaType mediaType;

        try {
            mediaType =
                    MediaType.parseMediaType(
                            download.contentType()
                    );

        } catch (Exception exception) {

            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;
        }

        ContentDisposition contentDisposition =
                ContentDisposition
                        .inline()
                        .filename(
                                download.nomeArquivoOriginal(),
                                StandardCharsets.UTF_8
                        )
                        .build();

        return ResponseEntity
                .ok()
                .contentType(
                        mediaType
                )
                .contentLength(
                        download.tamanho()
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        contentDisposition.toString()
                )
                .body(
                        download.resource()
                );
    }

    @DeleteMapping("/{arquivoCodigo}")
    public ResponseEntity<Void> excluir(
            @PathVariable
            Integer pacienteCodigo,

            @PathVariable
            Integer arquivoCodigo
    ) {
        prontuarioArquivoService.excluir(
                pacienteCodigo,
                arquivoCodigo
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}