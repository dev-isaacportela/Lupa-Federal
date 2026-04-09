package com.lupafederal.core_api.dto.response;

import com.lupafederal.core_api.model.TipoPessoa;

public record FornecedorResponse (
        Integer id,
        String cnpjCpf,
        String razaoSocial,
        TipoPessoa tipoPessoa
){}
