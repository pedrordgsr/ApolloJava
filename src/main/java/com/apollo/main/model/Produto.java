package com.apollo.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 45)
    @Enumerated(EnumType.STRING)
    private StatusProduto status;

    @Column(length = 45)
    @Basic
    private String nome;

    @Column(length = 45)
    @Basic
    private String descricao;

    @Basic
    private int qntdEstoque;

    @Basic
    private Double precoCusto;

    @Basic
    private Double precoVenda;

}
