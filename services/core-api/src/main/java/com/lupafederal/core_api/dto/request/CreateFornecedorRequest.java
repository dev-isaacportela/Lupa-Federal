package com.lupafederal.core_api.dto.request;

import com.lupafederal.core_api.model.TipoPessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateFornecedorRequest(
        @NotNull
        Long idFornecedorApi,

        @NotBlank
        @Pattern(
                regexp = "(^\\d{11}$)|(^\\d{14}$)",
                message = "O campo deve ser um CPF (11 dígitos) ou um CNPJ (14 dígitos)"
        )
        String cnpjCpf,

        @NotBlank
        @Size(min = 5, max = 255, message = "O campo deve ter no mínimo 5 caracteres e no máximo 255 caracteres")
        String razaoSocial,

        @NotNull(message = "O tipo da despesa é obrigatório")
        TipoPessoa tipoPessoa
) {

}
