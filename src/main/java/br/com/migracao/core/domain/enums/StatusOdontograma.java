package br.com.migracao.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum StatusOdontograma {

    EM_ANDAMENTO(1, "Em andamento"),
    CONCLUIDO(2, "Concluído"),
    CANCELADO(3, "Cancelado");

    private final Integer codigo;
    private final String descricao;

    public static StatusOdontograma fromCodigo(
            Integer codigo
    ) {
        if (codigo == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(status ->
                        status.codigo.equals(codigo)
                )
                .findFirst()
                .orElse(null);
    }
}