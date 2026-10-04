package br.com.migracao.core.mapper;

import br.com.migracao.core.domain.entity.Dentista;
import br.com.migracao.core.dto.dentista.DentistaRequest;
import br.com.migracao.core.dto.dentista.DentistaResponse;
import org.springframework.stereotype.Component;

@Component
public class DentistaMapper {

    public Dentista toEntity(
            DentistaRequest request
    ) {
        return Dentista.builder()
                .nome(request.nome())
                .build();
    }

    public DentistaResponse toResponse(
            Dentista dentista
    ) {
        return new DentistaResponse(
                dentista.getCodigo(),
                dentista.getNome()
        );
    }

    public void updateEntity(
            Dentista dentista,
            DentistaRequest request
    ) {
        dentista.setNome(request.nome());
    }
}