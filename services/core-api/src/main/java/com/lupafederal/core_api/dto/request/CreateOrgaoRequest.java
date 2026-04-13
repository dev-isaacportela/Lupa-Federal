package com.lupafederal.core_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateOrgaoRequest (
        @NotNull
        Long idOrgaoApi,

        @NotBlank
        @Size(min = 1, max = 255,message = "O campo deve ter no mínimo 1 caracter e no máximo 255 caracteres")
        String codigoSiafi,

        @NotBlank
        @Size(min = 5, max = 255,message = "O campo deve ter no mínimo 5 caracteres e no máximo 255 caracteres")
        String nome,

        @NotBlank
        @Size(min = 2, max = 10, message = "O campo deve ter no mínimo 2 caracteres e no máximo 10 caracteres")
        String sigla
){}
