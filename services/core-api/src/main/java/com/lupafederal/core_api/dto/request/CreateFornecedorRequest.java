package com.lupafederal.core_api.dto.request;

import com.lupafederal.core_api.model.TipoPessoa;

public record CreateFornecedorRequest(
        String cnpjCpf,
        String razaoSocial,
        TipoPessoa tipoPessoa
) {}
