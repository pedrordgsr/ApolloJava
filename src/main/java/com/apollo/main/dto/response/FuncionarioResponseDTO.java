package com.apollo.main.dto.response;

import java.time.LocalDateTime;

import com.apollo.main.model.Funcionario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FuncionarioResponseDTO extends PessoaResponseDTO{
    private LocalDateTime dataAdmissao;
    private Double salario;
    private LocalDateTime dataDemissao;

    public FuncionarioResponseDTO(Funcionario funcionario) {
        super(funcionario);
        this.dataAdmissao = funcionario.getDataAdmissao();
        this.salario = funcionario.getSalario();
        this.dataDemissao = funcionario.getDataDemissao();
    }
}
