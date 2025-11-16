package com.apollo.main.controller;

import com.apollo.main.dto.request.ProdutoRequestDTO;
import com.apollo.main.dto.response.ProdutoResponseDTO;
import com.apollo.main.service.ProdutoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produto", description = "Endpoints para gerenciar produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> create(@Valid @RequestBody ProdutoRequestDTO dto){
        ProdutoResponseDTO response = produtoService.create(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(produtoService.getAll(page,size));
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> getByName(@RequestParam String nome,
                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size){
        Page<ProdutoResponseDTO> response = produtoService.findByName(nome, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(produtoService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO dto){
        ProdutoResponseDTO response = produtoService.update(id,dto);
        return ResponseEntity.ok(response);
    }

    // TODO: Melhorar response dessa requisição de add e remover estoque
    @PutMapping("/add/{id}")
    public ResponseEntity<String> addStock(@PathVariable Long id, @RequestBody int qntd){
        return ResponseEntity.ok(produtoService.addStock(id, qntd));
    }

    @PutMapping("/sub/{id}")
    public ResponseEntity<String> removeStock(@PathVariable Long id, @RequestBody int qntd){
        return ResponseEntity.ok(produtoService.removeStock(id, qntd));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        produtoService.delete(id);
        return ResponseEntity.ok("Produto " + id + " Deletado!");
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<?> switchStatus(@PathVariable Long id) {
        try{
            String response = produtoService.switchStatus(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
