package com.lupafederal.core_api.dto.response;

import com.lupafederal.core_api.model.Despesa;

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
) {
    public DespesaResponse(Despesa entidade) {
        this(
                entidade.getId(),
                entidade.getAgente().getId(),
                entidade.getFornecedor().getId(),
                entidade.getCategoriaDespesa().getId(),
                entidade.getDataEmissao(),
                entidade.getValor(),
                entidade.getNumeroDocumento()
        );
    }
}
