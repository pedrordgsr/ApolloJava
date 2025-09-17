package com.apollo.main.controller;

import com.apollo.main.dto.request.ProdutoRequestDTO;
import com.apollo.main.dto.response.ProdutoResponseDTO;
import com.apollo.main.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto){
        ProdutoResponseDTO response = produtoService.criar(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos(){
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> listarPorId(@PathVariable Long id){
        return ResponseEntity.ok(produtoService.listarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO dto){
        ProdutoResponseDTO response = produtoService.atualizar(id,dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/add/{id}")
    public ResponseEntity<String> adicionarEstoque(@PathVariable Long id, @RequestBody int qntd){
        return ResponseEntity.ok(produtoService.adicionarEstoque(id, qntd));
    }

    // TODO: Melhorar response dessa requisição de add e remover estoque

    @PutMapping("/sub/{id}")
    public ResponseEntity<String> removerEstoque(@PathVariable Long id, @RequestBody int qntd){
        return ResponseEntity.ok(produtoService.removerEstoque(id, qntd));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id){
        produtoService.deletar(id);
        return ResponseEntity.ok("Produto " + id + " Deletado!");
    }



}
