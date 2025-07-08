package com.apollo.main.dto;


import com.apollo.main.model.Produto;
import com.apollo.main.model.StatusProduto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
        this.status = produto.getStatus();
    }

    private int id;
    private StatusProduto status;
    private String nome;
    private String descricao;
    private int qntdEstoque;
    private Double precoCusto;
    private Double precoVenda;

}
