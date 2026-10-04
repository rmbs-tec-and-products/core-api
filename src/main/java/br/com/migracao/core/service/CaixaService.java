package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Caixa;
import br.com.migracao.core.domain.entity.CaixaHistorico;
import br.com.migracao.core.domain.enums.OrigemMovimentacaoCaixa;
import br.com.migracao.core.dto.caixa.CaixaMovimentacaoRequest;
import br.com.migracao.core.dto.caixa.CaixaMovimentacaoResponse;
import br.com.migracao.core.dto.caixa.CaixaResponse;
import br.com.migracao.core.dto.caixa.CaixaResumoResponse;
import br.com.migracao.core.exception.BusinessRuleException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.CaixaMapper;
import br.com.migracao.core.repository.CaixaHistoricoRepository;
import br.com.migracao.core.repository.CaixaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaixaService {

    private static final String DESCRICAO_PROCEDIMENTO =
            "FINALIZAÇÃO DE PROCEDIMENTO";

    private static final String DESCRICAO_CONTA_PAGAR =
            "PAGAMENTO DE CONTA";

    private static final String DESCRICAO_CONTA_RECEBER =
            "RECEBIMENTO DE CONTA";

    private final CaixaRepository caixaRepository;
    private final CaixaHistoricoRepository caixaHistoricoRepository;
    private final CaixaMapper caixaMapper;

    @Transactional
    public CaixaResponse abrir() {
        LocalDateTime agora =
                normalizarData(
                        LocalDateTime.now()
                );

        if (caixaRepository
                .findFirstByDataFechamentoIsNullOrderByDataAberturaDesc()
                .isPresent()) {

            throw new BusinessRuleException(
                    "Já existe um caixa aberto."
            );
        }

        LocalDateTime inicioDia =
                agora.toLocalDate()
                        .atStartOfDay();

        LocalDateTime fimDia =
                agora.toLocalDate()
                        .plusDays(1)
                        .atStartOfDay();

        boolean existeCaixaHoje =
                caixaRepository
                        .existsByDataAberturaGreaterThanEqualAndDataAberturaLessThan(
                                inicioDia,
                                fimDia
                        );

        if (existeCaixaHoje) {

            throw new BusinessRuleException(
                    "Não é possível abrir dois caixas no mesmo dia."
            );
        }

        Caixa caixa =
                new Caixa();

        caixa.setDataAbertura(
                agora
        );

        caixa.setDataFechamento(
                null
        );

        Caixa caixaSalvo =
                caixaRepository.save(
                        caixa
                );

        return caixaMapper.toResponse(
                caixaSalvo,
                List.of()
        );
    }

    @Transactional
    public CaixaResponse fechar(
            Integer codigo
    ) {
        Caixa caixa =
                buscarEntidade(
                        codigo
                );

        validarCaixaAberto(
                caixa
        );

        caixa.setDataFechamento(
                normalizarData(
                        LocalDateTime.now()
                )
        );

        Caixa caixaSalvo =
                caixaRepository.save(
                        caixa
                );

        return montarResponse(
                caixaSalvo
        );
    }

    @Transactional(readOnly = true)
    public CaixaResponse buscarCaixaAberto() {
        Caixa caixa =
                caixaRepository
                        .findFirstByDataFechamentoIsNullOrderByDataAberturaDesc()
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Nenhum caixa está aberto."
                                )
                        );

        return montarResponse(
                caixa
        );
    }

    @Transactional(readOnly = true)
    public CaixaResponse buscarPorCodigo(
            Integer codigo
    ) {
        return montarResponse(
                buscarEntidade(
                        codigo
                )
        );
    }

    @Transactional(readOnly = true)
    public List<CaixaResumoResponse> listar(
            LocalDate inicio,
            LocalDate fim
    ) {
        LocalDate dataInicio =
                inicio != null
                        ? inicio
                        : LocalDate.now();

        LocalDate dataFim =
                fim != null
                        ? fim
                        : dataInicio;

        if (dataFim.isBefore(
                dataInicio
        )) {

            throw new BusinessRuleException(
                    "Data final não pode ser menor que a data inicial."
            );
        }

        LocalDateTime inicioPeriodo =
                dataInicio.atStartOfDay();

        LocalDateTime fimPeriodo =
                dataFim
                        .plusDays(1)
                        .atStartOfDay();

        return caixaRepository
                .findByDataAberturaGreaterThanEqualAndDataAberturaLessThanOrderByDataAberturaDesc(
                        inicioPeriodo,
                        fimPeriodo
                )
                .stream()
                .map(caixa -> {

                    List<CaixaHistorico> historicos =
                            caixaHistoricoRepository
                                    .findByCaixa_CodigoOrderByDataDesc(
                                            caixa.getCodigo()
                                    );

                    return caixaMapper
                            .toResumoResponse(
                                    caixa,
                                    historicos
                            );
                })
                .toList();
    }

    @Transactional
    public CaixaMovimentacaoResponse adicionarMovimentacao(
            Integer caixaCodigo,
            CaixaMovimentacaoRequest request
    ) {
        Caixa caixa =
                buscarEntidade(
                        caixaCodigo
                );

        validarCaixaAberto(
                caixa
        );

        validarDescricaoNaoReservada(
                request.descricao()
        );

        CaixaHistorico historico =
                new CaixaHistorico();

        historico.setCaixa(
                caixa
        );

        historico.setTipo(
                request.tipo()
                        .name()
        );

        historico.setOrigem(
                OrigemMovimentacaoCaixa.MANUAL
        );

        historico.setOrigemCodigo(
                null
        );

        historico.setDescricao(
                request.descricao()
                        .trim()
        );

        historico.setValor(
                request.valor()
        );

        historico.setObservacao(
                normalizarObservacao(
                        request.observacao()
                )
        );

        historico.setData(
                normalizarData(
                        LocalDateTime.now()
                )
        );

        CaixaHistorico historicoSalvo =
                caixaHistoricoRepository.save(
                        historico
                );

        return caixaMapper
                .toMovimentacaoResponse(
                        historicoSalvo
                );
    }

    @Transactional
    public CaixaMovimentacaoResponse atualizarMovimentacao(
            Integer caixaCodigo,
            Integer movimentacaoCodigo,
            CaixaMovimentacaoRequest request
    ) {
        Caixa caixa =
                buscarEntidade(
                        caixaCodigo
                );

        validarCaixaAberto(
                caixa
        );

        CaixaHistorico historico =
                buscarMovimentacao(
                        caixaCodigo,
                        movimentacaoCodigo
                );

        validarMovimentacaoManual(
                historico
        );

        validarDescricaoNaoReservada(
                request.descricao()
        );

        historico.setTipo(
                request.tipo()
                        .name()
        );

        historico.setDescricao(
                request.descricao()
                        .trim()
        );

        historico.setValor(
                request.valor()
        );

        historico.setObservacao(
                normalizarObservacao(
                        request.observacao()
                )
        );

        CaixaHistorico historicoSalvo =
                caixaHistoricoRepository.save(
                        historico
                );

        return caixaMapper
                .toMovimentacaoResponse(
                        historicoSalvo
                );
    }

    @Transactional
    public void excluirMovimentacao(
            Integer caixaCodigo,
            Integer movimentacaoCodigo
    ) {
        Caixa caixa =
                buscarEntidade(
                        caixaCodigo
                );

        validarCaixaAberto(
                caixa
        );

        CaixaHistorico historico =
                buscarMovimentacao(
                        caixaCodigo,
                        movimentacaoCodigo
                );

        validarMovimentacaoManual(
                historico
        );

        caixaHistoricoRepository.delete(
                historico
        );
    }

    private CaixaResponse montarResponse(
            Caixa caixa
    ) {
        List<CaixaHistorico> historicos =
                caixaHistoricoRepository
                        .findByCaixa_CodigoOrderByDataDesc(
                                caixa.getCodigo()
                        );

        return caixaMapper.toResponse(
                caixa,
                historicos
        );
    }

    private Caixa buscarEntidade(
            Integer codigo
    ) {
        return caixaRepository
                .findById(
                        codigo
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Caixa não encontrado. Código: "
                                        + codigo
                        )
                );
    }

    private CaixaHistorico buscarMovimentacao(
            Integer caixaCodigo,
            Integer movimentacaoCodigo
    ) {
        CaixaHistorico historico =
                caixaHistoricoRepository
                        .findById(
                                movimentacaoCodigo
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Movimentação de caixa não encontrada. Código: "
                                                + movimentacaoCodigo
                                )
                        );

        if (historico.getCaixa()
                == null
                || !caixaCodigo.equals(
                historico.getCaixa()
                        .getCodigo()
        )) {

            throw new ResourceNotFoundException(
                    "Movimentação não pertence ao caixa informado."
            );
        }

        return historico;
    }

    private void validarCaixaAberto(
            Caixa caixa
    ) {
        if (caixa.getDataFechamento()
                != null) {

            throw new BusinessRuleException(
                    "O caixa já está fechado."
            );
        }
    }

    private void validarMovimentacaoManual(
            CaixaHistorico historico
    ) {
        if (historico.getOrigem()
                != null
                && historico.getOrigem()
                != OrigemMovimentacaoCaixa.MANUAL) {

            throw new BusinessRuleException(
                    "Movimentações geradas automaticamente não podem ser alteradas ou excluídas pelo caixa."
            );
        }

        if (historico.getDescricao()
                != null
                && DESCRICAO_PROCEDIMENTO
                .equalsIgnoreCase(
                        historico.getDescricao()
                                .trim()
                )) {

            throw new BusinessRuleException(
                    "Movimentações geradas automaticamente não podem ser alteradas ou excluídas pelo caixa."
            );
        }
    }

    private void validarDescricaoNaoReservada(
            String descricao
    ) {
        if (descricao == null) {
            return;
        }

        String valor =
                descricao.trim();

        if (DESCRICAO_PROCEDIMENTO
                .equalsIgnoreCase(valor)
                || DESCRICAO_CONTA_PAGAR
                .equalsIgnoreCase(valor)
                || DESCRICAO_CONTA_RECEBER
                .equalsIgnoreCase(valor)) {

            throw new BusinessRuleException(
                    "A descrição informada é reservada para movimentações automáticas."
            );
        }
    }

    private String normalizarObservacao(
            String observacao
    ) {
        if (observacao == null) {
            return "";
        }

        return observacao.trim();
    }

    private LocalDateTime normalizarData(
            LocalDateTime data
    ) {
        return data.withNano(0);
    }
}