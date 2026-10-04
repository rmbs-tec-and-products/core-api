package br.com.migracao.core.dto.produto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve possuir no máximo 100 caracteres")
        String nome,

        @NotNull(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 0, message = "Quantidade não pode ser negativa")
        Integer quantidade,

        @NotBlank(message = "Unidade é obrigatória")
        @Size(max = 100, message = "Unidade deve possuir no máximo 100 caracteres")
        String unidade,

        @Min(value = 0, message = "Embalagem não pode ser negativa")
        Integer embalagem,

        @Min(value = 0, message = "Quantidade por embalagem não pode ser negativa")
        Integer qtdEmbalagem,

        @DecimalMin(
                value = "0.00",
                inclusive = true,
                message = "Valor não pode ser negativo"
        )
        BigDecimal valor,

        @DecimalMin(
                value = "0.00",
                inclusive = true,
                message = "Último valor não pode ser negativo"
        )
        BigDecimal ultimoValor,

        Integer fornecedorCodigo,

        @Min(value = 0, message = "Quantidade mínima não pode ser negativa")
        Integer quantidadeMinima

) {
}