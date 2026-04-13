package com.lupafederal.core_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoriaDespesaRequest(
        @NotNull
        Long idCategoriaDespesaApi,

        @NotBlank
        @Size(min = 5, max = 255,message = "O campo deve ter no mínimo 5 caracteres e no máximo 255 caracteres")
        String descricao
) {}
