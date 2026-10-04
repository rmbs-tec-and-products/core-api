package br.com.migracao.core.storage;

import br.com.migracao.core.exception.BusinessRuleException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;

@Component
public class LocalProntuarioStorage
        implements ProntuarioStorage {

    private final Path diretorioBase;

    public LocalProntuarioStorage(
            @Value("${app.storage.prontuario.path}")
            String diretorio
    ) {
        this.diretorioBase =
                Path.of(
                                diretorio
                        )
                        .toAbsolutePath()
                        .normalize();

        criarDiretorioBase();
    }

    @Override
    public String salvar(
            Integer pacienteCodigo,
            MultipartFile arquivo
    ) {
        String extensao =
                obterExtensao(
                        arquivo.getOriginalFilename()
                );

        String storageKey =
                pacienteCodigo
                        + "/"
                        + UUID.randomUUID()
                        + extensao;

        Path destino =
                resolver(
                        storageKey
                );

        try {
            Files.createDirectories(
                    destino.getParent()
            );

            try (
                    InputStream inputStream =
                            arquivo.getInputStream()
            ) {
                Files.copy(
                        inputStream,
                        destino
                );
            }

            return storageKey;

        } catch (IOException exception) {

            throw new BusinessRuleException(
                    "Não foi possível armazenar o arquivo do prontuário."
            );
        }
    }

    @Override
    public Resource carregar(
            String storageKey
    ) {
        Path arquivo =
                resolver(
                        storageKey
                );

        if (!Files.exists(arquivo)
                || !Files.isRegularFile(
                arquivo
        )) {

            throw new ResourceNotFoundException(
                    "Arquivo do prontuário não encontrado no armazenamento."
            );
        }

        return new FileSystemResource(
                arquivo
        );
    }

    @Override
    public void excluir(
            String storageKey
    ) {
        Path arquivo =
                resolver(
                        storageKey
                );

        try {
            Files.deleteIfExists(
                    arquivo
            );

        } catch (IOException exception) {

            throw new BusinessRuleException(
                    "Não foi possível excluir o arquivo do prontuário."
            );
        }
    }

    private Path resolver(
            String storageKey
    ) {
        Path caminho =
                diretorioBase
                        .resolve(
                                storageKey
                        )
                        .normalize();

        if (!caminho.startsWith(
                diretorioBase
        )) {

            throw new BusinessRuleException(
                    "Caminho de armazenamento inválido."
            );
        }

        return caminho;
    }

    private void criarDiretorioBase() {
        try {
            Files.createDirectories(
                    diretorioBase
            );

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Não foi possível criar o diretório de armazenamento do prontuário.",
                    exception
            );
        }
    }

    private String obterExtensao(
            String nomeOriginal
    ) {
        if (nomeOriginal == null
                || nomeOriginal.isBlank()) {

            return "";
        }

        String nome =
                nomeOriginal
                        .replace(
                                '\\',
                                '/'
                        );

        int ultimaBarra =
                nome.lastIndexOf('/');

        if (ultimaBarra >= 0) {

            nome =
                    nome.substring(
                            ultimaBarra + 1
                    );
        }

        int ultimoPonto =
                nome.lastIndexOf('.');

        if (ultimoPonto < 0
                || ultimoPonto
                == nome.length() - 1) {

            return "";
        }

        String extensao =
                nome.substring(
                                ultimoPonto + 1
                        )
                        .toLowerCase(
                                Locale.ROOT
                        )
                        .replaceAll(
                                "[^a-z0-9]",
                                ""
                        );

        if (extensao.isBlank()
                || extensao.length() > 10) {

            return "";
        }

        return "." + extensao;
    }
}