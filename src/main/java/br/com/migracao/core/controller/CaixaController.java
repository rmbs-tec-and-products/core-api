package br.com.migracao.core.controller;

import br.com.migracao.core.dto.caixa.CaixaMovimentacaoRequest;
import br.com.migracao.core.dto.caixa.CaixaMovimentacaoResponse;
import br.com.migracao.core.dto.caixa.CaixaResponse;
import br.com.migracao.core.dto.caixa.CaixaResumoResponse;
import br.com.migracao.core.service.CaixaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/caixas")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaService caixaService;

    @PostMapping("/abrir")
    public ResponseEntity<CaixaResponse> abrir() {
        return ResponseEntity.ok(
                caixaService.abrir()
        );
    }

    @PostMapping("/{codigo}/fechar")
    public ResponseEntity<CaixaResponse> fechar(
            @PathVariable Integer codigo
    ) {
        return ResponseEntity.ok(
                caixaService.fechar(codigo)
        );
    }

    @GetMapping("/aberto")
    public ResponseEntity<CaixaResponse> buscarCaixaAberto() {
        return ResponseEntity.ok(
                caixaService.buscarCaixaAberto()
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<CaixaResponse> buscarPorCodigo(
            @PathVariable Integer codigo
    ) {
        return ResponseEntity.ok(
                caixaService.buscarPorCodigo(codigo)
        );
    }

    @GetMapping
    public ResponseEntity<List<CaixaResumoResponse>> listar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim
    ) {
        return ResponseEntity.ok(
                caixaService.listar(
                        inicio,
                        fim
                )
        );
    }

    @PostMapping("/{codigo}/movimentacoes")
    public ResponseEntity<CaixaMovimentacaoResponse> adicionarMovimentacao(
            @PathVariable Integer codigo,
            @Valid @RequestBody CaixaMovimentacaoRequest request
    ) {
        return ResponseEntity.ok(
                caixaService.adicionarMovimentacao(
                        codigo,
                        request
                )
        );
    }

    @PutMapping("/{codigo}/movimentacoes/{movimentacaoCodigo}")
    public ResponseEntity<CaixaMovimentacaoResponse> atualizarMovimentacao(
            @PathVariable Integer codigo,
            @PathVariable Integer movimentacaoCodigo,
            @Valid @RequestBody CaixaMovimentacaoRequest request
    ) {
        return ResponseEntity.ok(
                caixaService.atualizarMovimentacao(
                        codigo,
                        movimentacaoCodigo,
                        request
                )
        );
    }

    @DeleteMapping("/{codigo}/movimentacoes/{movimentacaoCodigo}")
    public ResponseEntity<Void> excluirMovimentacao(
            @PathVariable Integer codigo,
            @PathVariable Integer movimentacaoCodigo
    ) {
        caixaService.excluirMovimentacao(
                codigo,
                movimentacaoCodigo
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}