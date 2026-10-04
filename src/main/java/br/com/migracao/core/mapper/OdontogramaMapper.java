package br.com.migracao.core.mapper;

import br.com.migracao.core.domain.entity.Odontograma;
import br.com.migracao.core.domain.entity.OdontogramaDente;
import br.com.migracao.core.domain.entity.OdontogramaProcedimento;
import br.com.migracao.core.domain.enums.FaceDente;
import br.com.migracao.core.domain.enums.StatusOdontograma;
import br.com.migracao.core.domain.enums.StatusProcedimentoOdontograma;
import br.com.migracao.core.domain.enums.TipoOdontograma;
import br.com.migracao.core.dto.odontograma.OdontogramaProcedimentoResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaResumoResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class OdontogramaMapper {

    public OdontogramaResponse toResponse(
            Odontograma odontograma,
            List<OdontogramaDente> dentesExcluidos,
            List<OdontogramaProcedimento> procedimentos
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

        List<OdontogramaProcedimentoResponse> itens =
                procedimentos.stream()
                        .map(this::toProcedimentoResponse)
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

                dentes,
                itens
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

    public OdontogramaProcedimentoResponse toProcedimentoResponse(
            OdontogramaProcedimento item
    ) {
        StatusProcedimentoOdontograma status =
                StatusProcedimentoOdontograma.fromCodigo(
                        item.getStatus()
                );

        return new OdontogramaProcedimentoResponse(
                item.getCodigo(),

                item.getProcedimento().getCodigo(),
                item.getProcedimento().getNome(),

                item.getDente(),

                status,
                status != null
                        ? status.getDescricao()
                        : null,

                item.getValor(),

                item.getFace(),
                converterFaces(item.getFace()),

                item.getObservacao(),
                item.getData()
        );
    }

    private List<FaceDente> converterFaces(
            String face
    ) {
        if (face == null || face.isBlank()) {
            return List.of();
        }

        return Arrays.stream(
                        face.split("\\|")
                )
                .map(String::trim)
                .map(FaceDente::fromDescricao)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}