package com.apollo.main.service;

import com.apollo.main.dto.request.ProdutoRequestDTO;
import com.apollo.main.dto.response.ProdutoResponseDTO;
import com.apollo.main.model.Produto;
import com.apollo.main.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public ProdutoResponseDTO criar(ProdutoRequestDTO dto){
        Produto produto = new Produto();
        produto.setDescricao(dto.getDescricao());
        produto.setNome(dto.getNome());
        produto.setPrecoCusto(dto.getPrecoCusto() != null ? dto.getPrecoCusto() : 0.0);
        produto.setPrecoVenda(dto.getPrecoVenda() != null ? dto.getPrecoVenda() : 0.0);
        produto.setStatus(dto.getStatus());
        produto.setQntdEstoque(0); // sempre inicia zerado

        Produto response = produtoRepository.save(produto);
        return new ProdutoResponseDTO(response);
    }

    public List<ProdutoResponseDTO> listarTodos () {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ProdutoResponseDTO listarPorId(Long id){
        Produto produto = produtoRepository.findById(id)
                          .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));
        return new ProdutoResponseDTO(produto);
    }

    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        produto.setDescricao(dto.getDescricao());
        produto.setNome(dto.getNome());
        produto.setPrecoCusto(dto.getPrecoCusto());
        produto.setPrecoVenda(dto.getPrecoVenda());
        produto.setStatus(dto.getStatus());

        Produto response = produtoRepository.save(produto);
        return new ProdutoResponseDTO(response);
    }

    public void deletar(Long id){
        if(!produtoRepository.existsById(id)){
            throw new EntityNotFoundException("Produto não encontrado com id: " + id);
        }

        produtoRepository.deleteById(id);
    }

    @Transactional
    public String adicionarEstoque(Long id, int qntd){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        int novoEstoque = produto.getQntdEstoque() + qntd;
        produto.setQntdEstoque(novoEstoque);
        produtoRepository.save(produto);

        return ("Foi adicionado ao produto " + produto.getDescricao() + " " + qntd + " unidades.");
    }
}
