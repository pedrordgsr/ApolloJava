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
    private Long id;

    @Column(length = 45, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusAtivo status;

    @Column(length = 45, nullable = false)
    @Basic
    private String nome;

    @Column(length = 45, nullable = false)
    @Basic
    private String descricao;

    @Column(nullable = false)
    @Basic
    private int qntdEstoque;

    @Column(nullable = false)
    @Basic
    private Double precoCusto;

    @Column(nullable = false)
    @Basic
    private Double precoVenda;

}
