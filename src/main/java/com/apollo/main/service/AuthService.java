package com.apollo.main.service;

import com.apollo.main.dto.request.LoginRequest;
import com.apollo.main.dto.request.RegisterRequest;
import com.apollo.main.dto.response.AuthResponse;
import com.apollo.main.model.Funcionario;
import com.apollo.main.model.StatusAtivo;
import com.apollo.main.model.Usuario;
import com.apollo.main.repository.FuncionarioRepository;
import com.apollo.main.repository.UsuarioRepository;
import com.apollo.main.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getSenha())
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtUtil.generateToken(usuario);

        return new AuthResponse(token, usuario.getUsername(), usuario.getIdUsuario());
    }

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setStatusUsuario(StatusAtivo.ATIVO);

        if (request.getFuncionarioId() != null) {
            Funcionario funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
            usuario.setFuncionario(funcionario);
        }

        Usuario savedUsuario = usuarioRepository.save(usuario);
        String token = jwtUtil.generateToken(savedUsuario);

        return new AuthResponse(token, savedUsuario.getUsername(), savedUsuario.getIdUsuario());
    }

    public void deleteUser(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }
}
