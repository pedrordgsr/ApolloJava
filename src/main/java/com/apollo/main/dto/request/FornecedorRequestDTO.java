package com.apollo.main.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FornecedorRequestDTO extends PessoaRequestDTO{
    @NotBlank(message = "Tipo de fornecedor é obrigatório")
    @Schema(description = "Tipo de fornecedor", example = "Bebidas e alimentos")
    private String tipoFornecedor;
}
