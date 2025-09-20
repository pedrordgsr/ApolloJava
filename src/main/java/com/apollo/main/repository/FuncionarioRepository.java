package com.apollo.main.repository;

import com.apollo.main.model.Cliente;
import com.apollo.main.model.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Page<Funcionario> findFuncionarioByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
