package com.apollo.main.dto.response;

import com.apollo.main.model.PedidoProduto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PedidoProdutoResponseDTO {
    private Long produtoId;
    private Long pedidoId;
    private BigDecimal precoVendaUN;
    private BigDecimal precoCustoUN;
    private int qntd;

    public PedidoProdutoResponseDTO(PedidoProduto pedidoProduto){
        this.produtoId = pedidoProduto.getProduto().getId();
        this.pedidoId = pedidoProduto.getPedido().getIdPedido();
        this.precoVendaUN = pedidoProduto.getPrecoVendaUN();
        this.precoCustoUN = pedidoProduto.getPrecoCustoUN();
        this.qntd = pedidoProduto.getQntd();
    }
}
