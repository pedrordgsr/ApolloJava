package com.apollo.main.dto.response;

import com.apollo.main.model.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseDTO {
    private String username;
    private String email;
    private String idUsuario;
    private String idPessoa;
    private String status;
    private String nome;
    private String cargo;
    private Boolean isAdmin;

    public UsuarioResponseDTO(Usuario usuario){
        this.username = usuario.getUsername();
        this.email = usuario.getFuncionario().getEmail();
        this.idUsuario = usuario.getIdUsuario().toString();
        this.idPessoa = usuario.getFuncionario().getIdPessoa().toString();
        this.status = usuario.getStatusUsuario().name();
        this.nome = usuario.getFuncionario().getNome();
        this.cargo = usuario.getFuncionario().getCargo();
        this.isAdmin = usuario.getIsAdmin();
    }
}
