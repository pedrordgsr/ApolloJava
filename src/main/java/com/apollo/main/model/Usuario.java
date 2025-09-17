package com.apollo.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdUsuario;

    @Basic
    @Column(nullable = false, length = 45)
    @Enumerated(EnumType.STRING)
    private StatusAtivo statusUsuario;

    @Basic
    @Column(nullable = false, length = 45)
    private String username;

    @Basic
    @Column(nullable = false, length = 45)
    private String senha;

    @OneToOne(cascade = CascadeType.ALL)
    private Funcionario funcionario;

}
