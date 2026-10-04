package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Odontograma;
import br.com.migracao.core.domain.entity.OdontogramaDente;
import br.com.migracao.core.domain.entity.OdontogramaDenteId;
import br.com.migracao.core.domain.entity.Paciente;
import br.com.migracao.core.domain.enums.StatusOdontograma;
import br.com.migracao.core.domain.enums.TipoOdontograma;
import br.com.migracao.core.dto.odontograma.OdontogramaRequest;
import br.com.migracao.core.dto.odontograma.OdontogramaResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaResumoResponse;
import br.com.migracao.core.dto.odontograma.OdontogramaStatusRequest;
import br.com.migracao.core.exception.BusinessRuleException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.OdontogramaMapper;
import br.com.migracao.core.repository.OdontogramaDenteRepository;
import br.com.migracao.core.repository.OdontogramaRepository;
import br.com.migracao.core.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    private final OdontogramaRepository odontogramaRepository;
    private final OdontogramaDenteRepository odontogramaDenteRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontogramaMapper odontogramaMapper;

    @Transactional
    public OdontogramaResponse cadastrar(
            OdontogramaRequest request
    ) {
        Paciente paciente =
                buscarPaciente(
                        request.pacienteCodigo()
                );

        Odontograma odontograma =
                Odontograma.builder()
                        .tipo(
                                TipoOdontograma
                                        .ODONTOGRAMA
                                        .getCodigo()
                        )
                        .paciente(paciente)
                        .valor(BigDecimal.ZERO)
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
                        .odontopediatria(false)
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
        buscarPaciente(pacienteCodigo);

        return odontogramaRepository
                .findByPaciente_CodigoAndOdontopediatriaOrderByDataDesc(
                        pacienteCodigo,
                        false
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
                buscarEntidade(codigo)
        );
    }

    @Transactional
    public OdontogramaResponse alterarStatus(
            Integer codigo,
            OdontogramaStatusRequest request
    ) {
        Odontograma odontograma =
                buscarEntidade(codigo);

        odontograma.setStatus(
                request.status()
                        .getCodigo()
        );

        Odontograma odontogramaSalvo =
                odontogramaRepository.save(
                        odontograma
                );

        return montarResponse(
                odontogramaSalvo
        );
    }

    @Transactional
    public OdontogramaResponse excluirDente(
            Integer odontogramaCodigo,
            Integer dente
    ) {
        validarDentePermanente(dente);

        Odontograma odontograma =
                buscarEntidade(
                        odontogramaCodigo
                );

        validarOdontogramaAdulto(
                odontograma
        );

        OdontogramaDenteId id =
                new OdontogramaDenteId(
                        odontogramaCodigo,
                        dente
                );

        if (odontogramaDenteRepository
                .existsById(id)) {

            throw new BusinessRuleException(
                    "O dente "
                            + dente
                            + " já está excluído."
            );
        }

        OdontogramaDente exclusao =
                OdontogramaDente.builder()
                        .id(id)
                        .odontograma(odontograma)
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
        validarDentePermanente(dente);

        Odontograma odontograma =
                buscarEntidade(
                        odontogramaCodigo
                );

        validarOdontogramaAdulto(
                odontograma
        );

        OdontogramaDenteId id =
                new OdontogramaDenteId(
                        odontogramaCodigo,
                        dente
                );

        if (!odontogramaDenteRepository
                .existsById(id)) {

            throw new BusinessRuleException(
                    "O dente "
                            + dente
                            + " não está excluído."
            );
        }

        odontogramaDenteRepository
                .deleteById(id);

        odontogramaDenteRepository
                .flush();

        return montarResponse(
                odontograma
        );
    }

    private OdontogramaResponse montarResponse(
            Odontograma odontograma
    ) {
        List<OdontogramaDente> dentes =
                odontogramaDenteRepository
                        .findByOdontograma_CodigoOrderById_DenteAsc(
                                odontograma.getCodigo()
                        );

        return odontogramaMapper.toResponse(
                odontograma,
                dentes
        );
    }

    private Odontograma buscarEntidade(
            Integer codigo
    ) {
        return odontogramaRepository
                .findById(codigo)
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
                .findById(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Paciente não encontrado. Código: "
                                        + codigo
                        )
                );
    }

    private void validarOdontogramaAdulto(
            Odontograma odontograma
    ) {
        if (Boolean.TRUE.equals(
                odontograma.getOdontopediatria()
        )) {
            throw new BusinessRuleException(
                    "Esta operação pertence ao odontograma adulto."
            );
        }
    }

    private void validarDentePermanente(
            Integer dente
    ) {
        if (dente == null
                || !DENTES_PERMANENTES.contains(dente)) {

            throw new BusinessRuleException(
                    "Número de dente permanente inválido."
            );
        }
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
        return data.withNano(0);
    }
}