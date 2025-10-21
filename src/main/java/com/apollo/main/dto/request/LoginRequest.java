package com.apollo.main.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para realizar login no sistema")
public class LoginRequest {
    @NotBlank(message = "Username é obrigatório")
    @Schema(description = "Nome de usuário", example = "admin")
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "senha123")
    private String senha;
}
