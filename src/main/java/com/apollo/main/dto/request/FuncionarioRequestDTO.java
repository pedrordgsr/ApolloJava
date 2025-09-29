package com.apollo.main.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class FuncionarioRequestDTO extends PessoaRequestDTO{
    private LocalDateTime dataAdmissao;
    private String cargo;
    private BigDecimal salario;
}
