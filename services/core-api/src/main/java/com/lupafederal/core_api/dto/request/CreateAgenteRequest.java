package com.lupafederal.core_api.dto.request;

public record CreateAgenteRequest(
        Long idPortalApi,
        String nome,
        String cpfMascarado,
        Integer idOrgao
) {}
