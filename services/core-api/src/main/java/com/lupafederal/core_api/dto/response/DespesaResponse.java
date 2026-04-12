package com.lupafederal.core_api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaResponse(
        Integer id,
        Integer idAgente,
        Integer idFornecedor,
        Integer idCategoriaDespesa,
        LocalDate dataEmissao,
        BigDecimal valor,
        String numeroDocumento
) {}
