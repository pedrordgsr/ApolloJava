package com.apollo.main.controller;

import com.apollo.main.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pessoas")
@Tag(name = "Pessoa", description = "Endpoints para gerenciar pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    @Autowired
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPessoas( @RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(pessoaService.getAll(page, size));
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> findByName( @RequestParam String name,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(pessoaService.findAllByName(name, page, size));
    }

    @GetMapping("/validar-cpf-cnpj")
    @Operation(summary = "Valida se CPF/CNPJ já está cadastrado", 
               description = "Verifica se o CPF ou CNPJ informado já existe no cadastro de clientes, fornecedores ou funcionários")
    public ResponseEntity<?> validarCpfCnpj(@RequestParam String cpfcnpj) {
        boolean duplicated = pessoaService.isCpfCnpjDuplicated(cpfcnpj);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cpfcnpj", cpfcnpj);
        response.put("duplicated", duplicated);
        response.put("available", !duplicated);
        
        return ResponseEntity.ok(response);
    }

}
