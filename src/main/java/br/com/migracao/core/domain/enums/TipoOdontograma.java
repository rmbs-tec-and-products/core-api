package br.com.migracao.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TipoOdontograma {

    ODONTOGRAMA(1, "Odontograma"),
    ORCAMENTO(2, "Orçamento");

    private final Integer codigo;
    private final String descricao;

    public static TipoOdontograma fromCodigo(
            Integer codigo
    ) {
        if (codigo == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(tipo ->
                        tipo.codigo.equals(codigo)
                )
                .findFirst()
                .orElse(null);
    }
}