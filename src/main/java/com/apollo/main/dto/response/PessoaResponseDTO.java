package com.apollo.main.dto.response;

import com.apollo.main.model.Pessoa;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PessoaResponseDTO {
    private Long id;
    private String status;
    private String nome;
    private String categoria;
    private String cpfCnpj;
    private String ie;
    private String email;
    private int telefone;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private int cep;
    private LocalDateTime dataCadastro;

    public PessoaResponseDTO(Pessoa pessoa){
        this.id = pessoa.getIdPessoa();
        this.status = pessoa.getStatus().name();
        this.nome = pessoa.getNome();
        this.categoria = pessoa.getCategoria();
        this.cpfCnpj = pessoa.getCpfcnpj();
        this.ie = pessoa.getIe();
        this.email = pessoa.getEmail();
        this.telefone = pessoa.getTelefone();
        this.endereco = pessoa.getEndereco();
        this.bairro = pessoa.getBairro();
        this.cidade = pessoa.getCidade();
        this.uf = pessoa.getUf();
        this.cep = pessoa.getCep();
        this.dataCadastro = pessoa.getDataCadastro();
    }
}
