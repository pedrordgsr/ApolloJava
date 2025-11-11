package com.apollo.main.dto.response;

import com.apollo.main.model.StatusPedido;
import com.apollo.main.model.TipoPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalhePedidoRelatorioResponse {
    private Long idPedido;
    private TipoPedido tipo;
    private StatusPedido status;
    private LocalDateTime dataEmissao;
    private String nomePessoa;
    private String nomeFuncionario;
    private BigDecimal totalCusto;
    private BigDecimal totalVenda;
    private String formaPagamento;
}
