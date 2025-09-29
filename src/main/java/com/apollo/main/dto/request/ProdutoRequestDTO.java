package com.apollo.main.dto.request;

import com.apollo.main.model.StatusAtivo;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoRequestDTO {

    @NotNull(message = "Nome é obrigatório")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Nome é obrigatório")
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @Min(value = 0, message = "Quantidade em estoque não pode ser negativa")
    private int qntdEstoque;

    @Min(value = 0, message = "Preço de custo não pode ser negativo")
    private BigDecimal precoCusto;

    @Min(value = 0, message = "Preço de venda não pode ser negativo")
    private BigDecimal precoVenda;

}
