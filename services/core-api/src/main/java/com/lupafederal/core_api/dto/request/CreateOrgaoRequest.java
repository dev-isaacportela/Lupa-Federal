package com.lupafederal.core_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateOrgaoRequest (
        @NotNull
        Long idOrgaoApi,

        @NotBlank
        @Size(min = 1, message = "O campo deve ter no mínimo 1 caracter")
        String codigoSiafi,

        @NotBlank
        @Size(min = 5, message = "O campo deve ter no mínimo 5 caracteres")
        String nome,

        @NotBlank
        @Size(min = 2, message = "O campo deve ter no mínimo 2 caracteres")
        String sigla
){}
