package br.com.migracao.core.mapper;

import br.com.migracao.core.domain.entity.Conta;
import br.com.migracao.core.domain.enums.StatusConta;
import br.com.migracao.core.domain.enums.TipoConta;
import br.com.migracao.core.dto.conta.ContaResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ContaMapper {

    public ContaResponse toResponse(
            Conta conta
    ) {
        LocalDate vencimento =
                conta.getDataVencimento()
                        .toLocalDate();

        boolean vencida =
                conta.getStatus()
                        == StatusConta.PENDENTE
                        && vencimento.isBefore(
                        LocalDate.now()
                );

        return new ContaResponse(
                conta.getCodigo(),
                TipoConta.fromCodigo(
                        conta.getTipo()
                ),
                conta.getDescricao(),
                conta.getFormaPagamento(),
                conta.getValor(),
                vencimento,
                conta.getDataPagamento(),
                conta.getObservacao(),
                conta.getStatus(),
                conta.isRecorrente(),
                vencida
        );
    }
}