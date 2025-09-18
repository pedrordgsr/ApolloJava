package com.apollo.main.service;

import com.apollo.main.dto.request.ClienteRequestDTO;
import com.apollo.main.dto.response.ClienteResponseDTO;
import com.apollo.main.model.Cliente;
import com.apollo.main.model.StatusAtivo;
import com.apollo.main.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        cliente.setCategoria("Cliente");
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

    public Page<ClienteResponseDTO> getAll(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        return clientes.map(ClienteResponseDTO::new);
    }

    public ClienteResponseDTO getById(Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return new ClienteResponseDTO(cliente);
    }

    public Page<ClienteResponseDTO> findByName(String nome, int page, int size) {
        try{
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<Cliente> clientes = clienteRepository.findClienteByNomeContainingIgnoreCase(nome,pageable);
            return clientes.map(ClienteResponseDTO::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ClienteResponseDTO update(Long id, ClienteRequestDTO dto){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

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
        Cliente response = clienteRepository.save(cliente);
        return new ClienteResponseDTO(response);
    }

    public String delete(Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        clienteRepository.delete(cliente);
        return "Cliente " + cliente.getNome() + " deletado com sucesso";
    }

    public String switchStatus(Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        if(cliente.getStatus() == StatusAtivo.ATIVO){
            cliente.setStatus(StatusAtivo.INATIVO);
        } else {
            cliente.setStatus(StatusAtivo.ATIVO);
        }
        clienteRepository.save(cliente);
        return cliente.getStatus().name();
    }

}
