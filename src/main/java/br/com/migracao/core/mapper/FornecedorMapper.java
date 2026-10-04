package br.com.migracao.core.mapper;

import br.com.migracao.core.domain.entity.Fornecedor;
import br.com.migracao.core.dto.fornecedor.FornecedorRequest;
import br.com.migracao.core.dto.fornecedor.FornecedorResponse;
import org.springframework.stereotype.Component;

@Component
public class FornecedorMapper {

    public Fornecedor toEntity(
            FornecedorRequest request
    ) {
        return Fornecedor.builder()
                .nome(request.nome())
                .telefone(request.telefone())
                .build();
    }

    public FornecedorResponse toResponse(
            Fornecedor fornecedor
    ) {
        return new FornecedorResponse(
                fornecedor.getCodigo(),
                fornecedor.getNome(),
                fornecedor.getTelefone()
        );
    }

    public void updateEntity(
            Fornecedor fornecedor,
            FornecedorRequest request
    ) {
        fornecedor.setNome(request.nome());
        fornecedor.setTelefone(request.telefone());
    }
}