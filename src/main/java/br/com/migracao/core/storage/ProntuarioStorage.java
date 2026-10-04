package br.com.migracao.core.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ProntuarioStorage {

    String salvar(
            Integer pacienteCodigo,
            MultipartFile arquivo
    );

    Resource carregar(
            String storageKey
    );

    void excluir(
            String storageKey
    );
}