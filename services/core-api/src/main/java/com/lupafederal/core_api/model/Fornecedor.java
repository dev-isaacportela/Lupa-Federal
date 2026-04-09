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

    @Column(name = "cnpj_cpf", nullable = false, unique = true, length = 20)
    private String cnpjCpf;

    @Column(name = "razao_social", nullable = false, length = 255)
    private String razaoSocial;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", nullable = false, length = 1)
    private TipoPessoa tipoPessoa;

    public Fornecedor() {}

    public void setCnpjCpf(String cnpjCpf) { this.cnpjCpf = cnpjCpf; }

    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public void setTipoPessoa(TipoPessoa tipoPessoa) { this.tipoPessoa = tipoPessoa; }
}