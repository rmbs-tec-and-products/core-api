package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Odontograma;
import br.com.migracao.core.domain.entity.OdontogramaDente;
import br.com.migracao.core.domain.entity.OdontogramaDenteId;
import br.com.migracao.core.domain.entity.OdontogramaProcedimento;
import br.com.migracao.core.domain.entity.Paciente;
import br.com.migracao.core.domain.entity.Procedimento;
import br.com.migracao.core.domain.enums.FaceDente;
import br.com.migracao.core.domain.enums.StatusOdontograma;
import br.com.migracao.core.domain.enums.StatusProcedimentoOdontograma;
import br.com.migracao.core.domain.enums.TipoOdontograma;
import br.com.migracao.core.dto.odontograma.OdontogramaProcedimentoRequest;
import br.com.migracao.core.dto.odontograma.OdontogramaProcedimentoResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaRequest;
import br.com.migracao.core.dto.odontograma.OdontogramaResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaResumoResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaStatusRequest;
import br.com.migracao.core.exception.BusinessRuleException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.OdontogramaMapper;
import br.com.migracao.core.repository.OdontogramaDenteRepository;
import br.com.migracao.core.repository.OdontogramaProcedimentoRepository;
import br.com.migracao.core.repository.OdontogramaRepository;
import br.com.migracao.core.repository.PacienteRepository;
import br.com.migracao.core.repository.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OdontogramaService {

    private static final Set<Integer> DENTES_PERMANENTES =
            Set.of(
                    11, 12, 13, 14, 15, 16, 17, 18,
                    21, 22, 23, 24, 25, 26, 27, 28,
                    31, 32, 33, 34, 35, 36, 37, 38,
                    41, 42, 43, 44, 45, 46, 47, 48
            );

    private static final Set<Integer> DENTES_DECIDUOS =
            Set.of(
                    51, 52, 53, 54, 55,
                    61, 62, 63, 64, 65,
                    71, 72, 73, 74, 75,
                    81, 82, 83, 84, 85
            );

    private final OdontogramaRepository odontogramaRepository;
    private final OdontogramaDenteRepository odontogramaDenteRepository;
    private final OdontogramaProcedimentoRepository odontogramaProcedimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final OdontogramaMapper odontogramaMapper;

    @Transactional
    public OdontogramaResponse cadastrar(
            OdontogramaRequest request
    ) {

        Paciente paciente =
                buscarPaciente(
                        request.pacienteCodigo()
                );

        boolean odontopediatria =
                Boolean.TRUE.equals(
                        request.odontopediatria()
                );

        Odontograma odontograma =
                Odontograma.builder()
                        .tipo(
                                TipoOdontograma
                                        .ODONTOGRAMA
                                        .getCodigo()
                        )
                        .paciente(
                                paciente
                        )
                        .valor(
                                BigDecimal.ZERO
                        )
                        .data(
                                normalizarData(
                                        LocalDateTime.now()
                                )
                        )
                        .status(
                                StatusOdontograma
                                        .EM_ANDAMENTO
                                        .getCodigo()
                        )
                        .nome(
                                paciente.getNome()
                        )
                        .telefone(
                                obterTelefonePaciente(
                                        paciente
                                )
                        )
                        .odontopediatria(
                                odontopediatria
                        )
                        .build();

        Odontograma odontogramaSalvo =
                odontogramaRepository.save(
                        odontograma
                );

        return montarResponse(
                odontogramaSalvo
        );
    }

    @Transactional(readOnly = true)
    public List<OdontogramaResumoResponse> listarPorPaciente(
            Integer pacienteCodigo
    ) {

        buscarPaciente(
                pacienteCodigo
        );

        return odontogramaRepository
                .findByPaciente_CodigoOrderByDataDesc(
                        pacienteCodigo
                )
                .stream()
                .map(
                        odontogramaMapper::toResumoResponse
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public OdontogramaResponse buscarPorCodigo(
            Integer codigo
    ) {

        return montarResponse(
                buscarEntidade(
                        codigo
                )
        );
    }

    @Transactional
    public OdontogramaResponse alterarStatus(
            Integer codigo,
            OdontogramaStatusRequest request
    ) {

        Odontograma odontograma =
                buscarEntidade(
                        codigo
                );

        odontograma.setStatus(
                request.status()
                        .getCodigo()
        );

        odontogramaRepository.save(
                odontograma
        );

        return montarResponse(
                odontograma
        );
    }

    @Transactional
    public OdontogramaResponse excluirDente(
            Integer odontogramaCodigo,
            Integer dente
    ) {

        Odontograma odontograma =
                buscarEntidade(
                        odontogramaCodigo
                );

        validarDenteDoOdontograma(
                odontograma,
                dente
        );

        OdontogramaDenteId id =
                new OdontogramaDenteId(
                        odontogramaCodigo,
                        dente
                );

        if (odontogramaDenteRepository
                .existsById(
                        id
                )) {

            throw new BusinessRuleException(
                    "O dente "
                            + dente
                            + " já está excluído."
            );
        }

        OdontogramaDente exclusao =
                OdontogramaDente.builder()
                        .id(
                                id
                        )
                        .odontograma(
                                odontograma
                        )
                        .build();

        odontogramaDenteRepository.save(
                exclusao
        );

        return montarResponse(
                odontograma
        );
    }

    @Transactional
    public OdontogramaResponse restaurarDente(
            Integer odontogramaCodigo,
            Integer dente
    ) {

        Odontograma odontograma =
                buscarEntidade(
                        odontogramaCodigo
                );

        validarDenteDoOdontograma(
                odontograma,
                dente
        );

        OdontogramaDenteId id =
                new OdontogramaDenteId(
                        odontogramaCodigo,
                        dente
                );

        if (!odontogramaDenteRepository
                .existsById(
                        id
                )) {

            throw new BusinessRuleException(
                    "O dente "
                            + dente
                            + " não está excluído."
            );
        }

        odontogramaDenteRepository
                .deleteById(
                        id
                );

        odontogramaDenteRepository
                .flush();

        return montarResponse(
                odontograma
        );
    }

    @Transactional
    public OdontogramaProcedimentoResponse adicionarProcedimento(
            Integer odontogramaCodigo,
            OdontogramaProcedimentoRequest request
    ) {

        Odontograma odontograma =
                buscarEntidade(
                        odontogramaCodigo
                );

        validarDenteDoOdontograma(
                odontograma,
                request.dente()
        );

        validarDenteDisponivel(
                odontogramaCodigo,
                request.dente()
        );

        validarStatusParaEdicao(
                request.status()
        );

        Procedimento procedimento =
                buscarProcedimento(
                        request.procedimentoCodigo()
                );

        BigDecimal valor =
                definirValor(
                        request.valor(),
                        procedimento
                );

        OdontogramaProcedimento item =
                OdontogramaProcedimento.builder()
                        .odontograma(
                                odontograma
                        )
                        .procedimento(
                                procedimento
                        )
                        .dente(
                                request.dente()
                        )
                        .status(
                                request.status()
                                        .getCodigo()
                        )
                        .valor(
                                valor
                        )
                        .face(
                                converterFacesParaBanco(
                                        request.faces()
                                )
                        )
                        .observacao(
                                normalizarObservacao(
                                        request.observacao()
                                )
                        )
                        .data(
                                normalizarData(
                                        LocalDateTime.now()
                                )
                        )
                        .build();

        OdontogramaProcedimento salvo =
                odontogramaProcedimentoRepository
                        .saveAndFlush(
                                item
                        );

        recalcularValorOdontograma(
                odontograma
        );

        return odontogramaMapper
                .toProcedimentoResponse(
                        salvo
                );
    }

    @Transactional
    public OdontogramaProcedimentoResponse alterarProcedimento(
            Integer odontogramaCodigo,
            Integer itemCodigo,
            OdontogramaProcedimentoRequest request
    ) {

        Odontograma odontograma =
                buscarEntidade(
                        odontogramaCodigo
                );

        OdontogramaProcedimento item =
                buscarItem(
                        odontogramaCodigo,
                        itemCodigo
                );

        validarItemNaoConcluido(
                item
        );

        validarDenteDoOdontograma(
                odontograma,
                request.dente()
        );

        validarDenteDisponivel(
                odontogramaCodigo,
                request.dente()
        );

        validarStatusParaEdicao(
                request.status()
        );

        Procedimento procedimento =
                buscarProcedimento(
                        request.procedimentoCodigo()
                );

        BigDecimal valor =
                definirValor(
                        request.valor(),
                        procedimento
                );

        item.setProcedimento(
                procedimento
        );

        item.setDente(
                request.dente()
        );

        item.setStatus(
                request.status()
                        .getCodigo()
        );

        item.setValor(
                valor
        );

        item.setFace(
                converterFacesParaBanco(
                        request.faces()
                )
        );

        item.setObservacao(
                normalizarObservacao(
                        request.observacao()
                )
        );

        OdontogramaProcedimento salvo =
                odontogramaProcedimentoRepository
                        .saveAndFlush(
                                item
                        );

        recalcularValorOdontograma(
                odontograma
        );

        return odontogramaMapper
                .toProcedimentoResponse(
                        salvo
                );
    }

    @Transactional
    public void excluirProcedimento(
            Integer odontogramaCodigo,
            Integer itemCodigo
    ) {

        Odontograma odontograma =
                buscarEntidade(
                        odontogramaCodigo
                );

        OdontogramaProcedimento item =
                buscarItem(
                        odontogramaCodigo,
                        itemCodigo
                );

        validarItemNaoConcluido(
                item
        );

        odontogramaProcedimentoRepository
                .delete(
                        item
                );

        odontogramaProcedimentoRepository
                .flush();

        recalcularValorOdontograma(
                odontograma
        );
    }

    private void recalcularValorOdontograma(
            Odontograma odontograma
    ) {

        List<OdontogramaProcedimento> itens =
                odontogramaProcedimentoRepository
                        .findByOdontograma_CodigoOrderByCodigoAsc(
                                odontograma.getCodigo()
                        );

        BigDecimal total =
                itens.stream()
                        .filter(
                                this::entraNoValorPendente
                        )
                        .map(
                                OdontogramaProcedimento::getValor
                        )
                        .filter(
                                valor ->
                                        valor != null
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        odontograma.setValor(
                total
        );

        odontogramaRepository.save(
                odontograma
        );
    }

    private boolean entraNoValorPendente(
            OdontogramaProcedimento item
    ) {

        return !StatusProcedimentoOdontograma
                .CONCLUIDO
                .getCodigo()
                .equals(
                        item.getStatus()
                );
    }

    private void validarStatusParaEdicao(
            StatusProcedimentoOdontograma status
    ) {

        if (StatusProcedimentoOdontograma
                .CONCLUIDO
                .equals(
                        status
                )) {

            throw new BusinessRuleException(
                    "Para concluir um procedimento utilize a operação de conclusão."
            );
        }
    }

    private void validarItemNaoConcluido(
            OdontogramaProcedimento item
    ) {

        if (StatusProcedimentoOdontograma
                .CONCLUIDO
                .getCodigo()
                .equals(
                        item.getStatus()
                )) {

            throw new BusinessRuleException(
                    "Procedimento concluído não pode ser alterado ou excluído."
            );
        }
    }

    private void validarDenteDoOdontograma(
            Odontograma odontograma,
            Integer dente
    ) {

        if (dente == null) {

            throw new BusinessRuleException(
                    "Número do dente é obrigatório."
            );
        }

        if (Boolean.TRUE.equals(
                odontograma.getOdontopediatria()
        )) {

            if (!DENTES_DECIDUOS.contains(
                    dente
            )) {

                throw new BusinessRuleException(
                        "Número de dente decíduo inválido para odontograma pediátrico."
                );
            }

            return;
        }

        if (!DENTES_PERMANENTES.contains(
                dente
        )) {

            throw new BusinessRuleException(
                    "Número de dente permanente inválido para odontograma adulto."
            );
        }
    }

    private void validarDenteDisponivel(
            Integer odontogramaCodigo,
            Integer dente
    ) {

        OdontogramaDenteId id =
                new OdontogramaDenteId(
                        odontogramaCodigo,
                        dente
                );

        if (odontogramaDenteRepository
                .existsById(
                        id
                )) {

            throw new BusinessRuleException(
                    "O dente "
                            + dente
                            + " está excluído do odontograma."
            );
        }
    }

    private BigDecimal definirValor(
            BigDecimal valorInformado,
            Procedimento procedimento
    ) {

        if (valorInformado != null) {

            return valorInformado;
        }

        if (procedimento.getValor() != null) {

            return procedimento.getValor();
        }

        return BigDecimal.ZERO;
    }

    private String converterFacesParaBanco(
            List<FaceDente> faces
    ) {

        if (faces == null
                || faces.isEmpty()) {

            return null;
        }

        LinkedHashSet<FaceDente> facesUnicas =
                new LinkedHashSet<>(
                        faces
                );

        return facesUnicas.stream()
                .map(
                        FaceDente::getDescricao
                )
                .collect(
                        Collectors.joining("|")
                );
    }

    private String normalizarObservacao(
            String observacao
    ) {

        if (observacao == null) {

            return null;
        }

        String valor =
                observacao.trim();

        return valor.isBlank()
                ? null
                : valor;
    }

    private OdontogramaResponse montarResponse(
            Odontograma odontograma
    ) {

        List<OdontogramaDente> dentes =
                odontogramaDenteRepository
                        .findByOdontograma_CodigoOrderById_DenteAsc(
                                odontograma.getCodigo()
                        );

        List<OdontogramaProcedimento> procedimentos =
                odontogramaProcedimentoRepository
                        .findByOdontograma_CodigoOrderByCodigoAsc(
                                odontograma.getCodigo()
                        );

        return odontogramaMapper.toResponse(
                odontograma,
                dentes,
                procedimentos
        );
    }

    private Odontograma buscarEntidade(
            Integer codigo
    ) {

        return odontogramaRepository
                .findById(
                        codigo
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Odontograma não encontrado. Código: "
                                        + codigo
                        )
                );
    }

    private Paciente buscarPaciente(
            Integer codigo
    ) {

        return pacienteRepository
                .findById(
                        codigo
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Paciente não encontrado. Código: "
                                        + codigo
                        )
                );
    }

    private Procedimento buscarProcedimento(
            Integer codigo
    ) {

        return procedimentoRepository
                .findById(
                        codigo
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Procedimento não encontrado. Código: "
                                        + codigo
                        )
                );
    }

    private OdontogramaProcedimento buscarItem(
            Integer odontogramaCodigo,
            Integer itemCodigo
    ) {

        return odontogramaProcedimentoRepository
                .findByCodigoAndOdontograma_Codigo(
                        itemCodigo,
                        odontogramaCodigo
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Procedimento do odontograma não encontrado. Código: "
                                        + itemCodigo
                        )
                );
    }

    private String obterTelefonePaciente(
            Paciente paciente
    ) {

        if (paciente.getTelefone() != null
                && !paciente.getTelefone().isBlank()) {

            return paciente.getTelefone();
        }

        if (paciente.getCelular() != null
                && !paciente.getCelular().isBlank()) {

            return paciente.getCelular();
        }

        return null;
    }

    private LocalDateTime normalizarData(
            LocalDateTime data
    ) {

        return data.withNano(
                0
        );
    }
}