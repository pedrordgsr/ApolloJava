package com.apollo.main.dto.response;

import com.apollo.main.model.Pedido;
import com.apollo.main.model.PedidoProduto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PedidoResponseDTO {
    private Long idPedido;
    private String status;
    private String tipo;
    private LocalDateTime dataEmissao;
    private LocalDateTime vencimento;
    private BigDecimal totalCusto;
    private BigDecimal totalVenda;
    private String formaPagamento;
    private Long idPessoa;
    private Long idFuncionario;
    private List<PedidoProdutoResponseDTO> itens;

    public PedidoResponseDTO(Pedido pedido){
        this.idPedido = pedido.getIdPedido();
        this.status = pedido.getStatus().name();
        this.tipo = pedido.getTipo().name();
        this.dataEmissao = pedido.getDataEmissao();
        this.vencimento = pedido.getVencimento();
        this.totalCusto = pedido.getTotalCusto();
        this.totalVenda = pedido.getTotalVenda();
        this.formaPagamento = pedido.getFormaPagamento();
        this.idPessoa = pedido.getPessoa().getIdPessoa();
        this.idFuncionario = pedido.getFuncionario().getIdPessoa();
        this.itens = pedido.getItens().stream().map(PedidoProdutoResponseDTO::new).collect(Collectors.toList());
    }

}


