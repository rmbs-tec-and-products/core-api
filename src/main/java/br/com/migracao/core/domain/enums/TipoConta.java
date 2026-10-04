package br.com.migracao.core.domain.enums;

import br.com.migracao.core.exception.BusinessRuleException;

public enum TipoConta {

    PAGAR(1),
    RECEBER(2);

    private final Integer codigo;

    TipoConta(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public static TipoConta fromCodigo(
            Integer codigo
    ) {
        if (codigo == null) {
            return null;
        }

        for (TipoConta tipo : values()) {

            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }

        throw new BusinessRuleException(
                "Tipo de conta inválido: " + codigo
        );
    }
}