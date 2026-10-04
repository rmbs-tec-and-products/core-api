package br.com.migracao.core.dto.prontuario;

import java.time.LocalDateTime;

public record ProntuarioArquivoResponse(

        Integer codigo,

        Integer pacienteCodigo,

        String titulo,

        String descricao,

        String nomeArquivoOriginal,

        String contentType,

        Long tamanho,

        LocalDateTime criadoEm,

        boolean imagem

) {
}