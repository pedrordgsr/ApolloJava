package com.apollo.main.service;

import com.apollo.main.dto.request.FuncionarioRequestDTO;
import com.apollo.main.dto.response.FuncionarioResponseDTO;
import com.apollo.main.model.Funcionario;
import com.apollo.main.model.StatusAtivo;
import com.apollo.main.model.TipoPessoa;
import com.apollo.main.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    @Autowired
    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public FuncionarioResponseDTO create(FuncionarioRequestDTO dto){
        Funcionario funcionario = new Funcionario();
        funcionario.setStatus(StatusAtivo.ATIVO);
        funcionario.setNome(dto.getNome());
        funcionario.setCategoria("Funcionario");
        funcionario.setTipoPessoa(TipoPessoa.valueOf(dto.getTipoPessoa()));
        funcionario.setCpfcnpj(dto.getCpfcnpj());
        funcionario.setIe(dto.getIe());
        funcionario.setEmail(dto.getEmail());
        funcionario.setTelefone(dto.getTelefone());
        funcionario.setEndereco(dto.getEndereco());
        funcionario.setBairro(dto.getBairro());
        funcionario.setCidade(dto.getCidade());
        funcionario.setUf(dto.getUf());
        funcionario.setCep(dto.getCep());
        funcionario.setDataAdmissao(dto.getDataAdmissao());
        funcionario.setSalario(dto.getSalario());
        funcionario.setDataCadastro(LocalDateTime.now());
        Funcionario response = funcionarioRepository.save(funcionario);

        return new FuncionarioResponseDTO(response);
    }

    public Page<FuncionarioResponseDTO> getAll(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Funcionario> funcionarios = funcionarioRepository.findAll(pageable);
        return funcionarios.map(FuncionarioResponseDTO::new);
    }

    public FuncionarioResponseDTO getById(Long id){
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
        return new FuncionarioResponseDTO(funcionario);
    }

    public Page<FuncionarioResponseDTO> findByName(String nome, int page, int size) {
        try{
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<Funcionario> funcionarios = funcionarioRepository.findFuncionarioByNomeContainingIgnoreCase(nome,pageable);
            return funcionarios.map(FuncionarioResponseDTO::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FuncionarioResponseDTO update(Long id, FuncionarioRequestDTO dto){
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));

        funcionario.setNome(dto.getNome());
        funcionario.setTipoPessoa(TipoPessoa.valueOf(dto.getTipoPessoa()));
        funcionario.setCpfcnpj(dto.getCpfcnpj());
        funcionario.setIe(dto.getIe());
        funcionario.setEmail(dto.getEmail());
        funcionario.setTelefone(dto.getTelefone());
        funcionario.setEndereco(dto.getEndereco());
        funcionario.setBairro(dto.getBairro());
        funcionario.setCidade(dto.getCidade());
        funcionario.setUf(dto.getUf());
        funcionario.setCep(dto.getCep());
        funcionario.setDataAdmissao(dto.getDataAdmissao());
        funcionario.setSalario(dto.getSalario());
        Funcionario response = funcionarioRepository.save(funcionario);
        return new FuncionarioResponseDTO(response);
    }

    public String delete(Long id){
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
        funcionarioRepository.delete(funcionario);
        return "Funcionario " + funcionario.getNome() + " deletado com sucesso";
    }

    public String switchStatus(Long id){
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
        if(funcionario.getStatus() == StatusAtivo.ATIVO){
            funcionario.setStatus(StatusAtivo.INATIVO);
        } else {
            funcionario.setStatus(StatusAtivo.ATIVO);
        }
        funcionarioRepository.save(funcionario);
        return funcionario.getStatus().name();
    }

    public String demote(Long id){
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));

        if(funcionario.getStatus() == StatusAtivo.INATIVO){
            return (funcionario.getStatus().name() + " ja foi desligado da empresa!");
        }
        else {
            funcionario.setStatus(StatusAtivo.INATIVO);
            funcionarioRepository.save(funcionario);
            return (funcionario.getStatus().name() + "Foi desligado da empresa!");
        }
    }

}
