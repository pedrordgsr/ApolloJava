package com.apollo.main.dto.request;

import com.apollo.main.validation.ValidCpfCnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PessoaRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Tipo de pessoa é obrigatório")
    private String tipoPessoa;

    @NotBlank(message = "CPF/CNPJ é obrigatório")
    @ValidCpfCnpj(message = "CPF ou CNPJ inválido")
    private String cpfcnpj;

    private String ie;

    private String email;

    private String telefone;

    private String endereco;

    private String bairro;

    private String cidade;

    private String uf;

    private String cep;
}
