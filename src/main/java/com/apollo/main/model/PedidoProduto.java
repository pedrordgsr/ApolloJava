package com.apollo.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class PedidoProduto {

    @Id
    @GeneratedValue
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Basic
    @Column(nullable = false,scale = 2, precision = 10)
    private BigDecimal ValorUN;

    @Basic
    @Column(nullable = false)
    private int qntd;
}
