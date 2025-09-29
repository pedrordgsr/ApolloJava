package com.apollo.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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

    @Column(nullable = false, scale = 2, precision = 10)
    @Basic
    private BigDecimal precoCusto;

    @Column(nullable = false, scale = 2, precision = 10)
    @Basic
    private BigDecimal precoVenda;

}
