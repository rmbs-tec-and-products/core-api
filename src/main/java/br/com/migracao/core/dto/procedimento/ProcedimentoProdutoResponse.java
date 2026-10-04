package br.com.migracao.core.dto.procedimento;

public record ProcedimentoProdutoResponse(

        Integer produtoCodigo,
        String produtoNome,
        Integer quantidade

) {
}