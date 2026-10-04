package br.com.migracao.core.dto.procedimento;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProcedimentoProdutoRequest(

        @NotNull(message = "Produto é obrigatório")
        Integer produtoCodigo,

        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 1, message = "Quantidade deve ser maior que zero")
        Integer quantidade

) {
}