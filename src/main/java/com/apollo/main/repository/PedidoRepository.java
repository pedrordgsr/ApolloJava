package com.apollo.main.repository;

import com.apollo.main.model.Pedido;
import com.apollo.main.model.TipoPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Page<Pedido> findPedidoByPessoa_NomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Pedido> findPedidoByPessoa_NomeContainingIgnoreCaseAndTipo(String nome, TipoPedido tipo, Pageable pageable);
}
