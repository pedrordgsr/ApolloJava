package com.apollo.main.service;

import com.apollo.main.dto.request.PedidoProdutoRequestDTO;
import com.apollo.main.dto.request.PedidoRequestDTO;
import com.apollo.main.dto.response.PedidoResponseDTO;
import com.apollo.main.model.*;
import com.apollo.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
            pedido.setStatus(StatusPedido.ORCAMENTO);
            pedido.setVencimento(dto.getVencimento());
            pedido.setFormaPagamento(dto.getFormaPagamento());
            pedido.setPessoa(cliente);
            pedido.setFuncionario(funcionario);
            pedido.setItens(new ArrayList<>());
            pedido.setDataEmissao(LocalDateTime.now());

            BigDecimal valor = new BigDecimal(0.0);
            BigDecimal custo = new BigDecimal(0.0);

            for(PedidoProdutoRequestDTO itemDto : dto.getItens()) {
                var produto = produtoRepository.findById(itemDto.getProdutoId())
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + itemDto.getProdutoId()));

                PedidoProduto item = new PedidoProduto();
                item.setPedido(pedido);
                item.setProduto(produto);

                item.setQntd(itemDto.getQntd());

                if(produto.getQntdEstoque() < item.getQntd()) {
                    throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
                }

                produtoService.removeStock(produto.getId(), item.getQntd());

                BigDecimal qntdBigDecimal = BigDecimal.valueOf(item.getQntd());

                valor = valor.add(produto.getPrecoVenda().multiply(qntdBigDecimal));
                custo = custo.add(produto.getPrecoCusto().multiply(qntdBigDecimal));

                item.setPrecoVendaUN(produto.getPrecoVenda());
                item.setPrecoCustoUN(produto.getPrecoCusto());

                pedido.getItens().add(item);
            }

            pedido.setTotalVenda(valor);
            pedido.setTotalCusto(custo);
            Pedido savedPedido = pedidoRepository.save(pedido);
            return new PedidoResponseDTO(savedPedido);

        } else if (tipoPedido == TipoPedido.COMPRA) {
            Fornecedor fornecedor = fornecedorRepository.findById(dto.getIdPessoa())
                    .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado"));

            Funcionario funcionario = funcionarioRepository.findById(dto.getIdFuncionario())
                    .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

            Pedido pedido = new Pedido();
            pedido.setTipo(TipoPedido.COMPRA);
            pedido.setStatus(StatusPedido.ORCAMENTO);
            pedido.setVencimento(dto.getVencimento());
            pedido.setFormaPagamento(dto.getFormaPagamento());
            pedido.setPessoa(fornecedor);
            pedido.setFuncionario(funcionario);
            pedido.setItens(new ArrayList<>());
            pedido.setDataEmissao(LocalDateTime.now());

            BigDecimal valor = new BigDecimal(0.0);
            BigDecimal custo = new BigDecimal(0.0);

            for(PedidoProdutoRequestDTO itemDto : dto.getItens()) {
                var produto = produtoRepository.findById(itemDto.getProdutoId())
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + itemDto.getProdutoId()));

                PedidoProduto item = new PedidoProduto();
                item.setPedido(pedido);
                item.setProduto(produto);

                item.setQntd(itemDto.getQntd());

                if(produto.getQntdEstoque() < item.getQntd()) {
                    throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
                }

                produtoService.addStock(produto.getId(), item.getQntd());

                BigDecimal qntdBigDecimal = BigDecimal.valueOf(item.getQntd());

                valor = valor.add(produto.getPrecoVenda().multiply(qntdBigDecimal));
                custo = custo.add(produto.getPrecoCusto().multiply(qntdBigDecimal));

                item.setPrecoVendaUN(produto.getPrecoVenda());
                item.setPrecoCustoUN(produto.getPrecoCusto());

                pedido.getItens().add(item);
            }

            pedido.setTotalVenda(valor);
            pedido.setTotalCusto(custo);
            Pedido savedPedido = pedidoRepository.save(pedido);
            return new PedidoResponseDTO(savedPedido);

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
