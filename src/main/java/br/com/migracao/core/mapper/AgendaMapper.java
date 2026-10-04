package br.com.migracao.core.mapper;

import br.com.migracao.core.domain.entity.Agenda;
import br.com.migracao.core.domain.entity.Dentista;
import br.com.migracao.core.domain.entity.Paciente;
import br.com.migracao.core.dto.agenda.AgendaResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AgendaMapper {

    public Agenda toEntity(
            LocalDateTime data,
            Paciente paciente,
            Dentista dentista
    ) {
        return Agenda.builder()
                .data(data)
                .paciente(paciente)
                .dentista(dentista)
                .build();
    }

    public AgendaResponse toResponse(
            Agenda agenda
    ) {
        return new AgendaResponse(
                agenda.getCodigo(),
                agenda.getData(),
                agenda.getPaciente().getCodigo(),
                agenda.getPaciente().getNome(),
                agenda.getPaciente().getCelular(),
                agenda.getDentista().getCodigo(),
                agenda.getDentista().getNome()
        );
    }

    public void updateEntity(
            Agenda agenda,
            LocalDateTime data,
            Paciente paciente,
            Dentista dentista
    ) {
        agenda.setData(data);
        agenda.setPaciente(paciente);
        agenda.setDentista(dentista);
    }
}