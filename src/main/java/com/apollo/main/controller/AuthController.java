package com.apollo.main.controller;

import com.apollo.main.dto.request.LoginRequest;
import com.apollo.main.dto.request.RegisterRequest;
import com.apollo.main.dto.response.AuthResponse;
import com.apollo.main.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro de usuários")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(
        summary = "Login de usuário",
        description = "Autentica um usuário e retorna um token JWT válido por 24 horas"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Credenciais inválidas"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Registrar novo usuário",
        description = "Cria um novo usuário no sistema e retorna um token JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário registrado com sucesso",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos ou username já existe"
        )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Excluir usuário",
        description = "Exclui um usuário do sistema pelo ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário excluído com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
        )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        try {
            authService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
