package com.apollo.main.dto.request.Pedido;

import com.apollo.main.model.Funcionario;
import com.apollo.main.model.PedidoProduto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoRequestDTO {
    private LocalDateTime vencimento;
    private String formaPagamento;
    private Long idPessoa;
    private Long idFuncionario;
    private List<PedidoProduto> itens;
}
