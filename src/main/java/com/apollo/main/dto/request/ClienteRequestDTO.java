package com.apollo.main.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequestDTO extends PessoaRequestDTO{
    @NotBlank(message = "Genero é obrigatório")
    @Schema(description = "Gênero", example = "NAO_INFORMADO")
    private String genero;
}
