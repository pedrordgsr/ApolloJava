package com.apollo.main.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Fornecedor extends Pessoa{

    @Basic
    @Column
    private String tipoFornecedor;

    public Fornecedor() {
        this.setCategoria("Fornecedor");
    }
}
