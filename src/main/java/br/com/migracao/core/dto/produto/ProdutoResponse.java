package br.com.migracao.core.dto.produto;

import java.math.BigDecimal;

public record ProdutoResponse(

        Integer codigo,
        String nome,
        String descricao,
        Integer quantidade,
        String unidade,
        Integer embalagem,
        Integer qtdEmbalagem,
        BigDecimal valor,
        BigDecimal ultimoValor,
        Integer fornecedorCodigo,
        String fornecedorNome,
        Integer quantidadeMinima

) {
}