package com.lupafederal.core_api.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FiltroDespesaRequest {
    @Schema(description = "Identificador do Agente", example = "10")
    private Integer idAgente;

    @Schema(description = "Identificador do Fornecedor", example = "50")
    private Integer idFornecedor;

    @Schema(description = "Identificador da Categoria de Despesa", example = "5")
    private Integer idCategoriaDespesa;

    @Parameter(description = "Data de início (AAAA-MM-DD)", example = "2024-01-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;

    @Parameter(description = "Data de fim (AAAA-MM-DD)", example = "2024-01-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;

    @Positive
    @Schema(description = "Valor mínimo da despesa", example = "100.00")
    private BigDecimal valorMinimo;

    @Positive
    @Schema(description = "Valor máximo da despesa", example = "5000.00")
    private BigDecimal valorMaximo;

    @Schema(description = "Número do documento fiscal", example = "NF-12345")
    private String numeroDocumento;

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Integer getIdCategoriaDespesa() {
        return idCategoriaDespesa;
    }

    public void setIdCategoriaDespesa(Integer idCategoriaDespesa) {
        this.idCategoriaDespesa = idCategoriaDespesa;
    }

    public Integer getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Integer idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public Integer getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Integer idAgente) {
        this.idAgente = idAgente;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(BigDecimal valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public BigDecimal getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(BigDecimal valorMaximo) {
        this.valorMaximo = valorMaximo;
    }
}