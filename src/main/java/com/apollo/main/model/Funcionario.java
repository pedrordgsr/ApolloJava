package com.apollo.main.model;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Funcionario extends Pessoa{

    @Basic
    @Column(nullable = false)
    private LocalDateTime dataAdmissao;

    @Basic
    @Column(nullable = false,scale = 2, precision = 10)
    private BigDecimal salario;

    @Column(nullable = false)
    private String cargo;

    @Column(nullable = true)
    private LocalDateTime dataDemissao;

    public Funcionario() {
        this.setCategoria("Funcionario");
    }

}
