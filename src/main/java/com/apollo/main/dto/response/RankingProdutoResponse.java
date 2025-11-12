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
public class RankingProdutoResponse {
    private Long idProduto;
    private String nome;
    private String descricao;
    private Long quantidadeVendida;
    private BigDecimal valorTotalVendido;
    private BigDecimal lucroTotal;
    private BigDecimal margemLucro; // Percentual de margem de lucro
    private BigDecimal ticketMedio; // Valor médio por venda
    private Integer posicaoRanking;
}
