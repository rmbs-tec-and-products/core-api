package br.com.migracao.core.dto.conta;

import br.com.migracao.core.domain.enums.TipoConta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaRequest(

        @NotNull(message = "Tipo da conta é obrigatório")
        TipoConta tipo,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(
                max = 100,
                message = "Descrição deve possuir no máximo 100 caracteres"
        )
        String descricao,

        @NotBlank(message = "Forma de pagamento/recebimento é obrigatória")
        @Size(
                max = 100,
                message = "Forma de pagamento deve possuir no máximo 100 caracteres"
        )
        String formaPagamento,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(
                value = "0.01",
                message = "Valor deve ser maior que zero"
        )
        BigDecimal valor,

        @NotNull(message = "Data de vencimento é obrigatória")
        LocalDate dataVencimento,

        String observacao,

        Boolean recorrente

) {
}