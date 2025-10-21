package com.apollo.main.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Resposta de autenticação com token JWT")
public class AuthResponse {
    @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "Nome de usuário autenticado", example = "admin")
    private String username;

    @Schema(description = "ID do usuário", example = "1")
    private Long usuarioId;

    public AuthResponse(String token, String username, Long usuarioId) {
        this.token = token;
        this.username = username;
        this.usuarioId = usuarioId;
    }
}
