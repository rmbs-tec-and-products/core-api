package br.com.migracao.core.mapper;

import br.com.migracao.core.domain.entity.Caixa;
import br.com.migracao.core.domain.entity.CaixaHistorico;
import br.com.migracao.core.domain.enums.TipoMovimentacaoCaixa;
import br.com.migracao.core.dto.caixa.CaixaMovimentacaoResponse;
import br.com.migracao.core.dto.caixa.CaixaResponse;
import br.com.migracao.core.dto.caixa.CaixaResumoResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CaixaMapper {

    public CaixaMovimentacaoResponse toMovimentacaoResponse(
            CaixaHistorico historico
    ) {
        return new CaixaMovimentacaoResponse(
                historico.getCodigo(),
                TipoMovimentacaoCaixa.fromBanco(
                        historico.getTipo()
                ),
                historico.getDescricao(),
                historico.getValor(),
                historico.getObservacao(),
                historico.getData()
        );
    }

    public CaixaResponse toResponse(
            Caixa caixa,
            List<CaixaHistorico> historicos
    ) {
        BigDecimal totalEntrada =
                calcularTotal(
                        historicos,
                        TipoMovimentacaoCaixa.ENTRADA
                );

        BigDecimal totalSaida =
                calcularTotal(
                        historicos,
                        TipoMovimentacaoCaixa.SAIDA
                );

        List<CaixaMovimentacaoResponse> movimentacoes =
                historicos.stream()
                        .map(this::toMovimentacaoResponse)
                        .toList();

        return new CaixaResponse(
                caixa.getCodigo(),
                caixa.getDataAbertura(),
                caixa.getDataFechamento(),
                caixa.getDataFechamento() == null,
                totalEntrada,
                totalSaida,
                totalEntrada.subtract(totalSaida),
                movimentacoes
        );
    }

    public CaixaResumoResponse toResumoResponse(
            Caixa caixa,
            List<CaixaHistorico> historicos
    ) {
        BigDecimal totalEntrada =
                calcularTotal(
                        historicos,
                        TipoMovimentacaoCaixa.ENTRADA
                );

        BigDecimal totalSaida =
                calcularTotal(
                        historicos,
                        TipoMovimentacaoCaixa.SAIDA
                );

        return new CaixaResumoResponse(
                caixa.getCodigo(),
                caixa.getDataAbertura(),
                caixa.getDataFechamento(),
                caixa.getDataFechamento() == null,
                totalEntrada,
                totalSaida,
                totalEntrada.subtract(totalSaida)
        );
    }

    private BigDecimal calcularTotal(
            List<CaixaHistorico> historicos,
            TipoMovimentacaoCaixa tipo
    ) {
        return historicos.stream()
                .filter(historico ->
                        tipo.equals(
                                TipoMovimentacaoCaixa.fromBanco(
                                        historico.getTipo()
                                )
                        )
                )
                .map(CaixaHistorico::getValor)
                .filter(valor -> valor != null)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }
}