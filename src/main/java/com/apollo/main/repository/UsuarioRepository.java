package com.apollo.main.repository;

import com.apollo.main.model.Funcionario;
import com.apollo.main.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);

    boolean existsByFuncionario(Funcionario funcionario);
}

