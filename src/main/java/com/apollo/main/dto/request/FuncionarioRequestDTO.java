package com.apollo.main.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class FuncionarioRequestDTO extends PessoaRequestDTO{

    @Schema(description = "Data de admissão")
    private LocalDateTime dataAdmissao;

    @NotBlank(message = "Cargo é obrigatório")
    @Schema(description = "Cargo", example = "Gerente")
    private String cargo;

    @Schema(description = "Salário", example = "2000.00")
    private BigDecimal salario;
}
