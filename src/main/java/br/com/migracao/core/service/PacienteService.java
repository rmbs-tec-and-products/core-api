package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Paciente;
import br.com.migracao.core.dto.paciente.PacienteRequest;
import br.com.migracao.core.dto.paciente.PacienteResponse;
import br.com.migracao.core.dto.paciente.PacienteResumoResponse;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.PacienteMapper;
import br.com.migracao.core.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    @Transactional
    public PacienteResponse cadastrar(
            PacienteRequest request
    ) {

        Paciente paciente = pacienteMapper.toEntity(request);

        Paciente pacienteSalvo =
                pacienteRepository.save(paciente);

        return pacienteMapper.toResponse(pacienteSalvo);
    }

    @Transactional(readOnly = true)
    public List<PacienteResumoResponse> listar(
            String pesquisa
    ) {

        List<Paciente> pacientes;

        if (pesquisa == null || pesquisa.isBlank()) {

            pacientes = pacienteRepository.findAll();

        } else {

            pacientes = pacienteRepository.pesquisar(
                    pesquisa.trim()
            );
        }

        return pacientes.stream()
                .map(pacienteMapper::toResumoResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorCodigo(
            Integer codigo
    ) {

        Paciente paciente = buscarEntidade(codigo);

        return pacienteMapper.toResponse(paciente);
    }

    @Transactional
    public PacienteResponse atualizar(
            Integer codigo,
            PacienteRequest request
    ) {

        Paciente paciente = buscarEntidade(codigo);

        pacienteMapper.updateEntity(
                paciente,
                request
        );

        Paciente pacienteSalvo =
                pacienteRepository.save(paciente);

        return pacienteMapper.toResponse(pacienteSalvo);
    }

    @Transactional
    public void excluir(
            Integer codigo
    ) {

        Paciente paciente = buscarEntidade(codigo);

        pacienteRepository.delete(paciente);
    }

    private Paciente buscarEntidade(
            Integer codigo
    ) {

        return pacienteRepository.findById(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Paciente não encontrado. Código: " + codigo
                        )
                );
    }
}