package com.apollo.main.dto;


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

    private int id;
    private StatusProduto status;
    private String nome;
    private String descricao;
    private int qntdEstoque;
    private Double precoCusto;
    private Double precoVenda;

}
