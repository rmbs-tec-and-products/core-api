package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Caixa;
import br.com.migracao.core.domain.entity.CaixaHistorico;
import br.com.migracao.core.domain.entity.Conta;
import br.com.migracao.core.domain.enums.OrigemMovimentacaoCaixa;
import br.com.migracao.core.domain.enums.StatusConta;
import br.com.migracao.core.domain.enums.TipoConta;
import br.com.migracao.core.dto.conta.ContaRequest;
import br.com.migracao.core.dto.conta.ContaResponse;
import br.com.migracao.core.dto.conta.ContaResumoResponse;
import br.com.migracao.core.exception.BusinessRuleException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.ContaMapper;
import br.com.migracao.core.repository.CaixaHistoricoRepository;
import br.com.migracao.core.repository.CaixaRepository;
import br.com.migracao.core.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final CaixaRepository caixaRepository;
    private final CaixaHistoricoRepository caixaHistoricoRepository;
    private final ContaMapper contaMapper;

    @Transactional
    public ContaResponse cadastrar(
            ContaRequest request
    ) {
        validarRecorrencia(
                request.tipo(),
                request.recorrente()
        );

        Conta conta =
                new Conta();

        preencherConta(
                conta,
                request
        );

        conta.setStatus(
                StatusConta.PENDENTE
        );

        conta.setDataPagamento(
                null
        );

        Conta salva =
                contaRepository.save(
                        conta
                );

        return contaMapper.toResponse(
                salva
        );
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> listar(
            LocalDate inicio,
            LocalDate fim,
            TipoConta tipo,
            StatusConta status,
            String pesquisa
    ) {
        LocalDate dataInicio =
                inicio != null
                        ? inicio
                        : LocalDate.now()
                        .withDayOfMonth(1);

        LocalDate dataFim =
                fim != null
                        ? fim
                        : dataInicio
                        .plusMonths(1)
                        .minusDays(1);

        validarPeriodo(
                dataInicio,
                dataFim
        );

        String termo =
                pesquisa == null
                        || pesquisa.isBlank()
                        ? null
                        : pesquisa.trim();

        Integer codigoTipo =
                tipo != null
                        ? tipo.getCodigo()
                        : null;

        return contaRepository
                .pesquisar(
                        dataInicio.atStartOfDay(),
                        dataFim
                                .plusDays(1)
                                .atStartOfDay(),
                        codigoTipo,
                        status,
                        termo
                )
                .stream()
                .map(
                        contaMapper::toResponse
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public ContaResumoResponse resumo(
            LocalDate inicio,
            LocalDate fim
    ) {
        List<ContaResponse> contas =
                listar(
                        inicio,
                        fim,
                        null,
                        null,
                        null
                );

        BigDecimal pagarPendente =
                somar(
                        contas,
                        TipoConta.PAGAR,
                        StatusConta.PENDENTE,
                        false
                );

        BigDecimal receberPendente =
                somar(
                        contas,
                        TipoConta.RECEBER,
                        StatusConta.PENDENTE,
                        false
                );

        BigDecimal pagarVencido =
                somar(
                        contas,
                        TipoConta.PAGAR,
                        StatusConta.PENDENTE,
                        true
                );

        BigDecimal receberVencido =
                somar(
                        contas,
                        TipoConta.RECEBER,
                        StatusConta.PENDENTE,
                        true
                );

        BigDecimal totalPago =
                somar(
                        contas,
                        TipoConta.PAGAR,
                        StatusConta.BAIXADA,
                        false
                );

        BigDecimal totalRecebido =
                somar(
                        contas,
                        TipoConta.RECEBER,
                        StatusConta.BAIXADA,
                        false
                );

        long pendentes =
                contas.stream()
                        .filter(conta ->
                                conta.status()
                                        == StatusConta.PENDENTE
                        )
                        .count();

        long vencidas =
                contas.stream()
                        .filter(
                                ContaResponse::vencida
                        )
                        .count();

        return new ContaResumoResponse(
                pagarPendente,
                receberPendente,
                pagarVencido,
                receberVencido,
                totalPago,
                totalRecebido,
                pendentes,
                vencidas
        );
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarPorCodigo(
            Integer codigo
    ) {
        return contaMapper.toResponse(
                buscarEntidade(
                        codigo
                )
        );
    }

    @Transactional
    public ContaResponse atualizar(
            Integer codigo,
            ContaRequest request
    ) {
        Conta conta =
                buscarParaAtualizacao(
                        codigo
                );

        validarPendente(
                conta
        );

        validarRecorrencia(
                request.tipo(),
                request.recorrente()
        );

        preencherConta(
                conta,
                request
        );

        Conta salva =
                contaRepository.save(
                        conta
                );

        return contaMapper.toResponse(
                salva
        );
    }

    @Transactional
    public ContaResponse baixar(
            Integer codigo
    ) {
        Conta conta =
                buscarParaAtualizacao(
                        codigo
                );

        validarPendente(
                conta
        );

        TipoConta tipo =
                TipoConta.fromCodigo(
                        conta.getTipo()
                );

        Caixa caixa =
                buscarCaixaAberto();

        OrigemMovimentacaoCaixa origem =
                tipo == TipoConta.PAGAR
                        ? OrigemMovimentacaoCaixa.CONTA_PAGAR
                        : OrigemMovimentacaoCaixa.CONTA_RECEBER;

        if (caixaHistoricoRepository
                .existsByOrigemAndOrigemCodigo(
                        origem,
                        conta.getCodigo()
                )) {

            throw new BusinessRuleException(
                    "Esta conta já possui uma movimentação financeira vinculada."
            );
        }

        registrarMovimentacaoCaixa(
                caixa,
                conta,
                tipo,
                origem
        );

        conta.setStatus(
                StatusConta.BAIXADA
        );

        conta.setDataPagamento(
                agora()
        );

        Conta salva =
                contaRepository.save(
                        conta
                );

        if (tipo == TipoConta.PAGAR
                && conta.isRecorrente()) {

            gerarProximaRecorrencia(
                    conta
            );
        }

        return contaMapper.toResponse(
                salva
        );
    }

    @Transactional
    public ContaResponse cancelar(
            Integer codigo
    ) {
        Conta conta =
                buscarParaAtualizacao(
                        codigo
                );

        validarPendente(
                conta
        );

        conta.setStatus(
                StatusConta.CANCELADA
        );

        conta.setDataPagamento(
                null
        );

        Conta salva =
                contaRepository.save(
                        conta
                );

        return contaMapper.toResponse(
                salva
        );
    }

    private void preencherConta(
            Conta conta,
            ContaRequest request
    ) {
        conta.setTipo(
                request.tipo()
                        .getCodigo()
        );

        conta.setDescricao(
                request.descricao()
                        .trim()
        );

        conta.setFormaPagamento(
                request.formaPagamento()
                        .trim()
        );

        conta.setValor(
                request.valor()
        );

        conta.setDataVencimento(
                request.dataVencimento()
                        .atStartOfDay()
        );

        conta.setObservacao(
                normalizarObservacao(
                        request.observacao()
                )
        );

        conta.setRecorrente(
                Boolean.TRUE.equals(
                        request.recorrente()
                )
        );
    }

    private void registrarMovimentacaoCaixa(
            Caixa caixa,
            Conta conta,
            TipoConta tipo,
            OrigemMovimentacaoCaixa origem
    ) {
        String tipoMovimentacao =
                tipo == TipoConta.PAGAR
                        ? "SAIDA"
                        : "ENTRADA";

        String descricao =
                tipo == TipoConta.PAGAR
                        ? "PAGAMENTO DE CONTA"
                        : "RECEBIMENTO DE CONTA";

        String observacao =
                conta.getDescricao()
                        + " | Forma: "
                        + conta.getFormaPagamento();

        if (conta.getObservacao() != null
                && !conta.getObservacao()
                .isBlank()) {

            observacao =
                    observacao
                            + " | "
                            + conta.getObservacao();
        }

        CaixaHistorico historico =
                CaixaHistorico.builder()
                        .tipo(
                                tipoMovimentacao
                        )
                        .origem(
                                origem
                        )
                        .origemCodigo(
                                conta.getCodigo()
                        )
                        .caixa(
                                caixa
                        )
                        .descricao(
                                descricao
                        )
                        .observacao(
                                observacao
                        )
                        .data(
                                agora()
                        )
                        .valor(
                                conta.getValor()
                        )
                        .build();

        caixaHistoricoRepository.save(
                historico
        );
    }

    private void gerarProximaRecorrencia(
            Conta contaAtual
    ) {
        Conta proxima =
                new Conta();

        proxima.setTipo(
                TipoConta.PAGAR
                        .getCodigo()
        );

        proxima.setDescricao(
                contaAtual.getDescricao()
        );

        proxima.setFormaPagamento(
                contaAtual.getFormaPagamento()
        );

        proxima.setValor(
                contaAtual.getValor()
        );

        proxima.setDataVencimento(
                contaAtual
                        .getDataVencimento()
                        .plusMonths(1)
        );

        proxima.setDataPagamento(
                null
        );

        proxima.setObservacao(
                contaAtual.getObservacao()
        );

        proxima.setStatus(
                StatusConta.PENDENTE
        );

        proxima.setRecorrente(
                true
        );

        contaRepository.save(
                proxima
        );
    }

    private void validarRecorrencia(
            TipoConta tipo,
            Boolean recorrente
    ) {
        if (Boolean.TRUE.equals(recorrente)
                && tipo != TipoConta.PAGAR) {

            throw new BusinessRuleException(
                    "Somente contas a pagar podem ser recorrentes."
            );
        }
    }

    private void validarPendente(
            Conta conta
    ) {
        if (conta.getStatus()
                == StatusConta.BAIXADA) {

            throw new BusinessRuleException(
                    "Conta já está baixada e não pode ser alterada."
            );
        }

        if (conta.getStatus()
                == StatusConta.CANCELADA) {

            throw new BusinessRuleException(
                    "Conta cancelada não pode ser alterada."
            );
        }
    }

    private void validarPeriodo(
            LocalDate inicio,
            LocalDate fim
    ) {
        if (fim.isBefore(inicio)) {

            throw new BusinessRuleException(
                    "Data final não pode ser menor que a data inicial."
            );
        }
    }

    private Caixa buscarCaixaAberto() {
        return caixaRepository
                .findFirstByDataFechamentoIsNullOrderByDataAberturaDesc()
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Não existe caixa aberto. Abra o caixa antes de baixar a conta."
                        )
                );
    }

    private Conta buscarEntidade(
            Integer codigo
    ) {
        return contaRepository
                .findById(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Conta não encontrada. Código: "
                                        + codigo
                        )
                );
    }

    private Conta buscarParaAtualizacao(
            Integer codigo
    ) {
        return contaRepository
                .buscarParaAtualizacao(
                        codigo
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Conta não encontrada. Código: "
                                        + codigo
                        )
                );
    }

    private BigDecimal somar(
            List<ContaResponse> contas,
            TipoConta tipo,
            StatusConta status,
            boolean apenasVencidas
    ) {
        return contas.stream()
                .filter(conta ->
                        conta.tipo()
                                == tipo
                )
                .filter(conta ->
                        conta.status()
                                == status
                )
                .filter(conta ->
                        !apenasVencidas
                                || conta.vencida()
                )
                .map(
                        ContaResponse::valor
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    private String normalizarObservacao(
            String observacao
    ) {
        if (observacao == null) {
            return "";
        }

        return observacao.trim();
    }

    private LocalDateTime agora() {
        return LocalDateTime.now()
                .withNano(0);
    }
}