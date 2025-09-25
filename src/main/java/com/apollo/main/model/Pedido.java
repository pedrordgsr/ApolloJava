package com.apollo.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @Basic
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @Basic
    @Enumerated(EnumType.STRING)
    private TipoPedido tipo;

    @Column(nullable = false)
    private LocalDateTime dataEmissao;

    @Column()
    private LocalDateTime vencimento;

    @Column(precision = 10, scale = 2)
    private Double totalCusto;

    @Column(precision = 10, scale = 2)
    private Double totalVenda;

    private String formaPagamento;

    @ManyToOne
    @JoinColumn(name = "id_pessoa")
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoProduto> itens = new ArrayList<>();
}
