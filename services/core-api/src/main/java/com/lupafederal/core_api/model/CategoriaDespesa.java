package com.lupafederal.core_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "categorias_despesa", schema = "lupa")
public class CategoriaDespesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descricao", nullable = false, length = 100, unique = true)
    private String descricao;

    public CategoriaDespesa() {}

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
