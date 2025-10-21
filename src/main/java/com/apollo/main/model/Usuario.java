package com.apollo.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdUsuario;

    @Basic
    @Column(nullable = false, length = 45)
    @Enumerated(EnumType.STRING)
    private StatusAtivo statusUsuario;

    @Basic
    @Column(nullable = false, length = 45, unique = true)
    private String username;

    @Basic
    @Column(nullable = false, length = 255)
    private String senha;

    @OneToOne(cascade = CascadeType.ALL)
    private Funcionario funcionario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return statusUsuario == StatusAtivo.ATIVO;
    }
}
