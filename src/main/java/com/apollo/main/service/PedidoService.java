package com.apollo.main.service;

import com.apollo.main.dto.request.Pedido.PedidoRequestDTO;
import com.apollo.main.dto.request.Pedido.PedidoResponseDTO;
import com.apollo.main.model.Cliente;
import com.apollo.main.model.Funcionario;
import com.apollo.main.model.Pedido;
import com.apollo.main.model.TipoPedido;
import com.apollo.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final FornecedorRepository fornecedorRepository;
    private final FuncionarioRepository funcionarioRepository;

    private final ProdutoService produtoService;
    private final ProdutoRepository produtoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         FornecedorRepository fornecedorRepository,
                         FuncionarioRepository funcionarioRepository,
                         ProdutoService produtoService,
                         ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
    }

    public PedidoResponseDTO createPedido(PedidoRequestDTO dto) {
        TipoPedido tipoPedido = dto.getTipo();

        if(tipoPedido == TipoPedido.VENDA) {
            Cliente cliente = clienteRepository.findById(dto.getIdPessoa())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

            Funcionario funcionario = funcionarioRepository.findById(dto.getIdFuncionario())
                    .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

            Pedido pedido = new Pedido();
            pedido.setTipo(TipoPedido.VENDA);
            pedido.setVencimento(dto.getVencimento());
            pedido.setFormaPagamento(dto.getFormaPagamento());
            pedido.setPessoa(cliente);
            pedido.setFuncionario(funcionario);
            pedido.setItens(new ArrayList<>());

            Double valor = 0.0;

            for(var item : dto.getItens()) {
                var produto = produtoRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + item.getProduto().getId()));

                if(produto.getQntdEstoque() < item.getQntd()) {
                    throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
                }

                produtoService.removeStock(produto.getId(), item.getQntd());

                item.setPedido(pedido);
                valor += produto.getPrecoVenda() * item.getQntd();
                pedido.getItens().add(item);
            }

            pedido.setTotalVenda(valor);
            Pedido savedPedido = pedidoRepository.save(pedido);
            return new PedidoResponseDTO(savedPedido);

        } else if (tipoPedido == TipoPedido.COMPRA) {
            // Lógica para criar pedido de compra
            return null;
        } else if (tipoPedido == TipoPedido.DEVOLUCAO) {
            // Lógica para criar pedido de devolução
            return null;
        }
        else {
            throw new IllegalArgumentException("Tipo de pedido inválido");
        }
    }
}
