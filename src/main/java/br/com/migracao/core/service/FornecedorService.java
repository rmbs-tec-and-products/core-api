package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Fornecedor;
import br.com.migracao.core.dto.fornecedor.FornecedorRequest;
import br.com.migracao.core.dto.fornecedor.FornecedorResponse;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.FornecedorMapper;
import br.com.migracao.core.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FornecedorMapper fornecedorMapper;

    @Transactional
    public FornecedorResponse cadastrar(
            FornecedorRequest request
    ) {
        Fornecedor fornecedor =
                fornecedorMapper.toEntity(request);

        Fornecedor fornecedorSalvo =
                fornecedorRepository.save(fornecedor);

        return fornecedorMapper.toResponse(
                fornecedorSalvo
        );
    }

    @Transactional(readOnly = true)
    public List<FornecedorResponse> listar(
            String pesquisa
    ) {
        List<Fornecedor> fornecedores;

        if (pesquisa == null || pesquisa.isBlank()) {
            fornecedores =
                    fornecedorRepository.findAllByOrderByNomeAsc();
        } else {
            String termo = pesquisa.trim();

            fornecedores =
                    fornecedorRepository
                            .findByNomeContainingIgnoreCaseOrTelefoneContainingIgnoreCaseOrderByNomeAsc(
                                    termo,
                                    termo
                            );
        }

        return fornecedores
                .stream()
                .map(fornecedorMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FornecedorResponse buscarPorCodigo(
            Integer codigo
    ) {
        return fornecedorMapper.toResponse(
                buscarEntidade(codigo)
        );
    }

    @Transactional
    public FornecedorResponse atualizar(
            Integer codigo,
            FornecedorRequest request
    ) {
        Fornecedor fornecedor =
                buscarEntidade(codigo);

        fornecedorMapper.updateEntity(
                fornecedor,
                request
        );

        Fornecedor fornecedorSalvo =
                fornecedorRepository.save(fornecedor);

        return fornecedorMapper.toResponse(
                fornecedorSalvo
        );
    }

    @Transactional
    public void excluir(
            Integer codigo
    ) {
        Fornecedor fornecedor =
                buscarEntidade(codigo);

        fornecedorRepository.delete(fornecedor);
    }

    private Fornecedor buscarEntidade(
            Integer codigo
    ) {
        return fornecedorRepository.findById(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Fornecedor não encontrado. Código: "
                                        + codigo
                        )
                );
    }
}