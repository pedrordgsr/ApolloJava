package com.apollo.main.dto.response;

import com.apollo.main.model.Cliente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteResponseDTO extends PessoaResponseDTO{
    private String genero;

    public ClienteResponseDTO(Cliente cliente) {
        super(cliente);
        this.genero = cliente.getGenero();
    }
}
