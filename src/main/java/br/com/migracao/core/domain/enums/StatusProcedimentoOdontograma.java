package br.com.migracao.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum StatusProcedimentoOdontograma {

    OBSERVACAO(1, "Observação"),
    A_REALIZAR(2, "À realizar"),
    INICIADO(3, "Iniciado"),
    CONCLUIDO(4, "Concluído");

    private final Integer codigo;
    private final String descricao;

    public static StatusProcedimentoOdontograma fromCodigo(Integer codigo) {
        if (codigo == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(status -> status.codigo.equals(codigo))
                .findFirst()
                .orElse(null);
    }
}