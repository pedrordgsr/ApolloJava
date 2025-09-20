package com.apollo.main.dto.response;

import com.apollo.main.model.Fornecedor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FornecedorResponseDTO extends PessoaResponseDTO{
    private String tipoFornecedor;

    public FornecedorResponseDTO(Fornecedor fornecedor) {
        super(fornecedor);
        this.tipoFornecedor = fornecedor.getTipoFornecedor();
    }
}
