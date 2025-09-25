package com.apollo.main.dto.request.Pedido;

import com.apollo.main.model.Pedido;
import com.apollo.main.model.PedidoProduto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {
    private Long idPedido;
    private String status;
    private String tipo;
    private LocalDateTime dataEmissao;
    private LocalDateTime vencimento;
    private Double totalVenda;
    private String formaPagamento;
    private Long idPessoa;
    private Long idFuncionario;
    private List<PedidoProduto> itens;

    public PedidoResponseDTO(Pedido pedido){
        this.idPedido = pedido.getIdPedido();
        this.status = pedido.getStatus().name();
        this.tipo = pedido.getTipo().name();
        this.dataEmissao = pedido.getDataEmissao();
        this.vencimento = pedido.getVencimento();
        this.totalVenda = pedido.getTotalVenda();
        this.formaPagamento = pedido.getFormaPagamento();
        this.idPessoa = pedido.getPessoa().getIdPessoa();
        this.idFuncionario = pedido.getFuncionario().getIdPessoa();
        this.itens = pedido.getItens();
    }

}


