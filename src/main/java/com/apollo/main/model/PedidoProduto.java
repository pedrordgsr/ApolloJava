package com.apollo.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PedidoProduto {

    @Id
    @GeneratedValue
    private int Id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Basic
    @Column(nullable = false)
    private Double ValorUN;

    @Basic
    @Column(nullable = false)
    private int qntd;
}
