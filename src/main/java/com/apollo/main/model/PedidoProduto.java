package com.apollo.main.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Basic
    @Column(nullable = false,scale = 2, precision = 10)
    private BigDecimal precoVendaUN;

    @Basic
    @Column(nullable = false,scale = 2, precision = 10)
    private BigDecimal precoCustoUN;

    @Basic
    @Column(nullable = false)
    private int qntd;
}
