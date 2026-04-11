package com.lupafederal.core_api.dto.request;

public record CreateOrgaoRequest (
        Long idOrgaoApi,
        String codigoSiafi,
        String nome,
        String sigla
){}
