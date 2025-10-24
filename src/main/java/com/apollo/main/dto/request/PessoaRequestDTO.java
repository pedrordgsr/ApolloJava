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

    @Email(message = "Email inválido")
    private String email;

    private String telefone;

    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;

    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "UF é obrigatório")
    private String uf;

    @NotNull(message = "CEP é obrigatório")
    private String cep;
}
