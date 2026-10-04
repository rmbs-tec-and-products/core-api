package br.com.migracao.core.mapper;

import br.com.migracao.core.domain.entity.Procedimento;
import br.com.migracao.core.domain.entity.ProcedimentoProduto;
import br.com.migracao.core.dto.procedimento.ProcedimentoProdutoResponse;
import br.com.migracao.core.dto.procedimento.ProcedimentoRequest;
import br.com.migracao.core.dto.procedimento.ProcedimentoResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcedimentoMapper {

    public Procedimento toEntity(
            ProcedimentoRequest request
    ) {
        return Procedimento.builder()
                .nome(request.nome())
                .valor(request.valor())
                .build();
    }

    public ProcedimentoResponse toResponse(
            Procedimento procedimento
    ) {
        List<ProcedimentoProdutoResponse> produtos =
                procedimento.getProdutos() == null
                        ? List.of()
                        : procedimento.getProdutos()
                        .stream()
                        .map(this::toProdutoResponse)
                        .toList();

        return new ProcedimentoResponse(
                procedimento.getCodigo(),
                procedimento.getNome(),
                procedimento.getValor(),
                produtos
        );
    }

    public void updateEntity(
            Procedimento procedimento,
            ProcedimentoRequest request
    ) {
        procedimento.setNome(request.nome());
        procedimento.setValor(request.valor());
    }

    private ProcedimentoProdutoResponse toProdutoResponse(
            ProcedimentoProduto procedimentoProduto
    ) {
        return new ProcedimentoProdutoResponse(
                procedimentoProduto.getProduto().getCodigo(),
                procedimentoProduto.getProduto().getNome(),
                procedimentoProduto.getQuantidade()
        );
    }
}