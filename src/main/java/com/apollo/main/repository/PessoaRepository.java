package com.apollo.main.repository;

import com.apollo.main.model.Cliente;
import com.apollo.main.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Page<Pessoa> findPessoaByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Optional<Pessoa> findByCpfcnpj(String cpfcnpj);
    boolean existsByCpfcnpj(String cpfcnpj);
}
