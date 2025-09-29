package com.apollo.main.dto.response;


import com.apollo.main.model.Produto;
import com.apollo.main.model.StatusAtivo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {

    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.precoCusto = produto.getPrecoCusto();
        this.precoVenda = produto.getPrecoVenda();
        this.qntdEstoque = produto.getQntdEstoque();
        this.status = produto.getStatus();
    }

    private Long id;
    private StatusAtivo status;
    private String nome;
    private String descricao;
    private int qntdEstoque;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;

}
