package com.lupafederal.core_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "agentes_politicos", schema = "lupa")
public class Agente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private Long idPortalApi;

    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Column(name = "cpf_mascarado", length = 20)
    private String cpfMascarado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orgao", nullable = false)
    private Orgao orgao;

    public Integer getId() {
        return id;
    }

    public Long getIdPortalApi() {
        return idPortalApi;
    }

    public String getNome() {
        return nome;
    }

    public String getCpfMascarado() {
        return cpfMascarado;
    }

    public Orgao getOrgao() {
        return orgao;
    }

    public void setIdPortalApi(Long idPortalApi) {
        this.idPortalApi = idPortalApi;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpfMascarado(String cpfMascarado) {
        this.cpfMascarado = cpfMascarado;
    }

    public void setOrgao(Orgao orgao) {
        this.orgao = orgao;
    }
}