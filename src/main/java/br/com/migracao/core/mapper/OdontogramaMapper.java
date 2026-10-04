package br.com.migracao.core.mapper;

import br.com.migracao.core.domain.entity.Odontograma;
import br.com.migracao.core.domain.entity.OdontogramaDente;
import br.com.migracao.core.domain.enums.StatusOdontograma;
import br.com.migracao.core.domain.enums.TipoOdontograma;
import br.com.migracao.core.dto.odontograma.OdontogramaResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaResumoResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OdontogramaMapper {

    public OdontogramaResponse toResponse(
            Odontograma odontograma,
            List<OdontogramaDente> dentesExcluidos
    ) {
        TipoOdontograma tipo =
                TipoOdontograma.fromCodigo(
                        odontograma.getTipo()
                );

        StatusOdontograma status =
                StatusOdontograma.fromCodigo(
                        odontograma.getStatus()
                );

        List<Integer> dentes =
                dentesExcluidos.stream()
                        .map(item ->
                                item.getId().getDente()
                        )
                        .toList();

        return new OdontogramaResponse(
                odontograma.getCodigo(),

                tipo,
                tipo != null
                        ? tipo.getDescricao()
                        : null,

                odontograma.getPaciente() != null
                        ? odontograma.getPaciente().getCodigo()
                        : null,

                odontograma.getNome(),
                odontograma.getTelefone(),

                odontograma.getValor(),
                odontograma.getData(),

                status,
                status != null
                        ? status.getDescricao()
                        : null,

                odontograma.getOdontopediatria(),

                dentes
        );
    }

    public OdontogramaResumoResponse toResumoResponse(
            Odontograma odontograma
    ) {
        TipoOdontograma tipo =
                TipoOdontograma.fromCodigo(
                        odontograma.getTipo()
                );

        StatusOdontograma status =
                StatusOdontograma.fromCodigo(
                        odontograma.getStatus()
                );

        return new OdontogramaResumoResponse(
                odontograma.getCodigo(),

                tipo,
                tipo != null
                        ? tipo.getDescricao()
                        : null,

                odontograma.getValor(),
                odontograma.getData(),

                status,
                status != null
                        ? status.getDescricao()
                        : null
        );
    }
}