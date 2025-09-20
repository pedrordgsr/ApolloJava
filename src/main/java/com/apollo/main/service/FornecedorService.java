package com.apollo.main.service;

import com.apollo.main.dto.request.FornecedorRequestDTO;
import com.apollo.main.dto.response.FornecedorResponseDTO;
import com.apollo.main.model.Fornecedor;
import com.apollo.main.model.StatusAtivo;
import com.apollo.main.model.TipoPessoa;
import com.apollo.main.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    @Autowired
    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public FornecedorResponseDTO create(FornecedorRequestDTO dto){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setStatus(StatusAtivo.ATIVO);
        fornecedor.setNome(dto.getNome());
        fornecedor.setCategoria("Fornecedor");
        fornecedor.setTipoPessoa(TipoPessoa.valueOf(dto.getTipoPessoa()));
        fornecedor.setCpfcnpj(dto.getCpfcnpj());
        fornecedor.setIe(dto.getIe());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEndereco(dto.getEndereco());
        fornecedor.setBairro(dto.getBairro());
        fornecedor.setCidade(dto.getCidade());
        fornecedor.setUf(dto.getUf());
        fornecedor.setCep(dto.getCep());
        fornecedor.setTipoFornecedor(dto.getTipoFornecedor());
        fornecedor.setDataCadastro(LocalDateTime.now());
        Fornecedor response = fornecedorRepository.save(fornecedor);

        return new FornecedorResponseDTO(response);
    }

    public Page<FornecedorResponseDTO> getAll(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Fornecedor> fornecedores = fornecedorRepository.findAll(pageable);
        return fornecedores.map(FornecedorResponseDTO::new);
    }

    public FornecedorResponseDTO getById(Long id){
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        return new FornecedorResponseDTO(fornecedor);
    }

    public Page<FornecedorResponseDTO> findByName(String nome, int page, int size) {
        try{
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<Fornecedor> fornecedores = fornecedorRepository.findFornecedorByNomeContainingIgnoreCase(nome,pageable);
            return fornecedores.map(FornecedorResponseDTO::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FornecedorResponseDTO update(Long id, FornecedorRequestDTO dto){
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        fornecedor.setNome(dto.getNome());
        fornecedor.setTipoPessoa(TipoPessoa.valueOf(dto.getTipoPessoa()));
        fornecedor.setCpfcnpj(dto.getCpfcnpj());
        fornecedor.setIe(dto.getIe());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEndereco(dto.getEndereco());
        fornecedor.setBairro(dto.getBairro());
        fornecedor.setCidade(dto.getCidade());
        fornecedor.setUf(dto.getUf());
        fornecedor.setCep(dto.getCep());
        fornecedor.setTipoFornecedor(dto.getTipoFornecedor());
        Fornecedor response = fornecedorRepository.save(fornecedor);
        return new FornecedorResponseDTO(response);
    }

    public String delete(Long id){
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        fornecedorRepository.delete(fornecedor);
        return "Fornecedor " + fornecedor.getNome() + " deletado com sucesso";
    }

    public String switchStatus(Long id){
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        if(fornecedor.getStatus() == StatusAtivo.ATIVO){
            fornecedor.setStatus(StatusAtivo.INATIVO);
        } else {
            fornecedor.setStatus(StatusAtivo.ATIVO);
        }
        fornecedorRepository.save(fornecedor);
        return fornecedor.getStatus().name();
    }

}
