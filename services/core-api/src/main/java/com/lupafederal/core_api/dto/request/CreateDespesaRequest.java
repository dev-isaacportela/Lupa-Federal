package com.lupafederal.core_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateDespesaRequest(
        @NotNull
        Long idDespesaApi,

        @NotNull
        Integer idAgente,

        @NotNull
        Integer idFornecedor,

        @NotNull
        Integer idCategoriaDespesa,

        @NotNull
        LocalDate dataEmissao,

        @NotNull
        @Positive
        BigDecimal valor,

        @NotBlank
        String numeroDocumento
) {}