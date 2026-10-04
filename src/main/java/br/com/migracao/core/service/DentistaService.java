package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Dentista;
import br.com.migracao.core.dto.dentista.DentistaRequest;
import br.com.migracao.core.dto.dentista.DentistaResponse;
import br.com.migracao.core.exception.ResourceInUseException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.DentistaMapper;
import br.com.migracao.core.repository.DentistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DentistaService {

    private final DentistaRepository dentistaRepository;
    private final DentistaMapper dentistaMapper;

    @Transactional
    public DentistaResponse cadastrar(
            DentistaRequest request
    ) {
        Dentista dentista =
                dentistaMapper.toEntity(request);

        Dentista dentistaSalvo =
                dentistaRepository.save(dentista);

        return dentistaMapper.toResponse(
                dentistaSalvo
        );
    }

    @Transactional(readOnly = true)
    public List<DentistaResponse> listar(
            String pesquisa
    ) {
        List<Dentista> dentistas;

        if (pesquisa == null || pesquisa.isBlank()) {
            dentistas =
                    dentistaRepository.findAllByOrderByNomeAsc();
        } else {
            dentistas =
                    dentistaRepository
                            .findByNomeContainingIgnoreCaseOrderByNomeAsc(
                                    pesquisa.trim()
                            );
        }

        return dentistas
                .stream()
                .map(dentistaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DentistaResponse buscarPorCodigo(
            Integer codigo
    ) {
        return dentistaMapper.toResponse(
                buscarEntidade(codigo)
        );
    }

    @Transactional
    public DentistaResponse atualizar(
            Integer codigo,
            DentistaRequest request
    ) {
        Dentista dentista =
                buscarEntidade(codigo);

        dentistaMapper.updateEntity(
                dentista,
                request
        );

        Dentista dentistaSalvo =
                dentistaRepository.save(dentista);

        return dentistaMapper.toResponse(
                dentistaSalvo
        );
    }

    @Transactional
    public void excluir(
            Integer codigo
    ) {
        Dentista dentista =
                buscarEntidade(codigo);

        try {
            dentistaRepository.delete(dentista);
            dentistaRepository.flush();

        } catch (DataIntegrityViolationException exception) {
            throw new ResourceInUseException(
                    "Dentista não pode ser excluído porque possui agendamentos vinculados."
            );
        }
    }

    private Dentista buscarEntidade(
            Integer codigo
    ) {
        return dentistaRepository.findById(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Dentista não encontrado. Código: "
                                        + codigo
                        )
                );
    }
}