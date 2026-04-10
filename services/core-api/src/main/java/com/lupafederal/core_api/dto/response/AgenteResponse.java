package com.lupafederal.core_api.dto.response;

public record AgenteResponse (
        Integer id,
        Long idPortalApi,
        String nome,
        String cpfMascarado,
        Integer idOrgao
){}
