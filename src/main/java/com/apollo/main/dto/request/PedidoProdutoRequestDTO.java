package com.apollo.main.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PedidoProdutoRequestDTO {
    private Long produtoId;
    private int qntd;
    private BigDecimal precoVendaUN;
}

