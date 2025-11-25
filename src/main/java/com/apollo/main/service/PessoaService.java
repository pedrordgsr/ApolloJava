package com.apollo.main.service;

import com.apollo.main.dto.response.PessoaResponseDTO;
import com.apollo.main.model.Pessoa;
import com.apollo.main.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    @Autowired
    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Page<PessoaResponseDTO> getAll(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Pessoa> pessoas = pessoaRepository.findAll(pageable);
        return pessoas.map(PessoaResponseDTO::new);
    }

    public Page<PessoaResponseDTO> findAllByName(String nome, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Pessoa> pessoas = pessoaRepository.findPessoaByNomeContainingIgnoreCase(nome, pageable);
        return pessoas.map(PessoaResponseDTO::new);
    }

    public boolean isCpfCnpjDuplicated(String cpfcnpj) {
        return pessoaRepository.existsByCpfcnpj(cpfcnpj);
    }

}
