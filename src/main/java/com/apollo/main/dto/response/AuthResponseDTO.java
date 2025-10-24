package com.apollo.main.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Resposta de autenticação com token JWT")
public class AuthResponseDTO {
    @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "Nome de usuário autenticado", example = "admin")
    private String username;

    @Schema(description = "ID do usuário", example = "1")
    private Long usuarioId;

    @Schema(description = "ID do funcionario", example = "1")
    private Long funcionarioId;

    public AuthResponseDTO(String token, String username, Long usuarioId, Long funcionarioId) {
        this.token = token;
        this.username = username;
        this.usuarioId = usuarioId;
        this.funcionarioId = funcionarioId;
    }
}
