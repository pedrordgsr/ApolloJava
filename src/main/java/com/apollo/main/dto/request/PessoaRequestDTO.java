package com.apollo.main.dto.request;

import com.apollo.main.model.StatusAtivo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PessoaRequestDTO {

    @NotNull
    private String nome;

    @NotNull
    private String categoria;

    @NotNull
    private String cpfcnpj;

    private String ie;

    private String email;

    private int telefone;

    @NotNull
    private String endereco;

    @NotNull
    private String bairro;

    @NotNull
    private String cidade;

    @NotNull
    private String uf;

    @NotNull
    private int cep;
}
