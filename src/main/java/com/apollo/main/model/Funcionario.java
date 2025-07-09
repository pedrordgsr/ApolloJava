package com.apollo.main.model;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Funcionario extends Pessoa{

    @Basic
    @Column(nullable = false)
    private Date dataAdmissao;

    @Basic
    @Column(nullable = false)
    private Double salario;

    @Column(nullable = true)
    private Date dataDemissao;

    public Funcionario() {
        this.setCategoria("Funcionario");
    }

}
