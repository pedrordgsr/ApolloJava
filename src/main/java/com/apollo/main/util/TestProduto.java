package com.apollo.main.util;

import com.apollo.main.model.Produto;

public class TestProduto {
    public static void main(String[] args) {
        Produto p = new Produto();
        p.setNome("Teste");
        System.out.println(p.getNome());
    }
}
