package com.lupafederal.core_api.dto.response;

import java.util.List;

public record IngestDespesaResponse(
        Integer criadosComSucesso,
        List<IngestDespesaItemErroResponse> falhas
){}
