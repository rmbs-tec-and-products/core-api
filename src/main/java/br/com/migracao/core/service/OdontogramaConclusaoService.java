package br.com.migracao.core.service;

import br.com.migracao.core.domain.entity.Caixa;
import br.com.migracao.core.domain.entity.CaixaHistorico;
import br.com.migracao.core.domain.entity.Odontograma;
import br.com.migracao.core.domain.entity.OdontogramaProcedimento;
import br.com.migracao.core.domain.entity.ProcedimentoProduto;
import br.com.migracao.core.domain.entity.Produto;
import br.com.migracao.core.domain.enums.StatusProcedimentoOdontograma;
import br.com.migracao.core.dto.odontograma.OdontogramaProcedimentoResponse;
import br.com.migracao.core.exception.BusinessRuleException;
import br.com.migracao.core.exception.ResourceNotFoundException;
import br.com.migracao.core.mapper.OdontogramaMapper;
import br.com.migracao.core.repository.CaixaHistoricoRepository;
import br.com.migracao.core.repository.CaixaRepository;
import br.com.migracao.core.repository.OdontogramaProcedimentoRepository;
import br.com.migracao.core.repository.OdontogramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OdontogramaConclusaoService {

    private final OdontogramaRepository odontogramaRepository;
    private final OdontogramaProcedimentoRepository odontogramaProcedimentoRepository;
    private final CaixaRepository caixaRepository;
    private final CaixaHistoricoRepository caixaHistoricoRepository;
    private final OdontogramaMapper odontogramaMapper;

    @Transactional
    public OdontogramaProcedimentoResponse concluir(
            Integer odontogramaCodigo,
            Integer itemCodigo
    ) {
        Odontograma odontograma =
                buscarOdontograma(
                        odontogramaCodigo
                );

        OdontogramaProcedimento item =
                buscarItem(
                        odontogramaCodigo,
                        itemCodigo
                );

        validarNaoConcluido(item);

        Caixa caixa =
                buscarCaixaAberto();

        List<ProcedimentoProduto> produtosUtilizados =
                item.getProcedimento()
                        .getProdutos();

        validarEstoque(
                produtosUtilizados
        );

        baixarEstoque(
                produtosUtilizados
        );

        registrarEntradaCaixa(
                caixa,
                odontograma,
                item
        );

        item.setStatus(
                StatusProcedimentoOdontograma
                        .CONCLUIDO
                        .getCodigo()
        );

        item.setData(
                LocalDateTime.now()
                        .withNano(0)
        );

        odontogramaProcedimentoRepository
                .saveAndFlush(item);

        recalcularValorPendente(
                odontograma
        );

        return odontogramaMapper
                .toProcedimentoResponse(
                        item
                );
    }

    private void validarEstoque(
            List<ProcedimentoProduto> produtosUtilizados
    ) {
        if (produtosUtilizados == null
                || produtosUtilizados.isEmpty()) {
            return;
        }

        for (ProcedimentoProduto composicao
                : produtosUtilizados) {

            Produto produto =
                    composicao.getProduto();

            Integer quantidadeNecessaria =
                    composicao.getQuantidade();

            if (quantidadeNecessaria == null
                    || quantidadeNecessaria <= 0) {

                throw new BusinessRuleException(
                        "Quantidade inválida na composição do produto "
                                + produto.getNome()
                                + "."
                );
            }

            Integer estoqueAtual =
                    produto.getQuantidade();

            if (estoqueAtual == null) {
                estoqueAtual = 0;
            }

            if (estoqueAtual < quantidadeNecessaria) {
                throw new BusinessRuleException(
                        "Estoque insuficiente para o produto "
                                + produto.getNome()
                                + ". Disponível: "
                                + estoqueAtual
                                + ", necessário: "
                                + quantidadeNecessaria
                                + "."
                );
            }
        }
    }

    private void baixarEstoque(
            List<ProcedimentoProduto> produtosUtilizados
    ) {
        if (produtosUtilizados == null
                || produtosUtilizados.isEmpty()) {
            return;
        }

        for (ProcedimentoProduto composicao
                : produtosUtilizados) {

            Produto produto =
                    composicao.getProduto();

            Integer novaQuantidade =
                    produto.getQuantidade()
                            - composicao.getQuantidade();

            produto.setQuantidade(
                    novaQuantidade
            );
        }
    }

    private void registrarEntradaCaixa(
            Caixa caixa,
            Odontograma odontograma,
            OdontogramaProcedimento item
    ) {
        BigDecimal valor =
                item.getValor() != null
                        ? item.getValor()
                        : BigDecimal.ZERO;

        /*
         * Não gera uma movimentação financeira de R$ 0,00.
         * A conclusão e a baixa de estoque continuam acontecendo.
         */
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        String nomePaciente =
                odontograma.getNome() != null
                        && !odontograma.getNome().isBlank()
                        ? odontograma.getNome()
                        : "PACIENTE";

        String nomeProcedimento =
                item.getProcedimento().getNome();

        String observacao =
                "PAGAMENTO DO PACIENTE "
                        + nomePaciente
                        + " EM RELAÇÃO À FINALIZAÇÃO DO PROCEDIMENTO "
                        + nomeProcedimento
                        + " DO DENTE "
                        + item.getDente();

        CaixaHistorico historico =
                CaixaHistorico.builder()
                        .tipo("ENTRADA")
                        .caixa(caixa)
                        .descricao(
                                "FINALIZAÇÃO DE PROCEDIMENTO"
                        )
                        .observacao(observacao)
                        .data(
                                LocalDateTime.now()
                                        .withNano(0)
                        )
                        .valor(valor)
                        .build();

        caixaHistoricoRepository.save(
                historico
        );
    }

    private void recalcularValorPendente(
            Odontograma odontograma
    ) {
        List<OdontogramaProcedimento> itens =
                odontogramaProcedimentoRepository
                        .findByOdontograma_CodigoOrderByCodigoAsc(
                                odontograma.getCodigo()
                        );

        BigDecimal total =
                itens.stream()
                        .filter(item ->
                                !StatusProcedimentoOdontograma
                                        .CONCLUIDO
                                        .getCodigo()
                                        .equals(
                                                item.getStatus()
                                        )
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

    private Caixa buscarCaixaAberto() {
        return caixaRepository
                .findFirstByDataFechamentoIsNullOrderByDataAberturaDesc()
                .orElseThrow(() ->
                        new BusinessRuleException(
                                "Não existe caixa aberto. Abra o caixa antes de concluir o procedimento."
                        )
                );
    }

    private Odontograma buscarOdontograma(
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

    private void validarNaoConcluido(
            OdontogramaProcedimento item
    ) {
        if (StatusProcedimentoOdontograma
                .CONCLUIDO
                .getCodigo()
                .equals(
                        item.getStatus()
                )) {

            throw new BusinessRuleException(
                    "Este procedimento já está concluído."
            );
        }
    }
}