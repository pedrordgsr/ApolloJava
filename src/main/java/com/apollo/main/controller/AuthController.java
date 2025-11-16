package com.apollo.main.controller;

import com.apollo.main.dto.request.LoginRequest;
import com.apollo.main.dto.request.RegisterRequestDTO;
import com.apollo.main.dto.response.AuthResponseDTO;
import com.apollo.main.dto.response.TokenValidationResponse;
import com.apollo.main.security.JwtUtil;
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
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro de usuários")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
        summary = "Login de usuário",
        description = "Autentica um usuário e retorna um token JWT válido por 24 horas"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso",
            content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Credenciais inválidas"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponseDTO response = authService.login(request);
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
            content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos ou username já existe"
        )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            AuthResponseDTO response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @Operation(
        summary = "Validar token JWT",
        description = "Verifica se um token JWT é válido e retorna informações sobre ele"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token validado",
            content = @Content(schema = @Schema(implementation = TokenValidationResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Token inválido ou ausente"
        )
    })
    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                .body(new TokenValidationResponse(false, "Token não fornecido ou formato inválido"));
        }

        String token = authHeader.substring(7);
        
        try {
            boolean isValid = jwtUtil.isTokenValid(token);
            
            if (isValid) {
                String username = jwtUtil.extractUsername(token);
                java.util.Date expiration = jwtUtil.extractExpiration(token);
                
                return ResponseEntity.ok(
                    new TokenValidationResponse(true, username, expiration, "Token válido")
                );
            } else {
                return ResponseEntity.ok(
                    new TokenValidationResponse(false, "Token inválido ou expirado")
                );
            }
        } catch (Exception e) {
            return ResponseEntity.ok(
                new TokenValidationResponse(false, "Token inválido: " + e.getMessage())
            );
        }
    }
}