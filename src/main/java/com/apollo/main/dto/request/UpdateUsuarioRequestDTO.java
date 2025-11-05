package com.apollo.main.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para atualizar um usuário existente")
public class UpdateUsuarioRequestDTO {
    @Schema(description = "Novo nome de usuário (opcional)", example = "novoUsuario123")
    private String username;

    @Schema(description = "Nova senha (opcional, será criptografada)", example = "novaSenha123")
    private String senha;
}
