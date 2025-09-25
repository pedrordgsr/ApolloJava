package com.apollo.main.dto.request.Pedido;

import com.apollo.main.model.PedidoProduto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {
    private Long idPedido;
    private String status;
    private String tipo;
    private LocalDateTime dataEmissao;
    private LocalDateTime vencimento;
    private Double valor;
    private String formaPagamento;
    private Long idPessoa;
    private Long idFuncionario;
    private List<PedidoProduto> itens;
}
