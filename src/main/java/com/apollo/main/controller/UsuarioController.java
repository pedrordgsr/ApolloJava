package com.apollo.main.controller;

import com.apollo.main.dto.request.UpdateUsuarioRequestDTO;
import com.apollo.main.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints para obter informações dos usuários")
public class UsuarioController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Retornar todos os usuários")
    @GetMapping
    public ResponseEntity<?> getAllUsuarios(@RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "10") int size) {
        try{
            return ResponseEntity.ok(authService.getAllUsuarios(page,size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Retornar usuário por ID")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUsuarioById(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(authService.getUsuarioById(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Atualizar usuário (username e/ou senha)")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUsuario(@PathVariable Long userId, 
                                           @Valid @RequestBody UpdateUsuarioRequestDTO request) {
        try {
            return ResponseEntity.ok(authService.updateUsuario(userId, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
