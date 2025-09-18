package com.apollo.main.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FuncionarioRequestDTO extends PessoaRequestDTO{
    private LocalDateTime dataAdmissao;
    private Double salario;
}
