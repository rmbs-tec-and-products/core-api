package br.com.migracao.core.dto.fornecedor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FornecedorRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve possuir no máximo 100 caracteres")
        String nome,

        @Size(max = 25, message = "Telefone deve possuir no máximo 25 caracteres")
        String telefone

) {
}