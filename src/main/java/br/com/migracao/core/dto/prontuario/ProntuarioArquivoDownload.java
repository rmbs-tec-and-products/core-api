package br.com.migracao.core.dto.prontuario;

import org.springframework.core.io.Resource;

public record ProntuarioArquivoDownload(

        Resource resource,

        String contentType,

        String nomeArquivoOriginal,

        long tamanho

) {
}