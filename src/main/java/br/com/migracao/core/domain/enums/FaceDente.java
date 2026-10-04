package br.com.migracao.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum FaceDente {

    DISTAL("Distal"),
    LINGUAL_PALATAL("Lingual/palatal"),
    MESIAL("Mesial"),
    VESTIBULAR("Vestibular"),
    OCLUSAL("Oclusal"),
    RAIZ_MESIO_VESTIBULAR("Raiz mésio-vestibular"),
    RAIZ_DISTO_VESTIBULAR("Raiz disto-vestibular"),
    RAIZ_PALATINA("Raiz palatina");

    private final String descricao;

    public static FaceDente fromDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return null;
        }

        String normalizado = normalizar(descricao);

        if ("lingual".equals(normalizado)
                || "palatal".equals(normalizado)
                || "lingual/palatal".equals(normalizado)) {
            return LINGUAL_PALATAL;
        }

        return Arrays.stream(values())
                .filter(face ->
                        normalizar(face.descricao)
                                .equals(normalizado)
                )
                .findFirst()
                .orElse(null);
    }

    private static String normalizar(String valor) {
        return Normalizer
                .normalize(valor.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }
}