package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Agenda;
import br.com.migracao.core.domain.entity.Dentista;
import br.com.migracao.core.domain.entity.Paciente;
import br.com.migracao.core.dto.agenda.AgendaRequest;
import br.com.migracao.core.dto.agenda.AgendaResponse;
import br.com.migracao.core.exception.BusinessRuleException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.AgendaMapper;
import br.com.migracao.core.repository.AgendaRepository;
import br.com.migracao.core.repository.DentistaRepository;
import br.com.migracao.core.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final AgendaMapper agendaMapper;

    @Transactional
    public AgendaResponse cadastrar(
            AgendaRequest request
    ) {
        LocalDateTime data =
                normalizarData(request.data());

        Paciente paciente =
                buscarPaciente(request.pacienteCodigo());

        Dentista dentista =
                buscarDentista(request.dentistaCodigo());

        validarDataFutura(data);

        validarConflitos(
                null,
                data,
                paciente.getCodigo(),
                dentista.getCodigo()
        );

        Agenda agenda =
                agendaMapper.toEntity(
                        data,
                        paciente,
                        dentista
                );

        Agenda agendaSalva =
                agendaRepository.save(agenda);

        return agendaMapper.toResponse(
                agendaSalva
        );
    }

    @Transactional(readOnly = true)
    public List<AgendaResponse> listar(
            LocalDate data,
            Integer dentistaCodigo
    ) {
        LocalDate dia =
                data != null
                        ? data
                        : LocalDate.now();

        LocalDateTime inicio =
                dia.atStartOfDay();

        LocalDateTime fim =
                dia.plusDays(1).atStartOfDay();

        List<Agenda> agendamentos;

        if (dentistaCodigo == null) {
            agendamentos =
                    agendaRepository
                            .findByDataGreaterThanEqualAndDataLessThanOrderByDataAsc(
                                    inicio,
                                    fim
                            );
        } else {
            buscarDentista(dentistaCodigo);

            agendamentos =
                    agendaRepository
                            .findByDataGreaterThanEqualAndDataLessThanAndDentista_CodigoOrderByDataAsc(
                                    inicio,
                                    fim,
                                    dentistaCodigo
                            );
        }

        return agendamentos
                .stream()
                .map(agendaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AgendaResponse buscarPorCodigo(
            Integer codigo
    ) {
        return agendaMapper.toResponse(
                buscarEntidade(codigo)
        );
    }

    @Transactional
    public AgendaResponse atualizar(
            Integer codigo,
            AgendaRequest request
    ) {
        Agenda agenda =
                buscarEntidade(codigo);

        LocalDateTime data =
                normalizarData(request.data());

        Paciente paciente =
                buscarPaciente(request.pacienteCodigo());

        Dentista dentista =
                buscarDentista(request.dentistaCodigo());

        validarDataFutura(data);

        validarConflitos(
                codigo,
                data,
                paciente.getCodigo(),
                dentista.getCodigo()
        );

        agendaMapper.updateEntity(
                agenda,
                data,
                paciente,
                dentista
        );

        Agenda agendaSalva =
                agendaRepository.save(agenda);

        return agendaMapper.toResponse(
                agendaSalva
        );
    }

    @Transactional
    public void excluir(
            Integer codigo
    ) {
        Agenda agenda =
                buscarEntidade(codigo);

        agendaRepository.delete(agenda);
    }

    private void validarConflitos(
            Integer codigo,
            LocalDateTime data,
            Integer pacienteCodigo,
            Integer dentistaCodigo
    ) {
        boolean conflitoDentista;

        if (codigo == null) {
            conflitoDentista =
                    agendaRepository
                            .existsByDentista_CodigoAndData(
                                    dentistaCodigo,
                                    data
                            );
        } else {
            conflitoDentista =
                    agendaRepository
                            .existsByDentista_CodigoAndDataAndCodigoNot(
                                    dentistaCodigo,
                                    data,
                                    codigo
                            );
        }

        if (conflitoDentista) {
            throw new BusinessRuleException(
                    "Existe um agendamento para esse dentista nesse mesmo horário."
            );
        }

        LocalDateTime inicioDia =
                data.toLocalDate()
                        .atStartOfDay();

        LocalDateTime fimDia =
                data.toLocalDate()
                        .plusDays(1)
                        .atStartOfDay();

        boolean conflitoPaciente;

        if (codigo == null) {
            conflitoPaciente =
                    agendaRepository
                            .existsByPaciente_CodigoAndDataGreaterThanEqualAndDataLessThan(
                                    pacienteCodigo,
                                    inicioDia,
                                    fimDia
                            );
        } else {
            conflitoPaciente =
                    agendaRepository
                            .existsByPaciente_CodigoAndDataGreaterThanEqualAndDataLessThanAndCodigoNot(
                                    pacienteCodigo,
                                    inicioDia,
                                    fimDia,
                                    codigo
                            );
        }

        if (conflitoPaciente) {
            throw new BusinessRuleException(
                    "Este paciente já tem consulta agendada neste mesmo dia."
            );
        }
    }

    private void validarDataFutura(
            LocalDateTime data
    ) {
        if (data.isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException(
                    "Data do agendamento é menor que a data atual."
            );
        }
    }

    private LocalDateTime normalizarData(
            LocalDateTime data
    ) {
        return data
                .withSecond(0)
                .withNano(0);
    }

    private Agenda buscarEntidade(
            Integer codigo
    ) {
        return agendaRepository.findById(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Agendamento não encontrado. Código: "
                                        + codigo
                        )
                );
    }

    private Paciente buscarPaciente(
            Integer codigo
    ) {
        return pacienteRepository.findById(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Paciente não encontrado. Código: "
                                        + codigo
                        )
                );
    }

    private Dentista buscarDentista(
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