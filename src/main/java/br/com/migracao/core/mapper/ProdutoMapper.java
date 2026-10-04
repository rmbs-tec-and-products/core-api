package br.com.migracao.core.mapper;

import br.com.migracao.core.domain.entity.Fornecedor;
import br.com.migracao.core.domain.entity.Produto;
import br.com.migracao.core.dto.produto.ProdutoRequest;
import br.com.migracao.core.dto.produto.ProdutoResponse;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(
            ProdutoRequest request,
            Fornecedor fornecedor
    ) {
        return Produto.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .quantidade(request.quantidade())
                .unidade(request.unidade())
                .embalagem(request.embalagem())
                .qtdEmbalagem(request.qtdEmbalagem())
                .valor(request.valor())
                .ultimoValor(request.ultimoValor())
                .fornecedor(fornecedor)
                .quantidadeMinima(request.quantidadeMinima())
                .build();
    }

    public ProdutoResponse toResponse(
            Produto produto
    ) {
        Integer fornecedorCodigo = null;
        String fornecedorNome = null;

        if (produto.getFornecedor() != null) {
            fornecedorCodigo = produto.getFornecedor().getCodigo();
            fornecedorNome = produto.getFornecedor().getNome();
        }

        return new ProdutoResponse(
                produto.getCodigo(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getQuantidade(),
                produto.getUnidade(),
                produto.getEmbalagem(),
                produto.getQtdEmbalagem(),
                produto.getValor(),
                produto.getUltimoValor(),
                fornecedorCodigo,
                fornecedorNome,
                produto.getQuantidadeMinima()
        );
    }

    public void updateEntity(
            Produto produto,
            ProdutoRequest request,
            Fornecedor fornecedor
    ) {
        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setQuantidade(request.quantidade());
        produto.setUnidade(request.unidade());
        produto.setEmbalagem(request.embalagem());
        produto.setQtdEmbalagem(request.qtdEmbalagem());
        produto.setValor(request.valor());
        produto.setUltimoValor(request.ultimoValor());
        produto.setFornecedor(fornecedor);
        produto.setQuantidadeMinima(request.quantidadeMinima());
    }
}