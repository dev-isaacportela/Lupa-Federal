package com.lupafederal.core_api.dto.response;

public record IngestDespesaItemErroResponse(
        Long idDespesaApi,
        Integer codigoErro,
        String mensagemErro
) {
}
