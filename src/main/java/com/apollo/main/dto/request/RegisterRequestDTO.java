package com.apollo.main.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para registrar um novo usuário no sistema")
public class RegisterRequestDTO {
    @NotBlank(message = "Username é obrigatório")
    @Schema(description = "Nome de usuário único", example = "usuario123")
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário (será criptografada)", example = "senha123")
    private String senha;

    @Schema(description = "ID do funcionário associado (opcional)", example = "1")
    private Long funcionarioId;
}
