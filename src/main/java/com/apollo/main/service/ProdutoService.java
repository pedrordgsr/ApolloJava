package com.apollo.main.service;

import com.apollo.main.dto.request.ProdutoRequestDTO;
import com.apollo.main.dto.response.ProdutoResponseDTO;
import com.apollo.main.model.Produto;
import com.apollo.main.model.StatusAtivo;
import com.apollo.main.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public ProdutoResponseDTO create(ProdutoRequestDTO dto){
        Produto produto = new Produto();
        produto.setDescricao(dto.getDescricao());
        produto.setNome(dto.getNome());
        produto.setPrecoCusto(dto.getPrecoCusto() != null ? dto.getPrecoCusto() : new BigDecimal("0.0"));
        produto.setPrecoVenda(dto.getPrecoVenda() != null ? dto.getPrecoVenda() : new BigDecimal("0.0"));
        produto.setStatus(StatusAtivo.ATIVO);
        produto.setQntdEstoque(0);

        Produto response = produtoRepository.save(produto);
        return new ProdutoResponseDTO(response);
    }

    public Page<ProdutoResponseDTO> getAll (int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Produto> produtosPage = produtoRepository.findAll(pageable);
        return produtosPage.map(ProdutoResponseDTO::new);
    }

    public ProdutoResponseDTO getById(Long id){
        Produto produto = produtoRepository.findById(id)
                          .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));
        return new ProdutoResponseDTO(produto);
    }

    public Page<ProdutoResponseDTO> findByName(String nome, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Produto> produtosPage = produtoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        return produtosPage.map(ProdutoResponseDTO::new);
    }


    public ProdutoResponseDTO update(Long id, ProdutoRequestDTO dto){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        produto.setDescricao(dto.getDescricao());
        produto.setNome(dto.getNome());
        produto.setPrecoCusto(dto.getPrecoCusto());
        produto.setPrecoVenda(dto.getPrecoVenda());
        produto.setStatus(produto.getStatus());

        Produto response = produtoRepository.save(produto);
        return new ProdutoResponseDTO(response);
    }

    public void delete(Long id){
        if(!produtoRepository.existsById(id)){
            throw new EntityNotFoundException("Produto não encontrado com id: " + id);
        }

        produtoRepository.deleteById(id);
    }

    @Transactional
    public String addStock(Long id, int qntd){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        int novoEstoque = produto.getQntdEstoque() + qntd;
        produto.setQntdEstoque(novoEstoque);
        produtoRepository.save(produto);

        return ("Foi adicionado ao produto " + produto.getDescricao() + " " + qntd + " unidades.");
    }

    public String removeStock(Long id, int qntd){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        if(produto.getQntdEstoque() < qntd){
            throw new IllegalArgumentException("Quantidade em estoque insuficiente para remoção.");
        }

        int novoEstoque = produto.getQntdEstoque() - qntd;
        produto.setQntdEstoque(novoEstoque);
        produtoRepository.save(produto);

        return ("Foi removido do produto " + produto.getDescricao() + " " + qntd + " unidades.");
    }

    public String switchStatus(Long id){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        if(produto.getStatus() == StatusAtivo.ATIVO){
            produto.setStatus(StatusAtivo.INATIVO);
        } else {
            produto.setStatus(StatusAtivo.ATIVO);
        }

        produtoRepository.save(produto);
        return "O status do produto " + produto.getDescricao() + " foi alterado para " + produto.getStatus();
    }
}
