package com.apollo.main.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cliente extends Pessoa {

    @Basic
    @Column(length = 45)
    private String genero;

    public Cliente() {
        this.setCategoria("Cliente");
    }
}
