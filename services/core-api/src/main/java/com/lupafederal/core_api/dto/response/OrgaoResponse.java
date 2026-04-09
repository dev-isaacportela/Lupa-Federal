package com.lupafederal.core_api.dto.response;

public record OrgaoResponse(
        Integer id,
        String codigoSiafi,
        String nome,
        String sigla) {
}
