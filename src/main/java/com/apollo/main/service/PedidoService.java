package com.apollo.main.service;

import com.apollo.main.dto.request.PedidoRequestDTO;
import com.apollo.main.dto.response.PedidoResponseDTO;
import com.apollo.main.model.Cliente;
import com.apollo.main.model.Funcionario;
import com.apollo.main.model.Pedido;
import com.apollo.main.model.TipoPedido;
import com.apollo.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

            BigDecimal valor = new BigDecimal(0.0);

            for(var item : dto.getItens()) {
                var produto = produtoRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + item.getProduto().getId()));

                if(produto.getQntdEstoque() < item.getQntd()) {
                    throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
                }

                produtoService.removeStock(produto.getId(), item.getQntd());

                item.setPedido(pedido);

                BigDecimal qntdBigDecimal = BigDecimal.valueOf(item.getQntd());

                valor.add(produto.getPrecoVenda().multiply(qntdBigDecimal));

                item.setPrecoCustoUN(produto.getPrecoVenda());
                item.setPrecoVendaUN(produto.getPrecoCusto());

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

    public PedidoResponseDTO getPedidoById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        return new PedidoResponseDTO(pedido);
    }

    public Page<PedidoResponseDTO> getAllPedidos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Pedido> pedidoPage = pedidoRepository.findAll(pageable);
        return pedidoPage.map(PedidoResponseDTO::new);
    }
}
