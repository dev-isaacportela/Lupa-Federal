package com.lupafederal.core_api.dto.response;

public record AgenteResponse (
        Integer id,
        String nome,
        String cpfMascarado,
        Integer idOrgao
){}
