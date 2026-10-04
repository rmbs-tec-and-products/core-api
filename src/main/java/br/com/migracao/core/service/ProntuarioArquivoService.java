package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Paciente;
import br.com.migracao.core.domain.entity.ProntuarioArquivo;
import br.com.migracao.core.dto.prontuario.ProntuarioArquivoDownload;
import br.com.migracao.core.dto.prontuario.ProntuarioArquivoResponse;
import br.com.migracao.core.exception.BusinessRuleException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.repository.PacienteRepository;
import br.com.migracao.core.repository.ProntuarioArquivoRepository;
import br.com.migracao.core.storage.ProntuarioStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProntuarioArquivoService {

    private static final long TAMANHO_MAXIMO =
            20L
                    * 1024L
                    * 1024L;

    private static final Set<String>
            CONTENT_TYPES_PERMITIDOS =
            Set.of(
                    "application/pdf",
                    "image/jpeg",
                    "image/jpg",
                    "image/png",
                    "image/webp",
                    "image/heic",
                    "image/heif"
            );

    private final PacienteRepository pacienteRepository;

    private final ProntuarioArquivoRepository
            prontuarioArquivoRepository;

    private final ProntuarioStorage
            prontuarioStorage;

    @Transactional
    public ProntuarioArquivoResponse enviar(
            Integer pacienteCodigo,
            String titulo,
            String descricao,
            MultipartFile arquivo
    ) {
        Paciente paciente =
                buscarPaciente(
                        pacienteCodigo
                );

        validarTitulo(
                titulo
        );

        validarDescricao(
                descricao
        );

        validarArquivo(
                arquivo
        );

        String nomeOriginal =
                normalizarNomeOriginal(
                        arquivo.getOriginalFilename()
                );

        String contentType =
                arquivo.getContentType()
                        .trim()
                        .toLowerCase(
                                Locale.ROOT
                        );

        String storageKey =
                prontuarioStorage.salvar(
                        pacienteCodigo,
                        arquivo
                );

        try {
            ProntuarioArquivo prontuarioArquivo =
                    ProntuarioArquivo.builder()
                            .paciente(
                                    paciente
                            )
                            .titulo(
                                    titulo.trim()
                            )
                            .descricao(
                                    normalizarDescricao(
                                            descricao
                                    )
                            )
                            .nomeOriginal(
                                    nomeOriginal
                            )
                            .contentType(
                                    contentType
                            )
                            .tamanho(
                                    arquivo.getSize()
                            )
                            .storageKey(
                                    storageKey
                            )
                            .build();

            ProntuarioArquivo salvo =
                    prontuarioArquivoRepository
                            .save(
                                    prontuarioArquivo
                            );

            return toResponse(
                    salvo
            );

        } catch (RuntimeException exception) {

            try {
                prontuarioStorage.excluir(
                        storageKey
                );

            } catch (RuntimeException ignored) {
            }

            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public List<ProntuarioArquivoResponse> listar(
            Integer pacienteCodigo
    ) {
        validarPacienteExiste(
                pacienteCodigo
        );

        return prontuarioArquivoRepository
                .findByPaciente_CodigoOrderByCriadoEmDesc(
                        pacienteCodigo
                )
                .stream()
                .map(
                        this::toResponse
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public ProntuarioArquivoResponse buscar(
            Integer pacienteCodigo,
            Integer arquivoCodigo
    ) {
        return toResponse(
                buscarArquivo(
                        pacienteCodigo,
                        arquivoCodigo
                )
        );
    }

    @Transactional(readOnly = true)
    public ProntuarioArquivoDownload baixar(
            Integer pacienteCodigo,
            Integer arquivoCodigo
    ) {
        ProntuarioArquivo arquivo =
                buscarArquivo(
                        pacienteCodigo,
                        arquivoCodigo
                );

        Resource resource =
                prontuarioStorage.carregar(
                        arquivo.getStorageKey()
                );

        return new ProntuarioArquivoDownload(
                resource,
                arquivo.getContentType(),
                arquivo.getNomeOriginal(),
                arquivo.getTamanho()
        );
    }

    @Transactional
    public void excluir(
            Integer pacienteCodigo,
            Integer arquivoCodigo
    ) {
        ProntuarioArquivo arquivo =
                buscarArquivo(
                        pacienteCodigo,
                        arquivoCodigo
                );

        String storageKey =
                arquivo.getStorageKey();

        prontuarioArquivoRepository.delete(
                arquivo
        );

        prontuarioArquivoRepository.flush();

        prontuarioStorage.excluir(
                storageKey
        );
    }

    private Paciente buscarPaciente(
            Integer pacienteCodigo
    ) {
        return pacienteRepository
                .findById(
                        pacienteCodigo
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Paciente não encontrado. Código: "
                                        + pacienteCodigo
                        )
                );
    }

    private void validarPacienteExiste(
            Integer pacienteCodigo
    ) {
        if (!pacienteRepository.existsById(
                pacienteCodigo
        )) {

            throw new ResourceNotFoundException(
                    "Paciente não encontrado. Código: "
                            + pacienteCodigo
            );
        }
    }

    private ProntuarioArquivo buscarArquivo(
            Integer pacienteCodigo,
            Integer arquivoCodigo
    ) {
        return prontuarioArquivoRepository
                .findByCodigoAndPaciente_Codigo(
                        arquivoCodigo,
                        pacienteCodigo
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Arquivo do prontuário não encontrado."
                        )
                );
    }

    private void validarTitulo(
            String titulo
    ) {
        if (titulo == null
                || titulo.isBlank()) {

            throw new BusinessRuleException(
                    "Título do arquivo é obrigatório."
            );
        }

        if (titulo.trim()
                .length() > 150) {

            throw new BusinessRuleException(
                    "Título deve possuir no máximo 150 caracteres."
            );
        }
    }

    private void validarDescricao(
            String descricao
    ) {
        if (descricao == null
                || descricao.isBlank()) {

            return;
        }

        if (descricao.trim()
                .length() > 2000) {

            throw new BusinessRuleException(
                    "Descrição deve possuir no máximo 2000 caracteres."
            );
        }
    }

    private void validarArquivo(
            MultipartFile arquivo
    ) {
        if (arquivo == null
                || arquivo.isEmpty()) {

            throw new BusinessRuleException(
                    "Selecione um arquivo para o prontuário."
            );
        }

        if (arquivo.getSize()
                > TAMANHO_MAXIMO) {

            throw new BusinessRuleException(
                    "O arquivo deve possuir no máximo 20 MB."
            );
        }

        String contentType =
                arquivo.getContentType();

        if (contentType == null
                || contentType.isBlank()) {

            throw new BusinessRuleException(
                    "Não foi possível identificar o tipo do arquivo."
            );
        }

        String tipo =
                contentType
                        .trim()
                        .toLowerCase(
                                Locale.ROOT
                        );

        if (!CONTENT_TYPES_PERMITIDOS
                .contains(
                        tipo
                )) {

            throw new BusinessRuleException(
                    "Tipo de arquivo não permitido. Utilize PDF, JPG, PNG, WEBP ou HEIC."
            );
        }
    }

    private String normalizarNomeOriginal(
            String nomeOriginal
    ) {
        if (nomeOriginal == null
                || nomeOriginal.isBlank()) {

            return "arquivo";
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

        if (nome.length() > 255) {

            nome =
                    nome.substring(
                            nome.length() - 255
                    );
        }

        return nome;
    }

    private String normalizarDescricao(
            String descricao
    ) {
        if (descricao == null
                || descricao.isBlank()) {

            return null;
        }

        return descricao.trim();
    }

    private ProntuarioArquivoResponse toResponse(
            ProntuarioArquivo arquivo
    ) {
        boolean imagem =
                arquivo.getContentType()
                        != null
                        && arquivo
                        .getContentType()
                        .toLowerCase(
                                Locale.ROOT
                        )
                        .startsWith(
                                "image/"
                        );

        return new ProntuarioArquivoResponse(
                arquivo.getCodigo(),
                arquivo.getPaciente()
                        .getCodigo(),
                arquivo.getTitulo(),
                arquivo.getDescricao(),
                arquivo.getNomeOriginal(),
                arquivo.getContentType(),
                arquivo.getTamanho(),
                arquivo.getCriadoEm(),
                imagem
        );
    }
}