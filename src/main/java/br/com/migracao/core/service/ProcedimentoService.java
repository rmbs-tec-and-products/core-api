package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Procedimento;
import br.com.migracao.core.domain.entity.ProcedimentoProduto;
import br.com.migracao.core.domain.entity.ProcedimentoProdutoId;
import br.com.migracao.core.domain.entity.Produto;
import br.com.migracao.core.dto.procedimento.ProcedimentoProdutoRequest;
import br.com.migracao.core.dto.procedimento.ProcedimentoRequest;
import br.com.migracao.core.dto.procedimento.ProcedimentoResponse;
import br.com.migracao.core.exception.BusinessRuleException;
import br.com.migracao.core.exception.ResourceInUseException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.ProcedimentoMapper;
import br.com.migracao.core.repository.ProcedimentoRepository;
import br.com.migracao.core.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProcedimentoService {

    private final ProcedimentoRepository procedimentoRepository;
    private final ProdutoRepository produtoRepository;
    private final ProcedimentoMapper procedimentoMapper;

    @Transactional
    public ProcedimentoResponse cadastrar(
            ProcedimentoRequest request
    ) {
        validarProdutosDuplicados(request.produtos());

        Procedimento procedimento =
                procedimentoMapper.toEntity(request);

        procedimento =
                procedimentoRepository.saveAndFlush(procedimento);

        adicionarProdutos(
                procedimento,
                request.produtos()
        );

        Procedimento procedimentoSalvo =
                procedimentoRepository.save(procedimento);

        return procedimentoMapper.toResponse(
                procedimentoSalvo
        );
    }

    @Transactional(readOnly = true)
    public List<ProcedimentoResponse> listar(
            String pesquisa
    ) {
        List<Procedimento> procedimentos;

        if (pesquisa == null || pesquisa.isBlank()) {
            procedimentos =
                    procedimentoRepository
                            .findAllByOrderByNomeAsc();
        } else {
            procedimentos =
                    procedimentoRepository
                            .findByNomeContainingIgnoreCaseOrderByNomeAsc(
                                    pesquisa.trim()
                            );
        }

        return procedimentos
                .stream()
                .map(procedimentoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProcedimentoResponse buscarPorCodigo(
            Integer codigo
    ) {
        Procedimento procedimento =
                buscarEntidade(codigo);

        return procedimentoMapper.toResponse(
                procedimento
        );
    }

    @Transactional
    public ProcedimentoResponse atualizar(
            Integer codigo,
            ProcedimentoRequest request
    ) {
        validarProdutosDuplicados(request.produtos());

        Procedimento procedimento =
                buscarEntidade(codigo);

        procedimentoMapper.updateEntity(
                procedimento,
                request
        );

        procedimento.getProdutos().clear();

        procedimentoRepository.flush();

        adicionarProdutos(
                procedimento,
                request.produtos()
        );

        Procedimento procedimentoSalvo =
                procedimentoRepository.save(procedimento);

        return procedimentoMapper.toResponse(
                procedimentoSalvo
        );
    }

    @Transactional
    public void excluir(
            Integer codigo
    ) {
        Procedimento procedimento =
                buscarEntidade(codigo);

        try {
            procedimentoRepository.delete(procedimento);
            procedimentoRepository.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new ResourceInUseException(
                    "Procedimento não pode ser excluído porque está sendo utilizado."
            );
        }
    }

    private void adicionarProdutos(
            Procedimento procedimento,
            List<ProcedimentoProdutoRequest> produtosRequest
    ) {
        if (produtosRequest == null || produtosRequest.isEmpty()) {
            return;
        }

        for (ProcedimentoProdutoRequest produtoRequest : produtosRequest) {

            Produto produto =
                    produtoRepository
                            .findById(produtoRequest.produtoCodigo())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Produto não encontrado. Código: "
                                                    + produtoRequest.produtoCodigo()
                                    )
                            );

            ProcedimentoProdutoId id =
                    new ProcedimentoProdutoId(
                            procedimento.getCodigo(),
                            produto.getCodigo()
                    );

            ProcedimentoProduto procedimentoProduto =
                    ProcedimentoProduto.builder()
                            .id(id)
                            .procedimento(procedimento)
                            .produto(produto)
                            .quantidade(produtoRequest.quantidade())
                            .build();

            procedimento.getProdutos()
                    .add(procedimentoProduto);
        }
    }

    private void validarProdutosDuplicados(
            List<ProcedimentoProdutoRequest> produtos
    ) {
        if (produtos == null || produtos.isEmpty()) {
            return;
        }

        Set<Integer> codigos = new HashSet<>();

        for (ProcedimentoProdutoRequest produto : produtos) {
            if (!codigos.add(produto.produtoCodigo())) {
                throw new BusinessRuleException(
                        "O mesmo produto não pode ser adicionado mais de uma vez ao procedimento. Produto: "
                                + produto.produtoCodigo()
                );
            }
        }
    }

    private Procedimento buscarEntidade(
            Integer codigo
    ) {
        return procedimentoRepository.findById(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Procedimento não encontrado. Código: "
                                        + codigo
                        )
                );
    }
}