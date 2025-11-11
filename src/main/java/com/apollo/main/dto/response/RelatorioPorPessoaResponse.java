package com.apollo.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioPorPessoaResponse {
    private Long idPessoa;
    private String nome;
    private String cpfCnpj;
    private String tipo; // CLIENTE ou FORNECEDOR
    private Long quantidadePedidos;
    private BigDecimal totalValor;
    private BigDecimal ticketMedio;
    private String ultimoPedido;
}
