package com.lupafederal.core_api.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "fornecedores", schema = "lupa")
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_fornecedor_api", unique = true, nullable = false)
    private Long idFornecedorApi;

    @Column(name = "cnpj_cpf", nullable = false, unique = true, length = 20)
    private String cnpjCpf;

    @Column(name = "razao_social", nullable = false, length = 255)
    private String razaoSocial;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", nullable = false, length = 1)
    private TipoPessoa tipoPessoa;

    public Fornecedor() {}

    public void setIdFornecedorApi(Long idFornecedorApi) { this.idFornecedorApi = idFornecedorApi; }

    public void setCnpjCpf(String cnpjCpf) { this.cnpjCpf = cnpjCpf; }

    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public void setTipoPessoa(TipoPessoa tipoPessoa) { this.tipoPessoa = tipoPessoa; }
}