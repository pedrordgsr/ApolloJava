package com.apollo.main.service;

import com.apollo.main.dto.request.ClienteRequestDTO;
import com.apollo.main.dto.response.ClienteResponseDTO;
import com.apollo.main.model.Cliente;
import com.apollo.main.model.StatusAtivo;
import com.apollo.main.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponseDTO create(ClienteRequestDTO dto){
        Cliente cliente = new Cliente();
        cliente.setStatus(StatusAtivo.ATIVO);
        cliente.setNome(dto.getNome());
        cliente.setCategoria(dto.getCategoria());
        cliente.setCpfcnpj(dto.getCpfcnpj());
        cliente.setIe(dto.getIe());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());
        cliente.setBairro(dto.getBairro());
        cliente.setCidade(dto.getCidade());
        cliente.setUf(dto.getUf());
        cliente.setCep(dto.getCep());
        cliente.setGenero(dto.getGenero());
        cliente.setDataCadastro(LocalDateTime.now());
        Cliente response = clienteRepository.save(cliente);

        return new ClienteResponseDTO(response);
    }

}
