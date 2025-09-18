package com.apollo.main.controller;


import com.apollo.main.dto.request.FornecedorRequestDTO;
import com.apollo.main.service.FornecedorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fornecedores")
@Tag(name = "Fornecedor", description = "Endpoints para gerenciar fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @Autowired
    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FornecedorRequestDTO dto){
        try {
            return ResponseEntity.ok(fornecedorService.create(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FornecedorRequestDTO dto){
        try {
            return ResponseEntity.ok(fornecedorService.update(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(fornecedorService.switchStatus(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam (value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(fornecedorService.getAll(page,size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(fornecedorService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> getAllByNome(@RequestParam String nome,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "10") int size){
        try {
            return ResponseEntity.ok(fornecedorService.findByName(nome, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            return ResponseEntity.ok(fornecedorService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
