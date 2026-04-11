package com.lupafederal.core_api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "despesas", schema = "lupa")
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_despesa_api", unique = true, nullable = false)
    private Long idDespesaApi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agente", nullable = false)
    private Agente agente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fornecedor", nullable = false)
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaDespesa categoriaDespesa;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "numero_documento", length = 50)
    private String numeroDocumento;

    public void setIdDespesaApi(Long idDespesaApi) { this.idDespesaApi = idDespesaApi; }

    public void setAgente(Agente agente) { this.agente = agente; }

    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public void setCategoriaDespesa(CategoriaDespesa categoriaDespesa) { this.categoriaDespesa = categoriaDespesa; }

    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }

    public void setValor(BigDecimal valor) { this.valor = valor; }

    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
}