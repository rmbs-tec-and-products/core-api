package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Fornecedor;
import br.com.migracao.core.domain.entity.Produto;
import br.com.migracao.core.dto.produto.ProdutoRequest;
import br.com.migracao.core.dto.produto.ProdutoResponse;
import br.com.migracao.core.exception.ResourceInUseException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.ProdutoMapper;
import br.com.migracao.core.repository.FornecedorRepository;
import br.com.migracao.core.repository.ProcedimentoProdutoRepository;
import br.com.migracao.core.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProcedimentoProdutoRepository procedimentoProdutoRepository;
    private final ProdutoMapper produtoMapper;

    @Transactional
    public ProdutoResponse cadastrar(
            ProdutoRequest request
    ) {
        Fornecedor fornecedor =
                buscarFornecedor(request.fornecedorCodigo());

        Produto produto =
                produtoMapper.toEntity(
                        request,
                        fornecedor
                );

        Produto produtoSalvo =
                produtoRepository.save(produto);

        return produtoMapper.toResponse(produtoSalvo);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar(
            String pesquisa
    ) {
        List<Produto> produtos;

        if (pesquisa == null || pesquisa.isBlank()) {
            produtos =
                    produtoRepository.findAllByOrderByNomeAsc();
        } else {
            produtos =
                    produtoRepository.pesquisar(
                            pesquisa.trim()
                    );
        }

        return produtos
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorCodigo(
            Integer codigo
    ) {
        Produto produto =
                buscarEntidade(codigo);

        return produtoMapper.toResponse(produto);
    }

    @Transactional
    public ProdutoResponse atualizar(
            Integer codigo,
            ProdutoRequest request
    ) {
        Produto produto =
                buscarEntidade(codigo);

        Fornecedor fornecedor =
                buscarFornecedor(request.fornecedorCodigo());

        produtoMapper.updateEntity(
                produto,
                request,
                fornecedor
        );

        Produto produtoSalvo =
                produtoRepository.save(produto);

        return produtoMapper.toResponse(produtoSalvo);
    }

    @Transactional
    public void excluir(
            Integer codigo
    ) {
        Produto produto =
                buscarEntidade(codigo);

        if (procedimentoProdutoRepository
                .existsByProduto_Codigo(codigo)) {

            throw new ResourceInUseException(
                    "Produto não pode ser excluído porque está associado a um procedimento."
            );
        }

        produtoRepository.delete(produto);
    }

    private Produto buscarEntidade(
            Integer codigo
    ) {
        return produtoRepository.findById(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Produto não encontrado. Código: "
                                        + codigo
                        )
                );
    }

    private Fornecedor buscarFornecedor(
            Integer fornecedorCodigo
    ) {
        if (fornecedorCodigo == null) {
            return null;
        }

        return fornecedorRepository
                .findById(fornecedorCodigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Fornecedor não encontrado. Código: "
                                        + fornecedorCodigo
                        )
                );
    }
}