package com.backend.OngConciencia.Dto;

import com.backend.OngConciencia.Model.Usuario;
import com.backend.OngConciencia.Model.UsuarioRole;

import java.math.BigInteger;

public record UsuarioResponseDto(String id, String nome, String email, String senha, UsuarioRole role, byte[] foto) {
    public UsuarioResponseDto(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(),usuario.getRole(), usuario.getFoto());
    }
}
