package br.com.migracao.core.domain.enums;

import java.text.Normalizer;

public enum TipoMovimentacaoCaixa {

    ENTRADA,
    SAIDA;

    public static TipoMovimentacaoCaixa fromBanco(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String normalizado = Normalizer
                .normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toUpperCase();

        return TipoMovimentacaoCaixa.valueOf(normalizado);
    }
}