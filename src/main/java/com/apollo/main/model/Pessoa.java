package com.apollo.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPessoa;

    @Basic
    @Column(nullable = false, length = 45)
    @Enumerated(EnumType.STRING)
    private StatusAtivo status;

    @Column(nullable = false, length = 45)
    @Basic
    private String nome;

    @Column(nullable = false, length = 45)
    @Basic
    private String categoria;

    @Basic
    @Column(nullable = false, length = 45)
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipoPessoa;

    @Column(nullable = false, length = 45, unique = true)
    @Basic
    private String cpfcnpj;

    @Column(nullable = true, length = 45)
    @Basic
    private String ie;

    @Column(length = 45)
    @Basic
    private String email;

    @Column(length = 45)
    @Basic
    private String telefone;

    @Column
    @Basic
    private String endereco;

    @Column
    @Basic
    private String bairro;

    @Column
    @Basic
    private String cidade;

    @Column(length = 45)
    @Basic
    private String uf;

    @Column(length = 45)
    @Basic
    private String cep;

    @Column(nullable = false)
    @Basic
    private LocalDateTime dataCadastro;

}
