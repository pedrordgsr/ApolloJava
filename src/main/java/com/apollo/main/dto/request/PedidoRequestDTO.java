package com.apollo.main.dto.request;

import com.apollo.main.model.PedidoProduto;
import com.apollo.main.model.TipoPedido;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoRequestDTO {
    private TipoPedido tipo;
    private LocalDateTime vencimento;
    private String formaPagamento;
    private Long idPessoa;
    private Long idFuncionario;
    private List<PedidoProdutoRequestDTO> itens;
}
