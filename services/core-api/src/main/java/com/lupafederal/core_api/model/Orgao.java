package com.lupafederal.core_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orgaos", schema = "lupa")
public class Orgao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_siafi", nullable = false, unique = true, length = 10)
    private String codigoSiafi;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(length = 10)
    private String sigla;

    public Orgao() {}

    public Integer getId() {
        return id;
    }

    public String getCodigoSiafi() {
        return codigoSiafi;
    }

    public void setCodigoSiafi(String codigoSiafi) {
        this.codigoSiafi = codigoSiafi;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
