package br.com.migracao.core.dto.procedimento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record ProcedimentoRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 200, message = "Nome deve possuir no máximo 200 caracteres")
        String nome,

        @DecimalMin(
                value = "0.00",
                inclusive = true,
                message = "Valor não pode ser negativo"
        )
        BigDecimal valor,

        @Valid
        List<ProcedimentoProdutoRequest> produtos

) {
}